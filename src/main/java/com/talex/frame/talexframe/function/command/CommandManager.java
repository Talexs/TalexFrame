package com.talex.frame.talexframe.function.command;

import com.talex.frame.talexframe.function.talex.FrameCreator;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.function.Predicate;

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

    private final Map<String, BaseCommand> commands = new HashMap<>(16);

    @SneakyThrows
    private CommandManager() {

        this.commandReader = new CommandReader();

        this.commandReader.run();

    }

    /**
     *
     * 处理命令 - 内部接口请勿调用 （可以用来发配假装指令）
     * dispatch
     *
     * @param wholeCmd
     */
    public void processCommand(ISender sender, String wholeCmd) {

        List<String> list = Arrays.asList(wholeCmd.split(" "));

        String label = list.get(0);
        list.remove(0);

        boolean match = false;

        for( Map.Entry<String, BaseCommand> entry : this.commands.entrySet() ) {

            match = true;

            String cmdLabel = entry.getKey();
            BaseCommand cmd = entry.getValue();

            if( cmdLabel.equalsIgnoreCase(label) || Arrays.stream(cmd.getAlias()).anyMatch(s -> s.equalsIgnoreCase(label)) ) {

                if( cmd.executeCommand(sender, wholeCmd, label, list.toArray(new String[0])) ) break;

            }

        }

        if( !match ) {

            sender.senderMessage("未知的命令: " + wholeCmd);

        }

    }

    /**
     *
     * 设置指令的执行器
     *
     * @param label 指令Label (标识符）
     * @param command 执行器
     *
     * @return 返回是否成功 返回假一般代表已有此命令 （可以通过 namespace 解决）
     */
    public boolean setCommandExecutor(String label, BaseCommand command) {

        if( this.commands.containsKey(label) ) return false;

        this.commands.put(label, command);

        return true;

    }

}
