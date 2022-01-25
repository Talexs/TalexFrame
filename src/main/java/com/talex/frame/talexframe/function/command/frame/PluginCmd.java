package com.talex.frame.talexframe.function.command.frame;

import com.talex.frame.talexframe.function.plugins.core.PluginInfo;
import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import com.talex.frame.talexframe.pojo.annotations.TalexCommand;
import com.talex.frame.talexframe.function.command.BaseCommand;
import com.talex.frame.talexframe.function.command.ISender;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.mapper.frame.FrameSender;
import com.talex.frame.talexframe.wrapper.WrappedSender;

/**
 * 帮助命令
 * <br /> {@link com.talex.frame.talexframe.function.command.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 11:40 <br /> Project: TalexFrame <br />
 */
public class PluginCmd extends BaseCommand {

    public PluginCmd() {

        super(TFrame.tframe.getFrameSender(), "plugin", new String[]{"plugins","plugman"}, "插件命令");
    }

    @TalexCommand
    public void onDefault(WrappedSender sender) {

        sender.sendMessage(DIVIDER);
        sender.sendMessage("/%label% #Plugin 帮助");
        sender.sendMessage("/%label% list #Plugin 插件列表");
        sender.sendMessage("/%label% load <pluginName> #加载一个插件");
        sender.sendMessage("/%label% unload <pluginName> #卸载一个插件");
        sender.sendMessage("/%label% reload <pluginName> #重载一个插件");
        sender.sendMessage("%divider%");

    }

    @TalexCommand( "list" )
    public void onListPlugin(WrappedSender sender) {

        sender.sendMessage("%divider%");

        for( WebPlugin plugin : tframe.getPluginManager().getPluginHashMap().values() ) {

            PluginInfo info = plugin.getInfo();

            sender.sendMessage(" " + plugin.getName() + " (" + info.getPluginVersion() + " @" + info.getSupportVersion() + ")");
            sender.sendMessage("       -- by " + info.getPluginAuthor());
            sender.sendMessage("       ");
            sender.sendMessage("   " + info.getDescription());
            sender.sendMessage("");
            sender.sendMessage("       @" + info.getWebsite());
            sender.sendMessage("");

        }

        sender.sendMessage("%divider%");

    }

    @TalexCommand( "load" )
    public void onLoadPlugin(ISender sender, String loadPlugin) {

        tframe.getPluginManager().loadPlugin(loadPlugin);

    }

    @TalexCommand( "unload" )
    public void onUnloadPlugin(ISender sender, String loadPlugin) {

        sender.sendMessage("[插件] 正在卸载插件 " + loadPlugin);

        if( tframe.getPluginManager().unloadPlugin(loadPlugin) ) {

            sender.sendMessage("[插件] " + loadPlugin + " 已卸载!");

        } else {

            if( sender instanceof FrameSender ) {

                ((FrameSender)sender).errorConsoleMessage("[插件] " + loadPlugin + " 卸载失败!");

            } else sender.sendMessage("[插件] " + loadPlugin + " 卸载失败!");

        }

    }

    @TalexCommand( "reload" )
    public void onReloadPlugin(ISender sender, String loadPlugin) {

        sender.sendMessage("[插件] 正在重载插件 " + loadPlugin);

        if( !tframe.getPluginManager().unloadPlugin(loadPlugin) ) {

            if( sender instanceof FrameSender ) {

                ((FrameSender)sender).errorConsoleMessage("[插件] " + loadPlugin + " 重载失败! (无法卸载)");

            } else sender.sendMessage("[插件] " + loadPlugin + " 重载失败! (无法卸载)");

            return;

        }

        tframe.getPluginManager().loadPlugin(loadPlugin);

    }

    @Override
    public boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args) {

        return false;
    }

}
