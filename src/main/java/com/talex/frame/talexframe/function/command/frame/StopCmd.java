package com.talex.frame.talexframe.function.command.frame;

import com.talex.frame.talexframe.TalexFrameApplication;
import com.talex.frame.talexframe.function.command.BaseCommand;
import com.talex.frame.talexframe.function.command.ISender;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.enums.FrameStatus;

/**
 * 帮助命令
 * <br /> {@link com.talex.frame.talexframe.function.command.frame Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 11:40 <br /> Project: TalexFrame <br />
 */
public class StopCmd extends BaseCommand {

    public StopCmd() {

        super(TFrame.tframe.getFrameSender(), "stop", new String[]{}, "关闭系统");
    }

    @Override
    public boolean executeCommand(ISender sender, String wholeCommand, String matchedLabel, String[] args) {

        tframe.getFrameSender().warnConsoleMessage("正在关闭服务器...");

        tframe.setFrameStatus(FrameStatus.STOPPING);

        for( String plugin : tframe.getPluginManager().getPluginHashMap().keySet() ) {

            tframe.getPluginManager().unloadPlugin(plugin);

        }

        TalexFrameApplication.context.close();

        return false;
    }

}
