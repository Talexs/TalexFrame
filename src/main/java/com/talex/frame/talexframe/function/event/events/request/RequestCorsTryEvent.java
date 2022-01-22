package com.talex.frame.talexframe.function.event.events.request;

import com.talex.frame.talexframe.function.event.Cancellable;
import com.talex.frame.talexframe.function.event.TalexEvent;
import com.talex.frame.talexframe.wrapper.WrappedResponse;
import lombok.Getter;

/**
 * 框架尝试同意一个 OPTIONS 请求时
 * <br /> {@link com.talex.frame.talexframe.function.event.events.request Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/22 17:19 <br /> Project: TalexFrame <br />
 */
public class RequestCorsTryEvent extends TalexEvent implements Cancellable {

    @Getter
    private final WrappedResponse response;

    public RequestCorsTryEvent(WrappedResponse response) {

        this.response = response;

    }

    private boolean cancel;

    @Override
    public boolean isCancelled() {

        return cancel;
    }

    @Override
    public void setCancelled(boolean var) {

        cancel = var;

    }

}
