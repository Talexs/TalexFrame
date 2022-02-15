package com.talex.frame.talexframe.wrapper;

import com.talex.frame.talexframe.function.auto.data.AutoSaveData;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 16:14 <br /> Project: TalexFrame <br />
 */
@Getter
@AllArgsConstructor
public class WrappedData<T extends AutoSaveData> {

    private T value;

}
