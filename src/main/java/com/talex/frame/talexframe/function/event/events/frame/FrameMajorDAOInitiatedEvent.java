package com.talex.frame.talexframe.function.event.events.frame;

import com.talex.frame.talexframe.dao.MajorDAO;
import com.talex.frame.talexframe.function.event.TalexEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 这里指框架的数据已连接 (MySQL)
 *
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class FrameMajorDAOInitiatedEvent extends TalexEvent {

    private final MajorDAO majorDAO;

    public FrameMajorDAOInitiatedEvent(MajorDAO majorDAO) {

        this.majorDAO = majorDAO;

    }

}
