package com.talex.frame.talexframe.function.command;

import com.talex.frame.talexframe.function.talex.FrameCreator;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.command Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 10:56 <br /> Project: TalexFrame <br />
 */
@Getter
public abstract class BaseCommand implements ICommand {

    private final FrameCreator owner;

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

}
