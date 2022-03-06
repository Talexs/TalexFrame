package com.talex.talexframe.frame.core.modules.event.events.dao;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.IContinue;
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
public class DAOProcessorConnectFailedEvent<T> extends BaseDAOEvent<T> implements Cancellable, IContinue {

    private final Exception e;

    public DAOProcessorConnectFailedEvent(T processor, Exception e) {

        super(processor);

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
