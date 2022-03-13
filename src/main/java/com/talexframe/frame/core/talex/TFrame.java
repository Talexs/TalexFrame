package com.talexframe.frame.core.talex;

import com.talexframe.frame.TalexFrameApplication;
import com.talexframe.frame.core.function.command.CommandManager;
import com.talexframe.frame.core.function.command.frame.HelpCmd;
import com.talexframe.frame.core.function.command.frame.InfoCmd;
import com.talexframe.frame.core.function.command.frame.PluginCmd;
import com.talexframe.frame.core.function.command.frame.StopCmd;
import com.talexframe.frame.core.function.listener.FrameSelfListener;
import com.talexframe.frame.core.modules.controller.TControllerManager;
import com.talexframe.frame.core.modules.event.FrameListener;
import com.talexframe.frame.core.modules.event.MethodManager;
import com.talexframe.frame.core.modules.event.TalexEvent;
import com.talexframe.frame.core.modules.event.events.frame.FrameStartedEvent;
import com.talexframe.frame.core.modules.event.service.TalexEventBus;
import com.talexframe.frame.core.modules.plugins.addon.FramePluginListener;
import com.talexframe.frame.core.modules.plugins.core.PluginInfo;
import com.talexframe.frame.core.modules.plugins.core.PluginManager;
import com.talexframe.frame.core.modules.repository.TRepositoryManager;
import com.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talexframe.frame.core.pojo.enums.FrameStatus;
import com.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import com.talexframe.frame.core.service.MailServiceImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <br /> {@link com.talexframe.frame.core.talex Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/18 22:10 <br /> Project: TalexFrame <br />
 */
@Getter
@Slf4j
@SuppressWarnings( "unused" )
public class TFrame {

    public static final TFrame tframe = new TFrame();
    @Getter
    private static final File mainFile = new File(System.getProperty("user.dir"));
    private final PluginInfo.PluginSupportVersion versionE = PluginInfo.PluginSupportVersion.SEVEN;
    private final String version = "7.0.0";
    private final FrameSender frameSender;
    @Getter( AccessLevel.PACKAGE )
    private final TalexEventBus eventBus = TalexEventBus.getDefault();
    @Setter
    private FrameStatus frameStatus = FrameStatus.STOPPED;
    private boolean started = false;
    private CommandManager commandManager;
    private DAOManager daoManager;
    private PluginManager pluginManager;
    private TRepositoryManager repositoryManager;
    private TControllerManager controllerManager;
    private TFrame() {

        this.printBanner();

        this.frameSender = FrameSender.getDefault();

    }

    private void printBanner() {

        System.out.println("                                                                                 \n" +
                " /__  ___/                            //   / /                                   \n" +
                "   / /   ___     //  ___             //___   __      ___      _   __      ___    \n" +
                "  / /  //   ) ) // //___) ) \\\\ / /  / ___  //  ) ) //   ) ) // ) )  ) ) //___) ) \n" +
                " / /  //   / / // //         \\/ /  //     //      //   / / // / /  / / //        \n" +
                "/ /  ((___( ( // ((____      / /\\ //     //      ((___( ( // / /  / / ((____     \n" +
                "                                                                                 \n" +
                "    \n" +
                " _____ ___   ___  \n" +
                "|___  / _ \\ / _ \\ \n" +
                "   / / | | | | | |\n" +
                "  / /| |_| | |_| |\n" +
                " /_(_)\\___(_)___/ \n" +
                "                  \n                                                 ");

    }

    /**
     * 启动整体架构
     */
    public void started() {

        if ( started ) {
            return;
        }

        started = true;
        setFrameStatus(FrameStatus.RUNNING);

        log.info("Loading repository & controller ...");
        this.repositoryManager = TRepositoryManager.init();
        this.controllerManager = TControllerManager.init();

        getEventBus().registerListener(new FrameSelfListener());

        callEvent(new FrameStartedEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

        log.info("Loading dao-manager ...");
        this.daoManager = new DAOManager();

        if ( frameStatus == FrameStatus.RUNNING ) {

            log.info("Loading command-manager ...");
            CommandManager.initial();
            this.commandManager = CommandManager.INSTANCE;
            this.commandManager.setCommandExecutor("help", new HelpCmd());
            this.commandManager.setCommandExecutor("plugin", new PluginCmd());
            this.commandManager.setCommandExecutor("stop", new StopCmd());
            this.commandManager.setCommandExecutor("info", new InfoCmd());

        }

        if ( frameStatus == FrameStatus.RUNNING ) {

            log.info("Loading plugin-manager ...");

            this.pluginManager = new PluginManager(new File(mainFile.getAbsolutePath() + "/plugins"));

            this.pluginManager.loadAllPluginsInFolder();

            log.info("框架启动成功!");
            log.debug("DEBUG 模式已启动!");

        }

    }

    public void crash(Throwable e) {

        shutdown();

        frameStatus = FrameStatus.CRASHED;

        log.error("框架运行失败, 详情: ", e);

    }

    public synchronized void shutdown() {

        if ( frameStatus == FrameStatus.STOPPED ) {
            return;
        }

        this.setFrameStatus(FrameStatus.STOPPING);

        log.warn("正在关闭服务器...");

        if ( pluginManager != null ) {

            for ( String plugin : pluginManager.getPluginHashMap().keySet() ) {

                pluginManager.unloadPlugin(plugin);

            }

        }

        if ( daoManager != null ) {
            daoManager.shutdown();
        }

        if ( TalexFrameApplication.context != null ) {

            TalexFrameApplication.context.close();

        }

    }

    public MailServiceImpl getMailService() {

        return MailServiceImpl.INSTANCE;

    }

    public TFrame callEvent(TalexEvent event) {

        assert eventBus != null;
        eventBus.callEvent(event);

        return this;

    }

    public TFrame registerListener(FramePluginListener listener) {

        assert eventBus != null;
        eventBus.registerListener(listener);

        return this;

    }

    public TFrame unRegisterListener(FramePluginListener listener) {

        assert eventBus != null;
        eventBus.unRegisterListener(listener);

        return this;

    }

    public Map<FrameListener, List<MethodManager>> getListeners() {

        assert eventBus != null;
        return eventBus.getMapCaches();

    }

}