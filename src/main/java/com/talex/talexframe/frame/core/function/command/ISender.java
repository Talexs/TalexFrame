package com.talex.talexframe.frame.core.function.command;

/**
 * Sender接口
 * <br /> {@link com.talex.frame.talexframe.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 10:40 <br /> Project: TalexFrame <br />
 */
public interface ISender {

    /**
     *
     * 发出消息
     *
     * @param message 要发出的消息
     *
     */
    void sendMessage(String... message);

}
