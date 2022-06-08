package com.talexframe.frame.core.modules.network.connection.app;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.network.connection.IRequestReceiver;
import com.talexframe.frame.core.modules.network.connection.NetworkManager;
import com.talexframe.frame.core.modules.network.connection.TRequest;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.utils.UrlUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 12:08 Project: TalexFrame
 */
@Getter
@Slf4j
public class ClassAppReceiver extends FrameCreator implements IRequestReceiver {

    private final TApp tApp;
    private final Class<? extends TApp> ownClass;
    private final Multimap<String, MethodAppReceiver> map = ArrayListMultimap.create();

    public ClassAppReceiver(TApp tApp, Class<? extends TApp> ownClass) {

        super("ClassAppReceiver", ownClass.getName());

        this.tApp = tApp;
        this.ownClass = ownClass;

        NetworkManager.INSTANCE.getMap().put(UrlUtil.formatUrl(tApp.getDefaultPath()), this);

        this.scanForSubApp();

    }

    public Set<MethodAppReceiver> matchUrlSubReceivers(String url) {

        Set<MethodAppReceiver> list = new HashSet<>();

        this.map.entries().forEach((entry) -> {

            if( UrlUtil.advancedUrlChecker(entry.getKey(), url) ) {

                list.add( entry.getValue() );

                log.debug("[解析层] 匹配到 - {} | @{}.{}", entry.getKey(), entry.getValue().getOwnClass().getName(), entry.getValue().getMethod().getName());

            }

        });

        return list;

    }

    private void scanForSubApp() {

        for ( Method method : ownClass.getMethods()  ) {

            // 过滤掉没有注解的方法
            if ( !method.isAnnotationPresent(TRequest.class) ) continue;

            MethodAppReceiver receiver = new MethodAppReceiver(this.tApp, method);

            String value = UrlUtil.formatUrl(receiver.getTRequest().value());
            map.put(value, receiver);

            log.debug("[ClassAppReceiver] 方法注册成功 - " + value);

        }

    }

}
