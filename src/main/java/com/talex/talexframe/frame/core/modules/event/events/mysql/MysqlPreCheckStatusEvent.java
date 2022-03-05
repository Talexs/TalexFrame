package com.talex.talexframe.frame.core.modules.event.events.mysql;

import com.talex.talexframe.frame.core.modules.event.Cancellable;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.modules.mysql.MysqlManager;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 23:50 <br /> Project: TalexFrame <br />
 */
public class MysqlPreCheckStatusEvent extends TalexEvent implements Cancellable {

    @Getter
    private final MysqlManager mysql;

    public MysqlPreCheckStatusEvent(MysqlManager mysql) {

        this.mysql = mysql;

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
