package com.talex.frame.talexframe.function.event.events.request;

import com.talex.frame.talexframe.function.event.Cancellable;
import com.talex.frame.talexframe.function.event.TalexEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在建立请求之前 (interceptor pre handle request)
 * <br /> {@link com.talex.frame.talexframe.function.event.events.request Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 19:19 <br /> Project: TalexFrame <br />
 */
public class PreHandleRequest extends TalexEvent implements Cancellable {

    @Getter
    private final HttpServletRequest request;

    @Getter
    private final HttpServletResponse response;

    @Getter
    private final Object handler;

    public PreHandleRequest(HttpServletRequest request, HttpServletResponse response, Object handler) {

        this.request = request;
        this.response = response;
        this.handler = handler;

    }

    private boolean cancel;

    @Override
    public boolean isCancelled() {

        return cancel;
    }

    @Override
    public void setCancelled(boolean var) {

        this.cancel = var;

    }

}
