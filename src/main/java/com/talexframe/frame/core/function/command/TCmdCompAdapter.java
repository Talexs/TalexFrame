package com.talexframe.frame.core.function.command;

import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.addon.PluginScanner;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link com.talexframe.frame.core.modules.application Package }
 *
 * @author TalexDreamSoul 22/04/02 下午 08:44 Project: TalexFrame
 */
public class TCmdCompAdapter extends PluginCompAdapter<BaseCommand> {

    private final CommandManager cmdManager = CommandManager.INSTANCE;

    @SneakyThrows
    @Override
    public boolean injectWithInstance(PluginScanner scanner, WebPlugin webPlugin, BaseCommand instance) {

        return cmdManager.setCommandExecutor(instance.getLabel(), instance);

    }

    @Override
    public boolean logoutInstance(PluginScanner scanner, WebPlugin webPlugin, Class<? extends FrameCreator> clazz) {

        final AtomicBoolean success = new AtomicBoolean(false);

        cmdManager.getCommands().values().forEach(baseCommand -> {

            if( baseCommand.getClass().equals(clazz) ) {

                cmdManager.removeCommandExecutor(baseCommand.getLabel());

                success.set(true);

            }

        });

        return success.get();

    }

}
