package com.talex.talexframe.frame.core.modules.event.events.mysql;

import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.modules.mysql.MysqlManager;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 23:52 <br /> Project: TalexFrame <br />
 */
public class MysqlPostCheckStatusEvent extends TalexEvent {

    @Getter
    private final MysqlManager mysql;

    public MysqlPostCheckStatusEvent(MysqlManager mysql) {

        this.mysql = mysql;

    }

}
