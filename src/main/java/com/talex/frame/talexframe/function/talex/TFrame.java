package com.talex.frame.talexframe.function.talex;

import com.talex.frame.talexframe.function.command.CommandReader;
import com.talex.frame.talexframe.function.event.FrameListener;
import com.talex.frame.talexframe.function.event.service.TalexEventBus;
import com.talex.frame.talexframe.function.plugins.addon.FramePluginListener;
import com.talex.frame.talexframe.function.plugins.core.PluginManager;
import com.talex.frame.talexframe.pojo.enums.FrameStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

/**
 * <br /> {@link com.talex.frame.talexframe.function.talex Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/18 22:10 <br /> Project: TalexFrame <br />
 */
@Getter
public class TFrame {

    public static TFrame tframe;

    public static void initialFrame() {

        tframe = new TFrame();

    }

    @Setter
    private FrameStatus frameStatus = FrameStatus.STOPPED;

    private static final File mainFile = new File("");

    private boolean started = false;

    private TFrame() {



    }

    /**
     *
     * 启动整体架构
     *
     */
    public void started() {

        if( started ) return;

        started = true;
        TFrame.tframe.setFrameStatus(FrameStatus.RUNNING);

        this.pluginManager.loadAllPluginsInFolder();

        this.commandReader.run();


    }

    private CommandReader commandReader;

    {
        try {
            commandReader = new CommandReader();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private final TalexEventBus eventBus = TalexEventBus.getDefault();

    private final PluginManager pluginManager = new PluginManager(new File(mainFile, "/plugins"));

    public TFrame registerEvent(FramePluginListener listener) {

        eventBus.registerListener(listener);

        return this;

    }

    public TFrame unRegisterEvent(FramePluginListener listener) {

        eventBus.unRegisterListener(listener);

        return this;

    }

}
