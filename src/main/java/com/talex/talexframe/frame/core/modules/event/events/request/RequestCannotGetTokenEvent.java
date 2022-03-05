package com.talex.talexframe.frame.core.modules.event.events.request;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import lombok.Getter;

/**
 * 当一个请求无法获得令牌而被取消时
 * <br /> {@link com.talex.talexframe.frame.function.event.events.request Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/23 23:59 <br /> Project: TalexFrame <br />
 */
@Getter
public class RequestCannotGetTokenEvent extends TalexEvent implements Cancellable {

    public enum LimiterType {

        GLOBAL(), CLASS(), METHOD()

    }

    private final WrappedResponse wr;
    private final LimiterType limiterType;

    public RequestCannotGetTokenEvent(WrappedResponse wr, LimiterType limiterType) {

        this.wr = wr;
        this.limiterType = limiterType;

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
