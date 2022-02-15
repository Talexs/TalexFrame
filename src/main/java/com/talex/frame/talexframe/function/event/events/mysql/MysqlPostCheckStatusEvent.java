package com.talex.frame.talexframe.function.event.events.mysql;

import com.talex.frame.talexframe.function.event.TalexEvent;
import com.talex.frame.talexframe.function.mysql.MysqlManager;
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
