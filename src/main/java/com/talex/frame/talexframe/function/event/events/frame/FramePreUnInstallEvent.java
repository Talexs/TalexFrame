package com.talex.frame.talexframe.function.event.events.frame;

import com.talex.frame.talexframe.function.event.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode( callSuper = true )
@Data
public class FramePreUnInstallEvent extends TalexEvent {

    @Getter
    private long installTimeStamp;

    public FramePreUnInstallEvent(long installTimeStamp) {

        this.installTimeStamp = installTimeStamp;

    }

}
