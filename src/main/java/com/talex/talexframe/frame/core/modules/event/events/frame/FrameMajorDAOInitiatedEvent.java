package com.talex.talexframe.frame.core.modules.event.events.frame;

import com.talex.talexframe.frame.dao.MajorDAO;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 这里指框架的数据已连接 (MySQL)
 *
 */
@EqualsAndHashCode( callSuper = true )
@Data
@Deprecated
public class FrameMajorDAOInitiatedEvent extends TalexEvent {

    private final MajorDAO majorDAO;

    public FrameMajorDAOInitiatedEvent(MajorDAO majorDAO) {

        this.majorDAO = majorDAO;

    }

}
