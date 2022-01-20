package com.talex.frame.talexframe.function.command;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * 命令管理器
 * <br /> {@link com.talex.frame.talexframe.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/19 23:26 <br /> Project: TalexFrame <br />
 */
@Getter
public class CommandManager {

    public static CommandManager INSTANCE;

    public static void initial() {

        if( INSTANCE != null ) return;

        INSTANCE = new CommandManager();

    }

    private final CommandReader commandReader;

    @SneakyThrows
    private CommandManager() {

        this.commandReader = new CommandReader();

        this.commandReader.run();

    }

}
