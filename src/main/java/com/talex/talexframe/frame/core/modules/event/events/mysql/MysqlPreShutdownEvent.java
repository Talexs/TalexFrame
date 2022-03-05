package com.talex.talexframe.frame.core.modules.event.events.mysql;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.modules.mysql.MysqlManager;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:44 <br /> Project: TalexFrame <br />
 */
@Getter
public class MysqlPreShutdownEvent extends TalexEvent implements Cancellable {

    private final MysqlManager mysqlManager;

    private boolean cancel;

    public MysqlPreShutdownEvent(MysqlManager manager) {

        this.mysqlManager = manager;

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
