package com.talexframe.frame.core.modules.repository;

import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul 22/04/02 下午 08:44 Project: TalexFrame
 */
public class TRepoCompAdapter extends PluginCompAdapter<TRepo> {

    private final TRepoManager repoManager = tframe.getRepoManager();

    @SneakyThrows
    @Override
    public boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, TRepo instance) {

        return repoManager.registerRepo( webPlugin, instance );

    }

    @Override
    public boolean logoutInstance(PluginScanner scanner, WebPlugin webPlugin, Class<? extends FrameCreator> clazz) {

        return repoManager.unRegisterRepo( webPlugin, repoManager.getRepoByClass(clazz));

    }

}
