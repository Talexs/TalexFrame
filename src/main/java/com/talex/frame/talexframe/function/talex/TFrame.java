package com.talex.frame.talexframe.function.talex;

import com.talex.frame.talexframe.TalexFrameApplication;
import com.talex.frame.talexframe.config.MysqlConfig;
import com.talex.frame.talexframe.function.command.CommandManager;
import com.talex.frame.talexframe.function.command.frame.HelpCmd;
import com.talex.frame.talexframe.function.command.frame.PluginCmd;
import com.talex.frame.talexframe.function.command.frame.StopCmd;
import com.talex.frame.talexframe.function.controller.TControllerManager;
import com.talex.frame.talexframe.function.event.TalexEvent;
import com.talex.frame.talexframe.function.event.events.frame.FrameStartedEvent;
import com.talex.frame.talexframe.function.event.service.TalexEventBus;
import com.talex.frame.talexframe.function.mysql.MysqlManager;
import com.talex.frame.talexframe.function.plugins.addon.FramePluginListener;
import com.talex.frame.talexframe.function.plugins.core.PluginInfo;
import com.talex.frame.talexframe.function.plugins.core.PluginManager;
import com.talex.frame.talexframe.function.repository.TRepositoryManager;
import com.talex.frame.talexframe.listener.FrameSelfListener;
import com.talex.frame.talexframe.mapper.frame.FrameSender;
import com.talex.frame.talexframe.pojo.enums.FrameStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * <br /> {@link com.talex.frame.talexframe.function.talex Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/18 22:10 <br /> Project: TalexFrame <br />
 */
@Getter
@Slf4j
public class TFrame {

    public static TFrame tframe = new TFrame();

    private final PluginInfo.PluginSupportVersion versionE = PluginInfo.PluginSupportVersion.SIX_NORMAL;
    private final String version = "6.1.0";

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

        callEvent(new FrameStartedEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

        started = true;
        setFrameStatus(FrameStatus.RUNNING);

        this.printBanner();

        this.frameSender = FrameSender.getDefault();

        this.mysqlManager = new MysqlManager(this, MysqlConfig.getInfo());

        CommandManager.initial();

        this.commandManager = CommandManager.INSTANCE;
        this.commandManager.setCommandExecutor("help", new HelpCmd());
        this.commandManager.setCommandExecutor("plugin", new PluginCmd());
        this.commandManager.setCommandExecutor("stop", new StopCmd());

        log.info("正在准备 储存器与控制器 - 请等待...");

        this.repositoryManager = TRepositoryManager.init();
        this.controllerManager = TControllerManager.init();

        getEventBus().registerListener( new FrameSelfListener() );

        log.info("开始加载插件...");

        this.pluginManager = new PluginManager(new File(mainFile.getAbsolutePath() + "/plugins"));

        this.pluginManager.loadAllPluginsInFolder();

        log.info("框架启动成功!");

    }

    private CommandManager commandManager;
    private MysqlManager mysqlManager;

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

    private TRepositoryManager repositoryManager;
    private TControllerManager controllerManager;

}
