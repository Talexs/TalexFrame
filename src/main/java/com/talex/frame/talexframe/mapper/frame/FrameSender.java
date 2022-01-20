package com.talex.frame.talexframe.mapper.frame;

import com.talex.frame.talexframe.function.command.IConsoleSender;
import com.talex.frame.talexframe.function.talex.FrameCreator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

/**
 * 框架发出者
 * <br /> {@link com.talex.frame.talexframe.mapper.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 10:39 <br /> Project: TalexFrame <br />
 */
@Slf4j
public final class FrameSender extends FrameCreator implements IConsoleSender {

    private static FrameSender INSTANCE;

    public static FrameSender getDefault() {

        if (INSTANCE == null) {

            INSTANCE = new FrameSender();

            return INSTANCE;

        }

        return null;

    }

    private FrameSender() {

        super("SENDER", "FRAME");
    }

    @Override
    public void sendConsoleMessage(String... message) {

        for( String msg : message ) {

            log.info(msg);

        }

    }

    @Override
    public void warnConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.warn(msg);

        }
    }

    @Override
    public void errorConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.error(msg);

        }

    }

    @Override
    public void errorConsoleMessage(String message, Throwable throwable) {

        log.error(message, throwable);

    }

    @Override
    public void debugConsoleMessage(String... message) {

        for ( String msg : message ) {

            log.debug(msg);

        }

    }

    @Override
    public void debugConsoleMessage(String message, Object object) {

        log.debug(message, object);

    }

    @Override
    public void sendMessage(String... message) {

        this.sendConsoleMessage(message);

    }

}
