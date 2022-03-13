package com.talexframe.frame.core.modules.event.events.dao;

/**
 * <br /> {@link com.talexframe.frame.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 23:52 <br /> Project: TalexFrame <br />
 */
public class DAOProcessorPostCheckStatusEvent<T> extends BaseDAOEvent<T> {

    public DAOProcessorPostCheckStatusEvent(T processor) {

        super(processor);

    }

}
