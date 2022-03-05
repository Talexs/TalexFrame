package com.talex.talexframe.frame.core.modules.event.events.dao;

import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import lombok.Getter;

/**
 * <br /> {@link com.talex.talexframe.frame.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 23:52 <br /> Project: TalexFrame <br />
 */
public class DAOProcessorPostCheckStatusEvent extends TalexEvent {

    @Getter
    private final IDataProcessor processor;

    public DAOProcessorPostCheckStatusEvent(IDataProcessor processor) {

        this.processor = processor;

    }

}
