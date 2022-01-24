package com.talex.frame.talexframe.function.event.service;

import cn.hutool.core.thread.ThreadUtil;
import com.talex.frame.talexframe.function.event.*;
import com.talex.frame.talexframe.pojo.enums.ThreadMode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TalexDreamSoul
 */
@Slf4j
public class TalexEventBus implements IEventBus {

    private static TalexEventBus instance;

    @Getter
    private final Map<FrameListener, List<MethodManager>> mapCaches;

    private TalexEventBus() {

        mapCaches = new HashMap<>();

    }

    public static TalexEventBus getDefault() {

        if (instance == null) {

            instance = new TalexEventBus();

            return instance;

        }

        return null;

    }

    @Override
    public TalexEventBus registerListener(FrameListener listener) {

        List<MethodManager> methodsList = mapCaches.get(listener);

        if (methodsList == null || methodsList.size() == 0) {

            methodsList = new ArrayList<>();

            Class<?> clazz = listener.getClass();

            Method[] methods = clazz.getMethods();

            for (Method method : methods) {

                TalexSubscribe annotation = method.getAnnotation(TalexSubscribe.class);

                if (annotation == null) { continue; }

                Type type = method.getGenericReturnType();

                if (!"void".equals(type.toString())) {
                    throw new RuntimeException(method.getName() + " 返回值类型必须为void类型");
                }

                Class<?>[] parameterTypes = method.getParameterTypes();

                if (parameterTypes.length != 1) {

                    throw new RuntimeException(method.getName() + " 参数个数必须为1");

                }

                MethodManager methodManager = new MethodManager(listener, parameterTypes[0],
                        method, annotation);

                methodsList.add(methodManager);

                for( TalexEvent continueEvent : continueEvents.values() ) {

                    this.callEvent( continueEvent, methodManager );

                }

            }

            mapCaches.put(listener, methodsList);

        }

        return this;

    }

    private void callEvent(TalexEvent event, MethodManager methodManager) {

        if (methodManager.getParamType().isAssignableFrom(event.getClass())) {

            methodManager.listen( (( IContinue ) event).getMatchKey() );

            if(methodManager.getTalexSubscribe().threadMode() == ThreadMode.ASYNC) {

                ThreadUtil.execAsync(() -> event(event, methodManager));

            } else {

                event(event, methodManager);

            }

        }

    }

    @Override
    public TalexEventBus callEvent(TalexEvent event) {

        if( event instanceof IContinue ) {

            String matchKey = ( (IContinue) event ).getMatchKey();
            continueEvents.put(matchKey, event);

        }

        Map<Integer, MethodManager> preList = new HashMap<>(mapCaches.size());

        for( Map.Entry<FrameListener, List<MethodManager>> entry : mapCaches.entrySet() ) {

            List<MethodManager> methodList = entry.getValue();

            for (MethodManager methodManager : methodList) {

                if (methodManager.getParamType().isAssignableFrom(event.getClass())) {

                    preList.put(methodManager.hashCode(), methodManager);

                }
            }

        }

        List<MethodManager> pl = new ArrayList<>(preList.values());

        pl.sort((o1, o2) -> {

            TalexSubscribe ts1 = o1.getTalexSubscribe();
            TalexSubscribe ts2 = o2.getTalexSubscribe();

            return Integer.compare(ts1.priority().getLevel(), ts2.priority().getLevel());

        });

        for(MethodManager methodManager : pl) {

            if(methodManager.getTalexSubscribe().threadMode() == ThreadMode.ASYNC) {

                ThreadUtil.execAsync(() -> event(event, methodManager));

            } else {

                event(event, methodManager);

            }

        }

        return this;

    }

    @Getter
    private final Map<String, TalexEvent> continueEvents = new HashMap<>(16);

    private void event(TalexEvent event, MethodManager methodManager) {

        Method method = methodManager.getMethod();

        log.debug("[Event] --> Invoke: " + event.getClass().getName() + " | " + method.getName() + " <> " + methodManager.getOwner().getClass().getName() );

        try {

            method.invoke(methodManager.getOwner(), event);

            if( event instanceof IContinue ) {

                String matchKey = ( (IContinue) event ).getMatchKey();
                continueEvents.put(matchKey, event);

                methodManager.listen(matchKey);

            }

            if( methodManager.getTalexSubscribe().once() ) {

                FrameListener listener = methodManager.getOwner();

                List<MethodManager> methodsList = mapCaches.get(listener);

                methodsList.remove(methodManager);

                mapCaches.put(listener, methodsList);

            }

        } catch (Exception e) {

            if( e.getCause() == null ) {


                log.error("[事件] 在调用 " + method.getName() + " 时, 目标抛出异常" + "   @" + method.getDeclaringClass().getName() + "." + method.getName());

            } else log.error("[事件] 在调用 " + method.getName() + " 时, 目标抛出异常: " + e.getCause().getMessage() + "   @" + method.getDeclaringClass().getName() + "." + method.getName());

            e.printStackTrace();

        }

    }

    @Override
    public TalexEventBus unRegisterListener(FrameListener listener) {

        mapCaches.remove(listener);

        return this;

    }

}


