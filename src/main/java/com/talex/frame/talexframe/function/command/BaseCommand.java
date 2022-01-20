package com.talex.frame.talexframe.function.command;

import com.talex.frame.talexframe.function.talex.FrameCreator;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.mapper.frame.FrameSender;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 10:56 <br /> Project: TalexFrame <br />
 */
@Getter
public abstract class BaseCommand implements ICommand {

    protected TFrame tframe = TFrame.tframe;
    protected FrameSender frameSender = tframe.getFrameSender();

    private final FrameCreator owner;
    public static final String DIVIDER = "---------------------------";

    private final String label;
    private final String[] alias;
    private final String description;

    /**
     *
     * 指令 init
     *
     * @param creator 指令拥有者
     * @param label 指令Label
     * @param alias 指令别称
     * @param description 指令描述
     */
    public BaseCommand(FrameCreator creator, String label, String[] alias, String description) {

        this.owner = creator;

        this.label = label;
        this.alias = alias;
        this.description = description;

    }

    /**
     *
     * 当扫描到这个类但是没有任何相关匹配的时候，执行操作 （@Override 重写）
     *
     * @param sender 命令发出者
     */
    @Deprecated
    public void onDisMatched(ISender sender) {

        sender.senderMessage("命令参数错误!");

    }

}
