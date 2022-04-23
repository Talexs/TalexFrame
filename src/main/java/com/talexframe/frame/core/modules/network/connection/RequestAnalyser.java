package com.talexframe.frame.core.modules.network.connection;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.event.events.request.RequestCannotGetTokenEvent;
import com.talexframe.frame.core.pojo.annotations.*;
import com.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talexframe.frame.core.pojo.dao.factory.redis.Redis;
import com.talexframe.frame.core.pojo.dao.factory.redis.TRedisCache;
import com.talexframe.frame.core.pojo.wrapper.ResultData;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.utils.ReqMethodUtil;
import com.talexframe.frame.utils.UrlUtil;
import com.talexframe.frame.utils.ValidatorUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 请求解析器 <br /> {@link com.talexframe.frame.core.modules.network.connection Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 06:57 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class RequestAnalyser {

    private static final NetworkManager networkManager = new NetworkManager();

    private static final Redis redis = new DAOManager.ProcessorGetter<Redis>(Redis.class).getProcessor();
    private final WrappedResponse wr;
    private final HttpServletRequest request;
    private final long time;

    public RequestAnalyser(WrappedResponse wr, long time) {

        this.wr = wr;
        this.time = time;
        this.request = wr.getRequest();

        process();

    }

    public static void scanRequests(TApp tApp) {

        networkManager.scanClassForRequests(tApp);

    }

    public static void removeRequests(TApp tApp) {

        networkManager.removeRequestsInClass(tApp);

    }

    @SneakyThrows
    private void process() {

        log.info("[解析层] RequestID: " + request.getSession().getId());

        for ( RequestReceiver clzReceiver : networkManager.receivers.values() ) {

            for ( RequestReceiver.RequestMethodReceiver methodReceiver : clzReceiver.getMethods() ) {

                if ( wr.getResponse().isCommitted() ) {
                    return;
                }

                switch ( methodReceiver.accessChecker(wr) ) {

                    case -1:
                        continue;
                    case 1:
                        return;

                }

                log.info("[解析层] Access in " + methodReceiver.tRequest.value());

                if ( !clzReceiver.accessChecker(wr) ) {

                    continue;

                }

                List<Object> params = new ArrayList<>();

                params.add(wr);

                String str = wr.getRequest().getBody();
                JSONObject json = null;

                if ( methodReceiver.tRequest.parseJSON() ) {

                    if ( StrUtil.isBlankIfStr(str) ) {

                        wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Data error");

                        log.info("[解析层] AccessDenied # MissingBodyData");

                        return;

                    }

                    json = JSONUtil.parseObj(str);

                }

                String url = wr.getRequest().getRequestURI();

                for ( RequestReceiver.RequestMethodReceiver.RequestParameterReceiver paramReceiver : methodReceiver.getParamReceivers() ) {

                    if ( !paramReceiver.processUrlParam(url, params) ) {
                        return;
                    }

                    if ( json == null && methodReceiver.tRequest.parseJSON() && !Objects.equals(methodReceiver.supportMethodName, "GET") ) {

                        log.warn("[解析层] DataNull # MissingBodyData");

                        params.add(null);

                        continue;

                    }

                    if ( paramReceiver.tParam != null ) {

                        TParam param = paramReceiver.tParam;
                        Parameter parameter = paramReceiver.getParameter();

                        TPParamValidator validator = parameter.getAnnotation(TPParamValidator.class);

                        try {

                            String fieldName = param.field() != null ? param.field() : parameter.getName();

                            if ( !json.containsKey(fieldName) ) {

                                if ( extracted(wr, params, parameter, param) ) {

                                    return;

                                }

                            } else {

                                Object obj = json.get(fieldName, parameter.getType());

                                if ( validator != null ) {

                                    int back = ValidatorUtil.validateData(validator, obj);

                                    if( back != 100 ) {

                                        wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Data error");

                                        switch ( back ) {

                                            case ValidatorUtil.MISS_MAX:
                                                log.info("[解析层] AccessDenied # MissingMax " + fieldName);
                                            case ValidatorUtil.MISS_MIN:
                                                log.info("[解析层] AccessDenied # MissingMin " + fieldName);
                                            case ValidatorUtil.MISS_PATTERN:
                                                log.info("[解析层] AccessDenied # MissingPattern " + fieldName);
                                            case ValidatorUtil.MISS_MAX_LENGTH:
                                                log.info("[解析层] AccessDenied # MissingMaxLength " + fieldName);
                                            case ValidatorUtil.MISS_MIN_LENGTH:
                                                log.info("[解析层] AccessDenied # MissingMinLength " + fieldName);
                                            case ValidatorUtil.MISS_ASSERT:
                                                log.info("[解析层] AccessDenied # MissingAssert " + fieldName);
                                            case ValidatorUtil.MISS_DATA:
                                                log.info("[解析层] AccessDenied # MissingData " + fieldName);

                                        }

                                        return;

                                    }

                                }

                                params.add(obj);

                            }

                        } catch ( ConvertException e ) {

                            if ( extracted(wr, params, parameter, param) ) {

                                e.printStackTrace();

                                return;

                            }

                        }

                    }

                }

                log.info("[接口层] Processing by @" + clzReceiver.ownClass);

                long a = System.currentTimeMillis();

                // Class<?> returnClz = ownMethod.getReturnType();

                log.debug("params: " + params);
                Object object = methodReceiver.ownMethod.invoke(clzReceiver.controller, params.toArray());

                if ( object != null ) {

                    HttpServletResponse response = wr.getResponse();
                    OutputStream os = response.getOutputStream();

                    response.setStatus(200);

                    String str = JSONUtil.toJsonStr(object);

                    response.setContentType("application/json");

                    os.write(str.getBytes(StandardCharsets.UTF_8));

                    os.flush();

                    log.info("[应用层] OK Return: " + str);

                    methodReceiver.processRedisCache(json, params, object);

                }

                a = System.currentTimeMillis() - a;

                if ( a > 300 ) {

                    log.warn("[应用层] 处理完毕 - 耗时较长，请优化接口！ 耗时: " + a + "ms");

                } else {

                    log.info("[应用层] 处理完毕 耗时: " + a + "ms");

                }

                log.info("[解析层] Access in " + (System.currentTimeMillis() - time) + "ms totally");

                return;

            }

        }

    }

    private boolean extracted(WrappedResponse wr, List<Object> params, Parameter parameter, TParam param) {

        if ( !param.value() ) {

            wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Parameter error");

            log.info("[接口层] 请求参数错误 - " + ( !StrUtil.isBlankIfStr(param.field()) ? param.field() : parameter.getName() ));

            return true;

        } else {

            params.add(null);

        }

        return false;

    }

    @Accessors( chain = true )
    @Getter
    private static class RequestReceiver {

        private final TApp controller;
        private final Class<?> ownClass;
        private final TReqLoginChecker tReqLoginChecker;
        private final TRestFul tRestFul;
        private final List<RequestMethodReceiver> methods = new ArrayList<>();
        private RateLimiter rateLimiter;

        public RequestReceiver(TApp controller, Class<?> ownClass, TReqLoginChecker tReqLoginChecker, TRestFul tRestFul) {

            this.controller = controller;
            this.ownClass = ownClass;
            this.tReqLoginChecker = tReqLoginChecker;
            this.tRestFul = tRestFul;

        }

        /**
         * 返回是否放行 根据 @TReqLoginChecker 来判断
         */
        public boolean accessLogin(WrappedResponse wr) {

            if ( tReqLoginChecker == null ) {
                return true;
            }

            // 注解要求登录 但是没有登录 和 注解要求不登录才可以请求 而已经登录
            if ( tReqLoginChecker.value() && !StpUtil.isLogin() ) {

                wr.returnDataByFailed("请先登录.");

                log.info("[解析层] @TReqLoginChecker # AccessDenied - NeedLogin");

                return false;

            } else if ( !tReqLoginChecker.value() && StpUtil.isLogin() ) {

                wr.returnDataByFailed("已登录.");

                log.info("[解析层] @TReqLoginChecker # AccessDenied - AlreadyLogin");

                return false;

            }

            return true;

        }

        public RequestReceiver setRateLimiter(TRequestLimit clzLimit) {

            if ( clzLimit == null ) {
                return this;
            }

            RateLimiter.create(clzLimit.QPS(), clzLimit.timeout(), clzLimit.timeUnit());

            return this;

        }

        /**
         * 是否可以通过
         *
         * @return 返回真则可以通过
         */
        public boolean doRateLimiter() {

            if ( rateLimiter == null ) {
                return true;
            }

            if( !rateLimiter.tryAcquire() ) return false;

            rateLimiter.acquire();

            return true;

        }

        /**
         * 总体通行判断 整合 doRateLimiter 与 accessLogin
         */
        public boolean accessChecker(WrappedResponse wr) {

            if ( !doRateLimiter() ) {

                RequestCannotGetTokenEvent rcg = new RequestCannotGetTokenEvent(wr, RequestCannotGetTokenEvent.LimiterType.METHOD);

                if ( !rcg.isCancelled() ) {

                    wr.returnDataByFailed(ResultData.ResultEnum.SERVICE_LIMITED);

                    log.info("[解析层] @RateLimiter # AccessDenied - RateLimited");

                    return false;

                }

            }

            return accessLogin(wr);

        }

        public void addMethod(RequestMethodReceiver methodReceiver) {

            this.methods.add(methodReceiver);

        }

        @Getter
        @Accessors( chain = true )
        private static class RequestMethodReceiver {

            private final TReqLoginChecker tReqLoginChecker;
            private final TRequest tRequest;
            private final RequestReceiver ownReceiver;
            private final List<RequestParameterReceiver> paramReceivers = new ArrayList<>();
            private RateLimiter rateLimiter;
            @Setter
            private TReqSupportMethod tReqSupportMethod;

            @Setter
            private TRedisCache tRedisCache;
            private String supportMethodName;
            private Method ownMethod;

            public RequestMethodReceiver(RequestReceiver ownReceiver, TReqLoginChecker tReqLoginCheck, TRequest tRequest) {

                this.ownReceiver = ownReceiver;
                this.tReqLoginChecker = tReqLoginCheck;
                this.tRequest = tRequest;

            }

            public RequestMethodReceiver setRateLimiter(TRequestLimit clzLimit) {

                if ( clzLimit == null ) {
                    return this;
                }

                RateLimiter.create(clzLimit.QPS(), clzLimit.timeout(), clzLimit.timeUnit());

                return this;

            }

            public void processRedisCache(JSONObject json, List<Object> params, Object data) {

                RedisTemplate<String, Object> template = redis.getConfig().getRedisTemplate();

                template.execute((RedisCallback<Object>) connection -> {

                    /**
                     *
                     * 解析整个key
                     * 首先解析文本 如果以#开头则解析为变量
                     * #result 解析为返回的json结果 （如果返回的内容是基本数据类型则转换为字符串）
                     * #params 解析为参数列表 如#params[x].xxx
                     * #data 解析为数据内容 如#data[x].xxx
                     *
                     */
                    String key = tRedisCache.value();

                    if ( key.startsWith("#") ) {

                        if ( key.equals("#result") ) {

                            key = new JSONObject().putOpt("data", data).getStr("data");

                        }

                        if ( key.startsWith("#params") ) {

                            int index = Integer.parseInt(key.substring(7, 8));

                            if ( index + 1 > params.size() ) {

                                throw new RuntimeException("params index out of range");

                            }

                            Object obj = params.get(index);
                            String value = null;
                            JSONObject json1 = new JSONObject().putOpt("data", obj);

                            if ( key.contains("].") ) {

                                String[] args = key.split(".");

                                for ( int i = 1; i < args.length; ++i ) {

                                    String arg = args[i];

                                    Object o = json1.get(arg);

                                    if ( o instanceof JSONObject ) {

                                        json1 = (JSONObject) o;
                                        value = json1.toString();

                                    } else {

                                        value = String.valueOf(o);

                                    }

                                }

                                key = value;

                            }

                        }

                        if ( key.startsWith("#data") ) {

                            String name = key.substring(4, key.indexOf("]"));

                            if ( !json.containsKey(name) ) {

                                throw new RuntimeException("data not found : " + name);

                            }

                            Object obj = json.get(name);
                            String value = null;
                            JSONObject json1 = new JSONObject().putOpt("data", obj);

                            if ( key.contains("].") ) {

                                String[] args = key.split(".");

                                for ( int i = 1; i < args.length; ++i ) {

                                    String arg = args[i];

                                    Object o = json1.get(arg);

                                    if ( o instanceof JSONObject ) {

                                        json1 = (JSONObject) o;
                                        value = json1.toString();

                                    } else {

                                        value = String.valueOf(o);

                                    }

                                }

                                key = value;

                            }

                        }

                    }

                    if ( tRedisCache.delete() ) {

                        return connection.del(key.getBytes(StandardCharsets.UTF_8));

                    } else {

                        JSONObject cacheValue = JSONUtil.parseObj(data);

                        connection.set(key.getBytes(StandardCharsets.UTF_8), cacheValue.toString().getBytes(StandardCharsets.UTF_8));
                        if ( tRedisCache.expireTime() > 0 ) {

                            connection.expire(key.getBytes(StandardCharsets.UTF_8), tRedisCache.expireTime());

                        }

                    }

                    return 1L;
                });

            }

            /**
             * 返回真则代表地址一致
             **/
            public boolean urlChecker(String comingUrl) {

                return UrlUtil.advancedUrlChecker(tRequest.value(), comingUrl);

            }

            /**
             * 返回真则代表方法一致
             **/
            public boolean methodChecker(String comingMethod) {

                return tReqSupportMethod == null || comingMethod.equalsIgnoreCase(this.supportMethodName);

            }

            public String getSupportMethod() {

                this.supportMethodName = ReqMethodUtil.getMethodName(tReqSupportMethod);

                return this.supportMethodName;

            }

            /**
             * 是否可以通过
             *
             * @return 返回真则可以通过
             */
            public boolean doRateLimiter() {

                if ( rateLimiter == null ) {
                    return true;
                }

                if( !rateLimiter.tryAcquire() ) return false;

                rateLimiter.acquire();

                return true;

            }

            /**
             * 返回是否放行 根据 @TReqLoginChecker 来判断
             */
            public boolean accessLogin(WrappedResponse wr) {

                if ( tReqLoginChecker == null ) {
                    return true;
                }

                // 注解要求登录 但是没有登录 和 注解要求不登录才可以请求 而已经登录
                if ( tReqLoginChecker.value() && !StpUtil.isLogin() ) {

                    wr.returnDataByFailed("请先登录.");

                    log.info("[解析层] @TReqLoginChecker # AccessDenied - NeedLogin");

                    return false;

                } else if ( !tReqLoginChecker.value() && StpUtil.isLogin() ) {

                    wr.returnDataByFailed("已登录.");

                    log.info("[解析层] @TReqLoginChecker # AccessDenied - AlreadyLogin");

                    return false;

                }

                return true;

            }

            public RequestMethodReceiver setMethod(Method method) {

                this.ownMethod = method;

                if ( !method.isAccessible() ) {
                    method.setAccessible(true);
                }

                return this;

            }

            /**
             * 总体通行判断 整合 doRateLimiter 与 accessLogin
             *
             * @return 0为通过 1为错误 -1为不匹配
             */
            public int accessChecker(WrappedResponse wr) {

                if ( !urlChecker(wr.getRequest().getRequestURI()) ) {
                    return -1;
                }

                if ( !methodChecker(wr.getRequest().getMethod()) ) {
                    return -1;
                }

                if ( !doRateLimiter() ) {

                    RequestCannotGetTokenEvent rcg = new RequestCannotGetTokenEvent(wr, RequestCannotGetTokenEvent.LimiterType.METHOD);

                    if ( !rcg.isCancelled() ) {

                        wr.returnDataByFailed(ResultData.ResultEnum.SERVICE_LIMITED);

                        log.info("[解析层] @RateLimiter # AccessDenied - RateLimited");

                        return 1;

                    }

                }

                if ( !accessLogin(wr) ) {

                    return 1;

                }

                return 0;

            }

            public void addParam(RequestParameterReceiver receiver) {

                this.paramReceivers.add(receiver);

            }

            @Getter
            @Setter
            @Accessors( chain = true )
            private static class RequestParameterReceiver {

                private final RequestMethodReceiver ownMethodReceiver;
                private final Parameter parameter;

                private final TParam tParam;
                private final TUrlParam tUrlParam;

                public RequestParameterReceiver(Parameter parameter, RequestMethodReceiver ownMethodReceiver, @NonNull TParam tParam) {

                    this.parameter = parameter;
                    this.ownMethodReceiver = ownMethodReceiver;
                    this.tParam = tParam;
                    this.tUrlParam = null;

                }

                public RequestParameterReceiver(Parameter parameter, RequestMethodReceiver ownMethodReceiver, @NonNull TUrlParam tUrlParam) {

                    this.parameter = parameter;
                    this.ownMethodReceiver = ownMethodReceiver;
                    this.tParam = null;
                    this.tUrlParam = tUrlParam;

                }

                public boolean processUrlParam(String url, List<Object> params) {

                    if ( this.tUrlParam == null ) {
                        return true;
                    }

                    String[] requestUrls = url.split("/");
                    String[] requireUrls = ownMethodReceiver.tRequest.value().split("/");

                    if ( requestUrls.length != requireUrls.length ) {

                        log.info("[解析层] AccessDenied - missing parameters # " + url);

                        return false;

                    }

                    String fieldName = tUrlParam.value();
                    String[] originUrl = UrlUtil.formatUrl(ownMethodReceiver.tRequest.value()).split("/");
                    String[] urls = UrlUtil.formatUrl(url).split("/");

                    for( int i = 0; i < originUrl.length; ++i ) {

                        String thisUrl = originUrl[i];
                        log.debug("[解析层] thisUrl: " + thisUrl + " | fieldName: " + fieldName);

                        if( thisUrl.equalsIgnoreCase("{" + fieldName + "}") ) {

                            String obj = urls[i];

                            params.add(obj);

                            log.debug("[解析层] add param: " + obj);

                            return true;

                        }

                    }

                    return false;

                }

            }

        }

    }

    private static class NetworkManager {

        private final Map<Class<?>, RequestReceiver> receivers = new HashMap<>();

        private void removeRequestsInClass(TApp controller) {

            this.receivers.remove(controller.getClass());

        }

        private void scanClassForRequests(TApp controller) {

            Class<?> clz = controller.getClass();

            RequestReceiver requestReceiver = new RequestReceiver(controller, clz, clz.getAnnotation(TReqLoginChecker.class), clz.getAnnotation(TRestFul.class))
                    .setRateLimiter(clz.getAnnotation(TRequestLimit.class));

            for ( Method method : clz.getMethods() ) {

                TRequest request = method.getAnnotation(TRequest.class);

                if ( request == null ) {

                    continue;

                }

                RequestReceiver.RequestMethodReceiver requestMethodReceiver = new RequestReceiver.RequestMethodReceiver(requestReceiver,
                        method.getAnnotation(TReqLoginChecker.class), request).setRateLimiter(method.getAnnotation(TRequestLimit.class));

                requestMethodReceiver.setTReqSupportMethod(method.getAnnotation(TReqSupportMethod.class));
                requestMethodReceiver.setTRedisCache(method.getAnnotation(TRedisCache.class));

                requestMethodReceiver.setMethod(method);

                requestReceiver.addMethod(requestMethodReceiver);

                for ( Parameter parameter : method.getParameters() ) {

                    RequestReceiver.RequestMethodReceiver.RequestParameterReceiver paramReceiver;

                    TParam param = parameter.getAnnotation(TParam.class);

                    if ( param == null ) {

                        TUrlParam urlParam = parameter.getAnnotation(TUrlParam.class);

                        if ( urlParam == null ) {
                            continue;
                        }

                        paramReceiver = new RequestReceiver.RequestMethodReceiver.RequestParameterReceiver(parameter, requestMethodReceiver, urlParam);

                    } else {

                        paramReceiver = new RequestReceiver.RequestMethodReceiver.RequestParameterReceiver(parameter, requestMethodReceiver, param);

                    }

                    requestMethodReceiver.addParam(paramReceiver);

                }

                log.debug("[解析层] 扫描到请求: " + request.value() + " #" + requestMethodReceiver.getSupportMethod());

            }

            this.receivers.put(clz, requestReceiver);

        }

    }

}
