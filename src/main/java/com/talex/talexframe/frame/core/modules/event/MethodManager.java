package com.talex.talexframe.frame.core.modules.event;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TalexDreamSoul
 */
public class MethodManager {

    private Class<?> paramType;

    private Method method;

    @Getter
    private final FrameListener owner;

    @Setter
    @Getter
    private TalexSubscribe talexSubscribe;

    @Getter
    private final Map<String, Long> listenedEvents = new HashMap<>();

    public void listen(String key) {

        if( listenedEvents.containsKey(key) ) {

            throw new RuntimeException("在监听时出现错误 - key的重复发放 " + key);

        }

        listenedEvents.put(key, System.currentTimeMillis());

    }

    public MethodManager(FrameListener listener, Class<?> paramType, Method method, TalexSubscribe talexSubscribe) {

        this.owner = listener;
        this.paramType = paramType;
        this.method = method;
        this.talexSubscribe = talexSubscribe;

    }

    public Class<?> getParamType() { return paramType; }

    public void setParamType(Class<?> paramType) { this.paramType = paramType; }

    public Method getMethod() { return method; }

    public void setMethod(Method method) { this.method = method; }

    @Override
    public int hashCode() {

        return owner.getClass().getName().hashCode() + method.getName().hashCode();

    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof MethodManager) {

            MethodManager target = (MethodManager) obj;

            if(target.getClass().getName().equals(owner.getClass().getName())) {

                return method.getName().equals(target.getMethod().getName());

            }

        }

        return false;

    }

}


