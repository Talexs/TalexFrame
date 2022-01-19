package com.talex.frame.talexframe.function.plugins.core;

public interface IPlugin {

    /** 当插件被卸载时调用 **/
    void onDisable();

    /** 当插件被启用时调用 **/
    void onEnable();

}
