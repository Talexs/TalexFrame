package com.talex.frame.talexframe.listener;

import com.talex.frame.talexframe.TalexFrameApplication;
import com.talex.frame.talexframe.function.event.events.frame.*;
import com.talex.frame.talexframe.function.mysql.MysqlManager;
import com.talex.frame.talexframe.function.plugins.core.PluginManager;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.enums.FrameStatus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 框架内部主要的监听器
 * <br /> {@link com.talex.frame.talexframe.listener Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/19 12:07 <br /> Project: TalexFrame <br />
 */
@Component
@Slf4j
public class FrameMajorListener {

    /**
     *
     * SpringContext BeforeRefresh (Bean Definitions Loaded)
     *
     */
    @EventListener
    public void onStart(ApplicationPreparedEvent event) {

        TFrame.tframe.setFrameStatus(FrameStatus.PREPARING);

        TFrame.tframe.callEvent(new FramePreInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

    }

    /**
     *
     * SpringContext refreshed after (CommandLine Runners before)
     *
     */
    @EventListener
    public void onStart(ApplicationStartedEvent event) {

        TFrame.tframe.setFrameStatus(FrameStatus.STARTING);

        TFrame.tframe.callEvent(new FramePostInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

    }

    /**
     *
     * After any command-line runners
     *
     */
    @EventListener
    public void onStart(ApplicationReadyEvent event) {

        TFrame.tframe.callEvent(new FrameStartedEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

        TFrame.tframe.started();

    }

    /**
     *
     * SpringBoot Application Stopped
     *
     */
    @EventListener
    public void onStopped(ContextStoppedEvent event) {

        TFrame.tframe.setFrameStatus(FrameStatus.STOPPING);

        TFrame.tframe.callEvent(new FramePreUnInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

    }

    /**
     *
     * SpringBoot Application Closed
     *
     */
    @SneakyThrows
    @EventListener
    public void onStopped(ContextClosedEvent event) {

        TFrame.tframe.setFrameStatus(FrameStatus.STOPPED);
        log.warn("准备停止 - 正在卸载插件...");

        PluginManager pluginsManager = TFrame.tframe.getPluginManager();

        if(pluginsManager != null) {

            for( String str : pluginsManager.getPluginHashMap().keySet() ) {

                pluginsManager.unloadPlugin( str );

            }

        }


        MysqlManager mysqlManager = TFrame.tframe.getMysqlManager();
        if( mysqlManager != null )
            mysqlManager.shutdown();

        log.warn("框架已停止.");

        TFrame.tframe.callEvent(new FramePostUnInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

    }

}
