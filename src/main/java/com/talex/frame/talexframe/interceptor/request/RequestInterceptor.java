package com.talex.frame.talexframe.interceptor.request;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.talex.frame.talexframe.function.controller.TController;
import com.talex.frame.talexframe.function.event.events.request.PostHandleRequest;
import com.talex.frame.talexframe.function.event.events.request.PreHandleRequest;
import com.talex.frame.talexframe.function.event.events.request.RequestAfterCompletion;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.annotations.TParam;
import com.talex.frame.talexframe.pojo.annotations.TRequest;
import com.talex.frame.talexframe.wrapper.BodyCopyHttpServletRequestWrapper;
import com.talex.frame.talexframe.wrapper.ResultData;
import com.talex.frame.talexframe.wrapper.WrappedResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
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
@Component
@Order
public final class RequestInterceptor implements HandlerInterceptor {

    private final TFrame tframe = TFrame.tframe;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        PreHandleRequest event = new PreHandleRequest(request, response, handler);

        TFrame.tframe.callEvent(event);

        return !event.isCancelled();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        BodyCopyHttpServletRequestWrapper copiedRequest = new BodyCopyHttpServletRequestWrapper(request);

        PostHandleRequest event = new PostHandleRequest(copiedRequest, response, handler, modelAndView);

        TFrame.tframe.callEvent(event);

        if( event.isCancelled() ) return;

        tframe.getFrameSender().sendConsoleMessage("[接口层] 新的请求 " + request.getRequestURI() + " #来自: " + request.getRemoteUser());

        WrappedResponse wr = new WrappedResponse(copiedRequest, response);

        for( TController controller : tframe.getControllerManager().getControllers().values() ) {

            processClassRequest(controller, wr);

        }

    }

    private void processClassRequest(TController controller, WrappedResponse wr) throws InstantiationException, IllegalAccessException, InvocationTargetException {

        Class<?> clz = controller.getClass();

        for( Method method : clz.getMethods() ) {

            TRequest request = method.getAnnotation(TRequest.class);

            if( request == null ) continue;

            List<Object> params = new ArrayList<>();

            params.add(wr);

            if( !method.isAccessible() ) method.setAccessible(true);

            if( !request.parseJSON() ) { method.invoke(clz.newInstance(), params.toArray()); }

            JSONObject json = JSONUtil.parseObj(wr.getRequest().getBody());

            for( Parameter parameter : method.getParameters() ) {

                TParam param = parameter.getAnnotation(TParam.class);

                if( param == null ) continue;

                try {

                    Object obj = param.field() != null ? json.get(param.field(), parameter.getType()) : json.get(parameter.getName(), parameter.getType());

                    params.add(obj);

                } catch ( ConvertException e ) {

                    if( !param.value() ) {

                        wr.returnDataByFailed(ResultData.ResultEnum.RC203, "Parameter error");

                        tframe.getFrameSender().sendConsoleMessage("[接口层] 请求参数错误 - " + (param.field() != null ? param.field() : parameter.getName()) + " #" + e.getMessage());

                        return;

                    } else {

                        params.add(null);

                    }

                }

            }

            tframe.getFrameSender().sendConsoleMessage("[接口层] 由 @" + clz + " 开始处理.");

            long a = System.nanoTime();

            method.invoke(clz.newInstance(), params.toArray());

            a = System.nanoTime() - a;

            if( a > 200000000 ) {

                tframe.getFrameSender().warnConsoleMessage("[接口层] 处理完毕 - 耗时较长，请优化接口！ 耗时: " + a + "nanoTime");

            } else tframe.getFrameSender().sendMessage("[接口层] 处理完毕 耗时: " + a + "nanoTime");

            return;

        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        TFrame.tframe.callEvent(new RequestAfterCompletion(request, response, handler, ex));

    }

}
