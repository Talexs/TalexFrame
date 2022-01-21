package com.talex.frame.talexframe.function.event.events.frame;

import com.talex.frame.talexframe.function.event.IContinue;
import com.talex.frame.talexframe.function.event.TalexEvent;
import com.talex.frame.talexframe.function.event.Cancellable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode( callSuper = true )
@Data
public class FrameFirstInstallEvent extends TalexEvent implements Cancellable, IContinue {

    @Getter
    private long installedTimeStamp;

    public FrameFirstInstallEvent(long installedTimeStamp) {

        this.installedTimeStamp = installedTimeStamp;

    }

    /**
     *
     * @Description: 在本事件中如果设定为真则不会输出默认的 config.yml 文件
     *
     */
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {

        return this.cancelled;

    }

    @Override
    public void setCancelled(boolean var) {

        this.cancelled = var;

    }

    @Override
    public String getMatchKey() {

        return System.nanoTime() + " # FrameFirstInstallEvent";
    }

}
