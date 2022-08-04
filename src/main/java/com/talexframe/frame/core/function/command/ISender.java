package com.talexframe.frame.core.function.command;

/**
 * Sender接口 <br /> {@link com.talexframe.frame.function.command Package }
 *
 * @author TalexDreamSoul
 * 2022/1/20 10:40 <br /> Project: TalexFrame <br />
 */
public interface ISender {

    /**
     * 发出消息
     *
     * @param message 要发出的消息
     */
    void sendMessage(String... message);

}
