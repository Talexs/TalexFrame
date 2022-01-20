package com.talex.frame.talexframe.interceptor.request;

import com.talex.frame.talexframe.function.controller.TController;
import com.talex.frame.talexframe.function.event.events.request.PostHandleRequest;
import com.talex.frame.talexframe.function.event.events.request.PreHandleRequest;
import com.talex.frame.talexframe.function.event.events.request.RequestAfterCompletion;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.wrapper.WrappedResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

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

        PostHandleRequest event = new PostHandleRequest(request, response, handler, modelAndView);

        TFrame.tframe.callEvent(event);

        if( event.isCancelled() ) return;

        WrappedResponse wr = new WrappedResponse(request, response);

        for( TController controller : tframe.getControllerManager().getControllers().values() ) {

            processClassRequest(controller, wr);

        }

    }

    private void processClassRequest(TController controller, WrappedResponse wr) {

        Class<?> clz = controller.getClass();

        for( Method method : clz.getMethods() ) {



        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        TFrame.tframe.callEvent(new RequestAfterCompletion(request, response, handler, ex));

    }

}
