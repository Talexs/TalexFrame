package com.talex.frame.talexframe.function.event;

/**
 *
 * 标志者一个事件可以被取消
 *
 */
public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean var);

}
