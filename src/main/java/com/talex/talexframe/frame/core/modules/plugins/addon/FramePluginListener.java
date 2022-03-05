package com.talex.talexframe.frame.core.modules.plugins.addon;

import com.talex.talexframe.frame.core.modules.event.FrameListener;
import com.talex.talexframe.frame.core.modules.plugins.core.WebPlugin;
import lombok.Getter;

/**
 * <br /> {@link com.talex.frame.talexframe.function.plugins.addon Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/18 22:18 <br /> Project: TalexFrame <br />
 */
@Getter
public class FramePluginListener extends FrameListener {

    private final WebPlugin webPlugin;

    public FramePluginListener(WebPlugin provider) {

        super(provider.getName());

        this.webPlugin = provider;

    }

}
