package com.talex.talexframe.frame.exception.lang;

import com.talex.talexframe.frame.exception.FrameException;

/**
 * <br /> {@link com.talex.talexframe.frame.exception.lang Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 11:26 <br /> Project: TalexFrame <br />
 */
public class IllegalParametersException extends FrameException {

    public IllegalParametersException() {

        super("参数不合法");

    }

    /**
     *
     * 通过给定数量创建一个细节异常
     *
     * @param amo 需求数量
     * @param provide 提供数量
     */
    public IllegalParametersException(int amo, int provide) {

        super("参数不合法，需要 " + amo + " 个参数，但只提供了 " + provide + " 个");

    }

}
