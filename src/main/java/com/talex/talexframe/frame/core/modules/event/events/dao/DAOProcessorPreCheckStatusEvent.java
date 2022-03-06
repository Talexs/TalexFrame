package com.talex.talexframe.frame.core.modules.event.events.dao;

import com.talex.talexframe.frame.core.modules.event.Cancellable;

/**
 * <br /> {@link com.talex.talexframe.frame.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 23:50 <br /> Project: TalexFrame <br />
 */
public class DAOProcessorPreCheckStatusEvent<T> extends BaseDAOEvent<T> implements Cancellable {

    public DAOProcessorPreCheckStatusEvent(T processor) {

        super(processor);

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
