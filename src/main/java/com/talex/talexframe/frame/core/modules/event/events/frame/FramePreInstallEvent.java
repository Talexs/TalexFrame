package com.talex.talexframe.frame.core.modules.event.events.frame;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 *
 * 这里指 SpringBoot 准备好 框架未开始准备
 *
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class FramePreInstallEvent extends TalexEvent implements Cancellable {

    /**
     *
     * 框架启动时间 (nanoTime)
     *
     */
    @Getter
    private long installTimeStamp;

    public FramePreInstallEvent(long installTimeStamp) {

        this.installTimeStamp = installTimeStamp;

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
