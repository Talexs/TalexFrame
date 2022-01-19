package com.talex.frame.talexframe.function.event;

import com.talex.frame.talexframe.function.event.service.TalexEventBus;

/**
 *
 * EventBus 抽象接口逻辑
 *
 */
public interface IEventBus {

    /**
     *
     * 注册一个事件
     *
     * @param listener 事件
     * @return 支持链式注册
     */
    TalexEventBus registerListener(FrameListener listener);

    /**
     *
     * 注销一个事件
     *
     * @param listener 事件
     * @return 支持链式注销
     */
    TalexEventBus unRegisterListener(FrameListener listener);

    /**
     *
     * 内部方法 - 调用事件
     *
     * @param event 事件
     * @return 支持链式调用(USELESS)
     */
    TalexEventBus callEvent(TalexEvent event);

}
