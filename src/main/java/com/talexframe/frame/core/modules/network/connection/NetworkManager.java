package com.talexframe.frame.core.modules.network.connection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * {@link com.talexframe.frame.core.modules.network.connection Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 12:51 Project: TalexFrame
 */
@Getter
public class NetworkManager {

    public static final NetworkManager INSTANCE = new NetworkManager();

    private final Multimap<String, ClassAppReceiver> map = ArrayListMultimap.create();

    private NetworkManager() {



    }

    public Collection<ClassAppReceiver> matchUrlReceivers(String path) {

        // 类不支持路径变量

        Collection<ClassAppReceiver> collection = new ArrayList<>();

        map.entries().forEach((entry) -> {

            if( path.startsWith(entry.getKey()) ) {

                collection.add(entry.getValue());

            }

        });

        return collection;

    }

}
