package com.talex.talexframe.frame.core.modules.event.events.mysql;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.IContinue;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.modules.mysql.MysqlManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:35 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
public class MysqlConnectFailedEvent extends TalexEvent implements Cancellable, IContinue {

    private final MysqlManager mysqlManager;
    private final Exception e;

    public MysqlConnectFailedEvent(MysqlManager mysqlManager, Exception e) {

        this.mysqlManager = mysqlManager;
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
