package com.talexframe.frame.core.modules.network.connection.app.subapp;

import cn.hutool.core.date.TimeInterval;
import com.talexframe.frame.core.modules.application.TApp;
import com.talexframe.frame.core.modules.network.connection.IRequestReceiver;
import com.talexframe.frame.core.modules.network.connection.TRequest;
import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.core.talex.FrameCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 12:08 Project: TalexFrame
 */
@Getter
@Slf4j
public class MethodAppReceiver extends FrameCreator implements IRequestReceiver {

    static final TimeInterval timer = new TimeInterval();

    private final TApp ownApp;
    private final Class<?> ownClass;
    private final Method method;
    private final TRequest tRequest;

    public MethodAppReceiver(TApp ownApp, Method method) {

        super("MethodAppReceiver", ownApp.getClass().getName());

        this.ownApp = ownApp;
        this.method = method;
        this.ownClass = ownApp.getClass();

        this.tRequest = method.getAnnotation(TRequest.class);

        this.params = new ArrayList<>(method.getParameterCount());

    }

    private final List<Object> params;

    @Setter
    private boolean parseData = false;

    public Object onRequest(ClassAppReceiver clzAppReceiver, MethodAppReceiver methodAppReceiver, WrappedResponse wr, long time) {

        log.info("[解析层] Access @" + clzAppReceiver.getOwnClass().getName());

        try {

            String requestID = wr.getRequest().getSession().getId();

            timer.start(requestID);

            log.debug("params: " + params);

            Object obj = method.invoke(ownApp, params.toArray());

            long ms = timer.intervalMs(wr.getRequest().getSession().getId());

            if ( ms > 300 ) {

                log.warn("[应用层] 处理完毕 - 耗时较长，请优化接口！ 耗时: " + ms + "ms");

            } else {

                log.info("[应用层] 处理完毕 耗时: " + ms + "ms");

            }

            log.info("[解析层] Access in " + (System.currentTimeMillis() - time) + "ms totally");

            return obj;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

}
