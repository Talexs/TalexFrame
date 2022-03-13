package com.talexframe.frame.core.modules.event.events.frame;

import com.talexframe.frame.core.modules.event.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author TalexDreamSoul
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class FramePostUnInstallEvent extends TalexEvent {

    @Getter
    private long installedTimeStamp;

    public FramePostUnInstallEvent(long installedTimeStamp) {

        this.installedTimeStamp = installedTimeStamp;

    }

}
