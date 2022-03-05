package com.talex.talexframe.frame.core.talex;

import com.talex.talexframe.frame.TalexFrameApplication;
import com.talex.talexframe.frame.core.function.command.CommandManager;
import com.talex.talexframe.frame.core.function.command.frame.HelpCmd;
import com.talex.talexframe.frame.core.function.command.frame.PluginCmd;
import com.talex.talexframe.frame.core.function.command.frame.StopCmd;
import com.talex.talexframe.frame.core.function.listener.FrameSelfListener;
import com.talex.talexframe.frame.core.modules.controller.TControllerManager;
import com.talex.talexframe.frame.core.modules.event.FrameListener;
import com.talex.talexframe.frame.core.modules.event.MethodManager;
import com.talex.talexframe.frame.core.modules.event.TalexEvent;
import com.talex.talexframe.frame.core.modules.event.events.frame.FrameStartedEvent;
import com.talex.talexframe.frame.core.modules.event.service.TalexEventBus;
import com.talex.talexframe.frame.core.modules.plugins.addon.FramePluginListener;
import com.talex.talexframe.frame.core.modules.plugins.core.PluginInfo;
import com.talex.talexframe.frame.core.modules.plugins.core.PluginManager;
import com.talex.talexframe.frame.core.modules.repository.TRepositoryManager;
import com.talex.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talex.talexframe.frame.core.pojo.enums.FrameStatus;
import com.talex.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import com.talex.talexframe.frame.service.MailServiceImpl;
import com.talex.talexframe.frame.service.RateLimiterManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <br /> {@link com.talex.frame.talexframe.function.talex Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/18 22:10 <br /> Project: TalexFrame <br />
 */
@Getter
@Slf4j
public class TFrame {

    public static final TFrame tframe = new TFrame();

    private final PluginInfo.PluginSupportVersion versionE = PluginInfo.PluginSupportVersion.SEVEN;
    private final String version = "7.0.0";

    @Setter
    private FrameStatus frameStatus = FrameStatus.STOPPED;

    @Getter
    private static final File mainFile = new File(System.getProperty("user.dir"));

    private boolean started = false;

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
     *
     * 启动整体架构
     *
     */
    public void started() {

        if( started ) return;

        getEventBus().registerListener( new FrameSelfListener() );

        callEvent(new FrameStartedEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

        started = true;
        setFrameStatus(FrameStatus.RUNNING);

        this.daoManager = new DAOManager();

        this.rateLimiterManager = new RateLimiterManager();

        CommandManager.initial();
        this.commandManager = CommandManager.INSTANCE;
        this.commandManager.setCommandExecutor("help", new HelpCmd());
        this.commandManager.setCommandExecutor("plugin", new PluginCmd());
        this.commandManager.setCommandExecutor("stop", new StopCmd());

        log.info("正在准备 储存器与控制器 - 请等待...");

        this.repositoryManager = TRepositoryManager.init();
        this.controllerManager = TControllerManager.init();

        log.info("开始加载插件...");

        this.pluginManager = new PluginManager(new File(mainFile.getAbsolutePath() + "/plugins"));

        this.pluginManager.loadAllPluginsInFolder();

        log.info("框架启动成功!");
        log.debug("DEBUG 模式已启动!");

    }

    public MailServiceImpl getMailService() {

        return MailServiceImpl.INSTANCE;

    }

    private RateLimiterManager rateLimiterManager;

    private CommandManager commandManager;
    private DAOManager daoManager;

    private final FrameSender frameSender;

    @Getter( AccessLevel.PACKAGE )
    private final TalexEventBus eventBus = TalexEventBus.getDefault();

    private PluginManager pluginManager;

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

    private TRepositoryManager repositoryManager;
    private TControllerManager controllerManager;

}
