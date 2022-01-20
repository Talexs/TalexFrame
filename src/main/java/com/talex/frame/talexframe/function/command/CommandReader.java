package com.talex.frame.talexframe.function.command;

import cn.hutool.core.util.StrUtil;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.enums.FrameStatus;
import jline.console.ConsoleReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

/**
 * <br /> {@link com.talex.frame.talexframe.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/19 20:43 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class CommandReader {

    private final ConsoleReader reader = new ConsoleReader();
    private Thread thread;

    public CommandReader() throws IOException {



    }

    public void run() {

        this.thread = new Thread(() -> {

            thread.setName("Command");

            System.setProperty("jline.internal.Log.debug", "true");
            log.info("[Command] 输入 help 来查看命令列表!");

            Scanner scanner = new Scanner(System.in);

            while( true ) {

                if( TFrame.tframe.getFrameStatus() != FrameStatus.RUNNING ) {

                    log.info("[Command] 命令系统已结束!");

                    thread.interrupt();

                    break;

                }



                String cmd = scanner.nextLine();
                    if( !StrUtil.isEmptyIfStr(cmd) ) processCommand(cmd);

//                String cmd = null;
//                try {
//                    cmd = reader.readLine(">");
//                    if( !StrUtil.isEmptyIfStr(cmd) ) processCommand(cmd);
//                } catch ( IOException e ) {
//                    e.printStackTrace();
//                }



            }

        });

        this.thread.start();

    }
    
    private void processCommand(String cmd) {

        log.info("[Command] 未知的命令: " + cmd);

    }

}
