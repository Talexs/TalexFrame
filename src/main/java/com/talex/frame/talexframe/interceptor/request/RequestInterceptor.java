package com.talex.frame.talexframe.interceptor.request;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.talex.frame.talexframe.function.controller.TController;
import com.talex.frame.talexframe.function.controller.TControllerManager;
import com.talex.frame.talexframe.function.event.events.request.PostHandleRequest;
import com.talex.frame.talexframe.function.event.events.request.PreHandleRequest;
import com.talex.frame.talexframe.function.event.events.request.RequestAfterCompletion;
import com.talex.frame.talexframe.function.event.events.request.RequestCorsTryEvent;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.annotations.TParam;
import com.talex.frame.talexframe.pojo.annotations.TRequest;
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

        if ( request.getMethod().equals("OPTIONS")) {

            RequestCorsTryEvent e = new RequestCorsTryEvent(wr);

            tframe.callEvent(e);

            if(e.isCancelled()) return false;

            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN");

            wr.returnDataByOK("SUCCESS");

            return false;

        }

        log.info("[接口层] 新的请求 " + request.getRequestURI() + " #来自: " + request.getSession().getId());

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

    private void processClassRequest(TController controller, WrappedResponse wr) throws InstantiationException, IllegalAccessException, InvocationTargetException {

        if( TFrame.tframe == null ) return;

        Class<?> clz = controller.getClass();

        for( Method method : clz.getMethods() ) {

            TRequest request = method.getAnnotation(TRequest.class);

            if( request == null ) continue;

            if( !UrlUtil.advancedUrlChecker(wr.getRequest().getRequestURI(), request.value())) continue;

            List<Object> params = new ArrayList<>();

            params.add(wr);

            if( !method.isAccessible() ) method.setAccessible(true);

            if( !request.parseJSON() ) { method.invoke(clz.newInstance(), params.toArray()); return; }

            String str = wr.getRequest().getBody();

            if( StrUtil.isBlankIfStr(str) ) {

                wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Data error");

                log.info("[接口层] 请求数据错误 - 无任何数据");

                return;

            }

            JSONObject json = JSONUtil.parseObj(str);

            for( Parameter parameter : method.getParameters() ) {

                TParam param = parameter.getAnnotation(TParam.class);

                if( param == null ) continue;

                try {

                    Object obj = param.field() != null ? json.get(param.field(), parameter.getType()) : json.get(parameter.getName(), parameter.getType());

                    params.add(obj);

                } catch ( ConvertException e ) {

                    if( !param.value() ) {

                        wr.returnDataByFailed(ResultData.ResultEnum.INFORMATION_ERROR, "Parameter error");

                        log.info("[接口层] 请求参数错误 - " + (param.field() != null ? param.field() : parameter.getName()) + " #" + e.getMessage());

                        return;

                    } else {

                        params.add(null);

                    }

                }

            }

            log.info("[接口层] 由 @" + clz + " 开始处理.");

            long a = System.nanoTime();

            method.invoke(clz.newInstance(), params.toArray());

            a = System.nanoTime() - a;

            if( a > 200000000 ) {

                log.warn("[接口层] 处理完毕 - 耗时较长，请优化接口！ 耗时: " + a + "nanoTime");

            } else log.info("[接口层] 处理完毕 耗时: " + a + "nanoTime");

            return;

        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        TFrame.tframe.callEvent(new RequestAfterCompletion(request, response, handler, ex));

    }

}
