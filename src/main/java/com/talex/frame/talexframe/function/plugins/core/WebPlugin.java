package com.talex.frame.talexframe.function.plugins.core;

import com.talex.frame.talexframe.function.talex.FrameCreator;
import lombok.Getter;

@Getter
public abstract class WebPlugin extends FrameCreator implements IPlugin {

    public WebPlugin(String pluginName, PluginInfo info) {

        super("PLUGIN", pluginName);

        this.name = pluginName;
        this.info = info;

    }

    private final String name;
    private final PluginInfo info;

}
