package com.talex.talexframe.frame.core.modules.event.events.mysql;

import com.talex.talexframe.frame.core.modules.event.IContinue;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.modules.mysql.MysqlManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:38 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
@AllArgsConstructor
public class MysqlConnectedEvent extends TalexEvent implements IContinue {

    private final MysqlManager mysqlManager;

    @Override
    public String getMatchKey() {

        return System.nanoTime() + " # MysqlConnected @" + this.hashCode();
    }

}
