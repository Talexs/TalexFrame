package com.talex.frame.talexframe.listener;

import com.talex.frame.talexframe.TalexFrameApplication;
import com.talex.frame.talexframe.function.event.events.frame.*;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.enums.FrameStatus;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.context.event.*;
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

        TFrame.initialFrame();

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
        log.info("框架启动成功!");

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
    @EventListener
    public void onStopped(ContextClosedEvent event) {

        TFrame.tframe.setFrameStatus(FrameStatus.STOPPED);

        TFrame.tframe.callEvent(new FramePostUnInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp));

    }

}
