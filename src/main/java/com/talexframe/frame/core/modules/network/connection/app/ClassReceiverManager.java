package com.talexframe.frame.core.modules.network.connection.app;

import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 12:14 Project: TalexFrame
 */
public class ClassReceiverManager {

    public static ClassReceiverManager INSTANCE = new ClassReceiverManager();

    private ClassReceiverManager() {



    }

    @Getter
    private final Map<Class<?>, ClassAppReceiver> reqMap = new HashMap<>();

    @Getter
    private final Map<Class<?>, WebPlugin> pluginMap = new HashMap<>();

    public boolean registerReqReceiver(WebPlugin webPlugin, ClassAppReceiver receiver) {

        if( reqMap.containsKey(receiver.getOwnClass()) ) {

            return false;

        }

        reqMap.put(receiver.getOwnClass(), receiver);
        pluginMap.put(receiver.getOwnClass(), webPlugin);

        return true;

    }

    public boolean unRegisterReqReceiver(Class<?> cls) {

        if( !reqMap.containsKey(cls) ) {

            return false;

        }

        reqMap.remove(cls);
        pluginMap.remove(cls);

        return true;

    }

}
