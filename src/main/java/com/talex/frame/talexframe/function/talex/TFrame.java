package com.talex.frame.talexframe.function.talex;

import com.talex.frame.talexframe.dao.MajorDAO;
import com.talex.frame.talexframe.function.command.CommandManager;
import com.talex.frame.talexframe.function.command.CommandReader;
import com.talex.frame.talexframe.function.command.frame.HelpCmd;
import com.talex.frame.talexframe.function.command.frame.PluginCmd;
import com.talex.frame.talexframe.function.controller.TControllerManager;
import com.talex.frame.talexframe.function.event.FrameListener;
import com.talex.frame.talexframe.function.event.TalexEvent;
import com.talex.frame.talexframe.function.event.events.frame.FrameMajorDAOInitiatedEvent;
import com.talex.frame.talexframe.function.event.service.TalexEventBus;
import com.talex.frame.talexframe.function.plugins.addon.FramePluginListener;
import com.talex.frame.talexframe.function.plugins.core.PluginManager;
import com.talex.frame.talexframe.function.repository.TRepositoryManager;
import com.talex.frame.talexframe.mapper.frame.FrameSender;
import com.talex.frame.talexframe.pojo.enums.FrameStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * <br /> {@link com.talex.frame.talexframe.function.talex Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/18 22:10 <br /> Project: TalexFrame <br />
 */
@Getter
@Slf4j
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

    private void printBanner() {

        System.out.println("                                                                                 \n" +
                " /__  ___/                            //   / /                                   \n" +
                "   / /   ___     //  ___             //___   __      ___      _   __      ___    \n" +
                "  / /  //   ) ) // //___) ) \\\\ / /  / ___  //  ) ) //   ) ) // ) )  ) ) //___) ) \n" +
                " / /  //   / / // //         \\/ /  //     //      //   / / // / /  / / //        \n" +
                "/ /  ((___( ( // ((____      / /\\ //     //      ((___( ( // / /  / / ((____     \n" +
                "                                                                                 \n" +
                "      ____      ___         ___                                                  \n" +
                "    //        //   ) )    //   ) )                                               \n" +
                "   //__      //   / /    //   / /                                                \n" +
                "  //   ) )  //   / /    //   / /                                                 \n" +
                " ((___/ / (|(___/ /   (|(___/ /                                                  ");

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

        this.printBanner();

        this.frameSender = FrameSender.getDefault();

        CommandManager.initial();

        this.commandManager = CommandManager.INSTANCE;
        this.commandManager.setCommandExecutor("help", new HelpCmd());
        this.commandManager.setCommandExecutor("plugin", new PluginCmd());

        this.repositoryManager = TRepositoryManager.init();
        this.controllerManager = TControllerManager.init();

        this.pluginManager = new PluginManager(new File(mainFile.getAbsolutePath() + "/plugins"));
        this.pluginManager.loadAllPluginsInFolder();


    }

    private CommandManager commandManager;

    private FrameSender frameSender;

    @Getter( AccessLevel.PACKAGE )
    private final TalexEventBus eventBus = TalexEventBus.getDefault();

    private PluginManager pluginManager;

    public TFrame callEvent(TalexEvent event) {

        assert eventBus != null;
        eventBus.callEvent(event);

        return this;

    }

    public TFrame registerEvent(FramePluginListener listener) {

        assert eventBus != null;
        eventBus.registerListener(listener);

        return this;

    }

    public TFrame unRegisterEvent(FramePluginListener listener) {

        assert eventBus != null;
        eventBus.unRegisterListener(listener);

        return this;

    }

    private MajorDAO majorDao;

    public TFrame setMajorDAO(MajorDAO majorDao) {

        if( this.majorDao == null ) {

            this.majorDao = majorDao;

            this.callEvent(new FrameMajorDAOInitiatedEvent(majorDao));

        }

        return this;

    }

    private TRepositoryManager repositoryManager;
    private TControllerManager controllerManager;

}
