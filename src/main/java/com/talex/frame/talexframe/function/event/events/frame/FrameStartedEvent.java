package com.talex.frame.talexframe.function.event.events.frame;

import com.talex.frame.talexframe.function.event.Cancellable;
import com.talex.frame.talexframe.function.event.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 *
 * 这里指框架已完全启动
 *
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class FrameStartedEvent extends TalexEvent implements Cancellable {

    /**
     *
     * 框架启动时间 (nanoTime)
     *
     */
    @Getter
    private long installedTimeStamp;

    public FrameStartedEvent(long installedTimeStamp) {

        this.installedTimeStamp = installedTimeStamp;

    }


    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {

        return this.cancelled;

    }

    @Override
    public void setCancelled(boolean var) {

        this.cancelled = var;

    }

}
