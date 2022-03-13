package com.talexframe.frame.core.function.listener;

import com.talexframe.frame.core.modules.event.FrameListener;
import com.talexframe.frame.core.modules.event.TalexSubscribe;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorConnectedEvent;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorPreShutdownEvent;
import com.talexframe.frame.core.modules.repository.TAutoRepository;
import com.talexframe.frame.core.modules.repository.TRepository;
import com.talexframe.frame.core.pojo.dao.factory.mysql.Mysql;
import com.talexframe.frame.core.talex.TFrame;

/**
 * <br /> {@link com.talexframe.frame.listener Package }
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
    public void onMysqlConnected(DAOProcessorConnectedEvent<Mysql> event) {

        for ( TRepository repository : tframe.getRepositoryManager().getRepositories().values() ) {

            if ( repository instanceof TAutoRepository ) {

                ( (TAutoRepository<?>) repository ).onInstall();

            }

        }

    }

    @TalexSubscribe
    public void onMysqlPreShutdown(DAOProcessorPreShutdownEvent<Mysql> event) {

        for ( TRepository repository : tframe.getRepositoryManager().getRepositories().values() ) {

            if ( repository instanceof TAutoRepository ) {

                ( (TAutoRepository<?>) repository ).onUninstall();

            }

        }

    }

}
