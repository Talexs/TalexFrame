package com.talex.talexframe.frame.core.function.command;

import com.talex.talexframe.frame.core.talex.TFrame;
import com.talex.talexframe.frame.core.pojo.mapper.frame.FrameSender;
import com.talex.talexframe.frame.core.pojo.enums.FrameStatus;
import lombok.extern.slf4j.Slf4j;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

/**
 * <br /> {@link com.talex.frame.talexframe.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/19 20:43 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class CommandReader {

    // private final ConsoleReader reader = new ConsoleReader();
    private Thread thread;

    public CommandReader(CommandManager manager) {

        this.commandManager = manager;

    }

    public void run() {

        this.thread = new Thread(() -> {

            thread.setName("Command");

            System.setProperty("jline.internal.Log.debug", "true");
            log.info("[Command] 输入 help 来查看命令列表!");

            new FrameConsole().start();

        });

        this.thread.start();

    }

    private final CommandManager commandManager;

    class FrameConsole extends SimpleTerminalConsole {

        @Override
        protected boolean isRunning() {

            return TFrame.tframe.getFrameStatus() == FrameStatus.RUNNING;
        }

        @Override
        protected void runCommand(String command) {

            commandManager.processCommand(FrameSender.getDefault(), command);

        }

        @Override
        protected LineReader buildReader(LineReaderBuilder builder) {

            return super.buildReader(builder.appName("TalexFrame"));
        }

        @Override
        protected void shutdown() {

            TFrame.tframe.getFrameSender().errorConsoleMessage("正在关闭...");

        }

    }

}
