package com.talex.talexframe.frame.core.modules.event.events.dao;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.modules.mysql.MysqlManager;
import com.talex.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 23:50 <br /> Project: TalexFrame <br />
 */
public class DAOProcessorPreCheckStatusEvent extends TalexEvent implements Cancellable {

    @Getter
    private final IDataProcessor processor;

    public DAOProcessorPreCheckStatusEvent(IDataProcessor processor) {

        this.processor = processor;

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
