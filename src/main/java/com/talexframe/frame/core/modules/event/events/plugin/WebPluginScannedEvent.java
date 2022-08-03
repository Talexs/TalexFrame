package com.talexframe.frame.core.modules.event.events.plugin;

import com.talexframe.frame.core.modules.event.service.TalexEvent;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import lombok.Getter;

/**
 * {@link com.talexframe.frame.core.modules.event.events.plugin Package }
 *
 * @author TalexDreamSoul 22/08/01 下午 03:13 Project: TalexFrame
 */
public class WebPluginScannedEvent extends TalexEvent {

    @Getter
    private final WebPlugin plugin;

    public WebPluginScannedEvent(WebPlugin plugin) {

        this.plugin = plugin;

    }

}
