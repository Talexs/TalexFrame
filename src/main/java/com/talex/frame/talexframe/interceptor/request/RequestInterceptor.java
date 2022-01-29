package com.talex.frame.talexframe.interceptor.request;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.talex.frame.talexframe.function.controller.TController;
import com.talex.frame.talexframe.function.controller.TControllerManager;
import com.talex.frame.talexframe.function.event.events.request.*;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.annotations.*;
import com.talex.frame.talexframe.utils.ReqMethodUtil;
import com.talex.frame.talexframe.utils.UrlUtil;
import com.talex.frame.talexframe.wrapper.BodyCopyHttpServletRequestWrapper;
import com.talex.frame.talexframe.wrapper.ResultData;
import com.talex.frame.talexframe.wrapper.WrappedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 请求过滤器
 * <br /> {@link com.talex.frame.talexframe.interceptor.request Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 16:47 <br /> Project: TalexFrame <br />
 */
// @Component
// @Order
@Slf4j
public final class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if( TFrame.tframe == null ) return false;

        TFrame tframe = TFrame.tframe;

        BodyCopyHttpServletRequestWrapper copiedRequest = new BodyCopyHttpServletRequestWrapper(request);

        WrappedResponse wr = new WrappedResponse(copiedRequest, response);

        PreHandleRequest event = new PreHandleRequest(wr, response, handler);

        tframe.callEvent(event);

        if( event.isCancelled() ) return false;

        RateLimiter globalLimiter = tframe.getRateLimiterManager().getGlobalLimiter();

        if( !globalLimiter.tryAcquire(100, TimeUnit.MILLISECONDS) ) {

            RequestCannotGetTokenEvent rcg = new RequestCannotGetTokenEvent(wr, RequestCannotGetTokenEvent.LimiterType.GLOBAL);

            if( !rcg.isCancelled() ) {

                wr.returnDataByFailed(ResultData.ResultEnum.SERVICE_LIMITED, "服务器繁忙");

                log.warn("[RateLimiter] 请求已被拦截 - 服务器繁忙 (Global)");

                return false;

            }

        }

        globalLimiter.acquire();

        if( !response.containsHeader("Access-Control-Allow-Origin") ) {

            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            response.addHeader("Access-Control-Allow-Headers", "*");
            response.addHeader("Access-Control-Max-Age", "1728000");

        }

        if ( request.getMethod().equals("OPTIONS")) {

            RequestCorsTryEvent e = new RequestCorsTryEvent(wr);

            tframe.callEvent(e);

            if(e.isCancelled()) return false;

            log.info("[接口层] 请求 " + request.getRequestURI() + " #来自: " + request.getSession().getId() + " 已批准跨域!");

            wr.returnDataByOK("SUCCESS");

            return false;

        }

        log.info("[接口层] 新的请求 " + request.getRequestURI() + " #来自: " + request.getSession().getId() + " (" + request.getRemoteHost() + ")");

        TControllerManager manager = tframe.getControllerManager();

        if( manager == null ) {

            wr.returnDataByFailed("Frame not install yet.");

            return false;

        }

        for( TController controller : manager.getControllers().values() ) {

            processClassRequest(controller, wr);

        }

        if( !response.isCommitted() ) {

            wr.returnDataByFailed(ResultData.ResultEnum.NOT_FOUND, "请重新确认 URL 正误.");

        }

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if( TFrame.tframe == null ) return;

        BodyCopyHttpServletRequestWrapper copiedRequest = new BodyCopyHttpServletRequestWrapper(request);

        PostHandleRequest event = new PostHandleRequest(copiedRequest, response, handler, modelAndView);

        TFrame.tframe.callEvent(event);

        // if( event.isCancelled() ) return;

    }

    private void processClassRequest(TController controller, WrappedResponse wr) throws IllegalAccessException, InvocationTargetException {

        TFrame tframe = TFrame.tframe;

        Class<?> clz = controller.getClass();

        log.debug("[接口层] 检查 " + clz.getName() + " 的匹配...");

        RateLimiter clzLimiter = tframe.getRateLimiterManager().getClassLimiterMapper().get(clz);

        if( clzLimiter != null ) {

            if( !clzLimiter.tryAcquire() ) {

                RequestCannotGetTokenEvent rcg = new RequestCannotGetTokenEvent(wr, RequestCannotGetTokenEvent.LimiterType.CLASS);

                if( !rcg.isCancelled() ) {

                    wr.returnDataByFailed(ResultData.ResultEnum.SERVICE_LIMITED, "服务器繁忙");

                    log.warn("[RateLimiter] 请求已被拦截 - 服务器繁忙 (Class) @" + clz.getName());

                    return;

                }

            }

            clzLimiter.acquire();

        }

        TReqLoginChecker tReqLoginChecker = clz.getAnnotation(TReqLoginChecker.class);

        if( tReqLoginChecker != null && !StpUtil.isLogin() ) {

            wr.returnDataByFailed("请先登录.");

            log.info("[@TReqLoginChecker] 请求已被拦截 - 请先登录 @" + clz.getName());

            return;

        }

        for( Method method : clz.getMethods() ) {

            log.debug("[接口层] 检查 " + method.getName() + " 的匹配...");

            TRequest request = method.getAnnotation(TRequest.class);

            if( request == null ) {

                log.debug("[接口层] " + method.getName() + " 没有 @TRequest 注解, 跳过...");

                continue;

            }

            if( !UrlUtil.advancedUrlChecker(request.value(), wr.getRequest().getRequestURI())) {

                log.debug("[接口层] " + method.getName() + " 没有匹配, 跳过... (" + wr.getRequest().getRequestURI() + " # " + request.value() + ")");

                continue;

            }

            TReqSupportMethod repSupportMethod = method.getAnnotation(TReqSupportMethod.class);

            if( repSupportMethod != null && !ReqMethodUtil.checkStatus(wr.getRequest().getMethod(), repSupportMethod) ) {

                wr.returnDataByFailed("Error request method.");

                log.info("[接口层] 请求 " + wr.getRequest().getRequestURI() + " #来自: " + wr.getRequest().getSession().getId() + " (" + wr.getRequest().getRemoteHost() + ") 方法不支持.");

                return;

            }

            RateLimiter methodLimiter = tframe.getRateLimiterManager().getMethodLimiterMapper().get(method);

            if( methodLimiter != null ) {

                if( !methodLimiter.tryAcquire() ) {

                    RequestCannotGetTokenEvent rcg = new RequestCannotGetTokenEvent(wr, RequestCannotGetTokenEvent.LimiterType.METHOD);

                    if( !rcg.isCancelled() ) {

                        wr.returnDataByFailed(ResultData.ResultEnum.SERVICE_LIMITED, "服务器繁忙");

                        log.warn("[RateLimiter] 请求已被拦截 - 服务器繁忙 (Method) @" + clz.getName() + "." + method.getName());

                        return;

                    }

                }

                methodLimiter.acquire();

            }

            if( tReqLoginChecker == null ) {

                tReqLoginChecker = method.getAnnotation(TReqLoginChecker.class);

                if( tReqLoginChecker != null && !StpUtil.isLogin() ) {

                    wr.returnDataByFailed("请先登录.");

                    log.info("[@TReqLoginChecker] 请求已被拦截 - 请先登录 @" + clz.getName() + "." + method.getName());

                    return;

                }

            }

            List<Object> params = new ArrayList<>();

            params.add(wr);

            if( !method.isAccessible() ) method.setAccessible(true);

            String str = wr.getRequest().getBody();
            JSONObject json = null;

            if( !request.parseJSON() ) {

                log.info("[接口层] 请求无需解析JSON @" + clz.getName() + "." + method.getName());

                // method.invoke(controller, params.toArray());

                // return;

            } else {

                if( StrUtil.isBlankIfStr(str) ) {

                    wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Data error");

                    log.info("[接口层] 请求数据错误 - 无任何数据");

                    return;

                }

                json = JSONUtil.parseObj(str);

            }

            String url = wr.getRequest().getRequestURI();

            for( Parameter parameter : method.getParameters() ) {

                TParam param = parameter.getAnnotation(TParam.class);

                if( param == null ) {

                    TUrlParam urlParam = parameter.getAnnotation(TUrlParam.class);

                    if( urlParam == null ) { continue; }

                    String[] requestUrls = url.split("/");
                    String[] requireUrls = request.value().split("/");

                    if( requestUrls.length != requireUrls.length ) {

                        log.info("[接口层] 请求路径错误 - " + url + " # 缺少参数");

                        return;

                    }

                    for( int i = 0; i < requireUrls.length; ++i ) {

                        String subUrl = requireUrls[i];

                        log.debug("[接口层] 请求路径 #" + i + " - " + url + " # " + subUrl + " / " + requireUrls[i]);

                        if ( subUrl.startsWith("{") && subUrl.endsWith("}") ) {

                            params.add(requestUrls[i]);

                        }

                    }

                    continue;

                }

                if( json == null ) {

                    log.warn("[接口层] 自动填充null # 请求数据错误 - 无任何数据 使用数据 @" + parameter.getName() + "." + clz.getName() + "." + method.getName());

                    params.add(null);

                    continue;

                }

                try {

                    String fieldName = param.field() != null ? param.field() : parameter.getName();

                    if( !json.containsKey(fieldName) ) {

                        if ( extracted(wr, params, parameter, param) ) {

                            return;

                        }

                    } else {

                        Object obj = json.get(fieldName, parameter.getType());

                        params.add(obj);

                    }

                } catch ( ConvertException e ) {

                    if ( extracted(wr, params, parameter, param) ) {

                        e.printStackTrace();

                        return;

                    }

                }

            }

            log.info("[接口层] 由 @" + clz + " 开始处理.");

            long a = System.nanoTime();

            method.invoke(controller, params.toArray());

            a = System.nanoTime() - a;

            if( a > 200000000 ) {

                log.warn("[接口层] 处理完毕 - 耗时较长，请优化接口！ 耗时: " + a + "nanoTime");

            } else log.info("[接口层] 处理完毕 耗时: " + a + "nanoTime");

            return;

        }

    }

    private boolean extracted(WrappedResponse wr, List<Object> params, Parameter parameter, TParam param) {

        if( !param.value() ) {

            wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Parameter error");

            log.info("[接口层] 请求参数错误 - " + ( !StrUtil.isBlankIfStr(param.field()) ? param.field() : parameter.getName()));

            return true;

        } else {

            params.add(null);

        }

        return false;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        TFrame.tframe.callEvent(new RequestAfterCompletion(request, response, handler, ex));

    }

}
