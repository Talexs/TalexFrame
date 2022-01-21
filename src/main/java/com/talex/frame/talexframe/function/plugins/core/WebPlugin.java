package com.talex.frame.talexframe.function.plugins.core;

import com.talex.frame.talexframe.function.talex.FrameCreator;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.mapper.frame.FrameSender;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class WebPlugin extends FrameCreator implements IPlugin {

    protected TFrame tframe = TFrame.tframe;
    protected FrameSender sender = TFrame.tframe.getFrameSender();

    public WebPlugin(String pluginName, PluginInfo info) {

        super("PLUGIN", pluginName);

        this.name = pluginName;
        this.info = info;

    }

    private final String name;

    @Setter
    private PluginInfo info;

}
