package com.talex.talexframe.frame.core.function.listener;

import com.talex.talexframe.frame.core.modules.event.FrameListener;
import com.talex.talexframe.frame.core.modules.event.TalexSubscribe;
import com.talex.talexframe.frame.core.modules.event.events.mysql.MysqlConnectedEvent;
import com.talex.talexframe.frame.core.modules.event.events.mysql.MysqlPreShutdownEvent;
import com.talex.talexframe.frame.core.modules.repository.TAutoRepository;
import com.talex.talexframe.frame.core.modules.repository.TRepository;
import com.talex.talexframe.frame.core.talex.TFrame;

/**
 * <br /> {@link com.talex.frame.talexframe.listener Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 17:10 <br /> Project: TalexFrame <br />
 */
public class FrameSelfListener extends FrameListener {

    final TFrame tframe = TFrame.tframe;

    public FrameSelfListener() {

        super("FRAME#SELF");

    }

    @TalexSubscribe
    public void onMysqlConnected(MysqlConnectedEvent event) {

        for( TRepository repository : tframe.getRepositoryManager().getRepositories().values() ) {

            if( repository instanceof TAutoRepository ) {

                ( (TAutoRepository<?>) repository ).onInstall();

            }

        }

    }

    @TalexSubscribe
    public void onMysqlPreShutdown(MysqlPreShutdownEvent event) {

        for( TRepository repository : tframe.getRepositoryManager().getRepositories().values() ) {

            if( repository instanceof TAutoRepository ) {

                ( (TAutoRepository<?>) repository ).onUninstall();

            }

        }

    }

}
