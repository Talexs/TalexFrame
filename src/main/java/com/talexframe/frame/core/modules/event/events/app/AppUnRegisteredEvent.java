package com.talexframe.frame.core.modules.event.events.app;

import com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.event.TalexEvent;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import lombok.Getter;

/**
 * {@link com.talexframe.frame.core.modules.event.events.app Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 01:08 Project: TalexFrame
 */
@Getter
public class AppUnRegisteredEvent extends TalexEvent {

    private WebPlugin plugin;
    private final TApp app;

    public AppUnRegisteredEvent(WebPlugin plugin, TApp app) {

        this.plugin = plugin;
        this.app = app;

    }

}
