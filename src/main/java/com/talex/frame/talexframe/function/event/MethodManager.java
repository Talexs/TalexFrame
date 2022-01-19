package com.talex.frame.talexframe.function.event;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

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


