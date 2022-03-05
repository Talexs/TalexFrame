package com.talex.talexframe.frame.core.modules.event.events.dao;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:44 <br /> Project: TalexFrame <br />
 */
@Getter
public class DAOProcessorPreShutdownEvent extends TalexEvent implements Cancellable {

    private final IDataProcessor processor;

    private boolean cancel;

    public DAOProcessorPreShutdownEvent(IDataProcessor processor) {

        this.processor = processor;

    }

    @Override
    public boolean isCancelled() {

        return cancel;
    }

    @Override
    public void setCancelled(boolean var) {

        cancel = var;

    }

}
