package com.talex.talexframe.frame.core.modules.event.events.dao;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.IContinue;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * <br /> {@link com.talex.talexframe.frame.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:35 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
public class DAOProcessorConnectFailedEvent extends TalexEvent implements Cancellable, IContinue {

    private final IDataProcessor processor;
    private final Exception e;

    public DAOProcessorConnectFailedEvent(IDataProcessor processor, Exception e) {

        this.processor = processor;
        this.e = e;

    }

    @Override
    public String getMatchKey() {

        return System.nanoTime() + " # MysqlConnectFailed @" + this.hashCode();
    }

    @Getter( AccessLevel.PRIVATE )
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
