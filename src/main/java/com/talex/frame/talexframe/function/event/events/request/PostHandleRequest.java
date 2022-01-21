package com.talex.frame.talexframe.function.event.events.request;

import com.talex.frame.talexframe.function.event.Cancellable;
import com.talex.frame.talexframe.function.event.TalexEvent;
import com.talex.frame.talexframe.wrapper.BodyCopyHttpServletRequestWrapper;
import lombok.Getter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在建立请求之后 (interceptor post handle request)
 * 如果取消则不会进入插件处理阶段
 * <br /> {@link com.talex.frame.talexframe.function.event.events.request Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 19:23 <br /> Project: TalexFrame <br />
 */
public class PostHandleRequest extends TalexEvent {

    @Getter
    private final BodyCopyHttpServletRequestWrapper request;

    @Getter
    private final HttpServletResponse response;

    @Getter
    private final Object handler;

    @Getter
    private final ModelAndView modelAndView;

    public PostHandleRequest(BodyCopyHttpServletRequestWrapper request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        this.request = request;
        this.response = response;
        this.handler = handler;
        this.modelAndView = modelAndView;

    }

}
