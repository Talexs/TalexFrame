package com.talex.frame.talexframe.function.command;

import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.mapper.frame.FrameSender;
import com.talex.frame.talexframe.pojo.enums.FrameStatus;
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

            // Scanner scanner = new Scanner(System.in);
            //
            // while( true ) {
            //
            //     if( TFrame.tframe.getFrameStatus() != FrameStatus.RUNNING ) {
            //
            //         log.info("[Command] 命令系统已结束!");
            //
            //         thread.interrupt();
            //
            //         break;
            //
            //     }
            //
            //
            //
            //     // String cmd = scanner.nextLine();
            //     //     if( !StrUtil.isEmptyIfStr(cmd) ) processCommand(cmd);
            //
            //    // String cmd = null;
            //    //
            //    // try {
            //    //
            //    //     cmd = reader.readLine(">");
            //    //
            //    //     if( !StrUtil.isEmptyIfStr(cmd) ) processCommand(cmd);
            //    //
            //    // } catch ( IOException e ) {
            //    //
            //    //     e.printStackTrace();
            //    //
            //    // }
            //
            //
            //
            // }

        });

        this.thread.start();

    }

    private CommandManager commandManager;

    class FrameConsole extends SimpleTerminalConsole {

        @Override
        protected boolean isRunning() {

            return TFrame.tframe.getFrameStatus() == FrameStatus.RUNNING;
        }

        @Override
        protected void runCommand(String command) {

            // if( commandManager == null ) {
            //
            //     commandManager = TFrame.tframe.getCommandManager();
            //
            //     if( commandManager == null ) {
            //
            //         log.warn("命令系统尚未初始化完成!");
            //
            //         return;
            //
            //     }
            //
            // }

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
