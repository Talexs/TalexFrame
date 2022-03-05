package com.talex.talexframe.frame.core.modules.event.events.dao;

import com.talex.talexframe.frame.core.modules.event.IContinue;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * <br /> {@link com.talex.talexframe.frame.function.event.events.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:38 <br /> Project: TalexFrame <br />
 */
@Getter
@Setter
@AllArgsConstructor
public class DAOProcessorConnectedEvent extends TalexEvent implements IContinue {

    private final IDataProcessor processor;

    @Override
    public String getMatchKey() {

        return System.nanoTime() + " # DAOProcessorConnectedEvent @" + this.hashCode();
    }

}
