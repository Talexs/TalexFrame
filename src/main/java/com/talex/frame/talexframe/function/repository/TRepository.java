package com.talex.frame.talexframe.function.repository;

import com.talex.frame.talexframe.function.talex.FrameCreator;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:02 <br /> Project: TalexFrame <br />
 */
@Getter
public class TRepository extends FrameCreator {

    /**
     *
     * provider 请一定填写 table name
     *
     * @param provider TableName
     */
    public TRepository(String provider) {

        super("TREPOSITORY", provider);
    }

}
