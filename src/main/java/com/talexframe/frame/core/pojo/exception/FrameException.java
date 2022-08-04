package com.talexframe.frame.core.pojo.exception;

import lombok.NoArgsConstructor;

/**
 * 框架基本异常 <br /> {@link com.talexframe.frame.exception Package }
 *
 * @author TalexDreamSoul
 * 2022/1/16 10:52 <br /> Project: TalexFrame <br />
 */
@NoArgsConstructor
public class FrameException extends Exception {

    public FrameException(String message) {

        super(message);
    }

    public FrameException(String message, Throwable cause) {

        super(message, cause);
    }

    public FrameException(Throwable cause) {

        super(cause);
    }

}
