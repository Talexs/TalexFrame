package com.talex.talexframe.frame.core.modules.network.connection;

import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.RateLimiter;
import com.talex.talexframe.frame.core.modules.controller.TControllerManager;
import com.talex.talexframe.frame.core.modules.event.events.request.RequestCannotGetTokenEvent;
import com.talex.talexframe.frame.core.modules.event.events.request.RequestCorsTryEvent;
import com.talex.talexframe.frame.core.pojo.wrapper.ResultData;
import com.talex.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talex.talexframe.frame.core.talex.TFrame;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 请求连接器
 * <br /> {@link com.talex.talexframe.frame.core.modules.network.connection Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 08:46 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class RequestConnector {

    static TFrame tframe = TFrame.tframe;
    static RateLimiter globalLimiter = RateLimiter.create(100, 1, TimeUnit.SECONDS);
    static TControllerManager manager = tframe.getControllerManager();

    private WrappedResponse wr;

    public RequestConnector( WrappedResponse wr ) {

        this.wr = wr;

        this.printConnectionInfo();

        this.processLimiter();

    }

    /** 打印连接信息 **/
    private void printConnectionInfo() {

        HttpServletRequest request = wr.getRequest();

        log.info("[连接层] Receive new request from " + request.getRemoteAddr() + "(" + request.getRemoteHost() + ")");
        log.info("[连接层] Visit: " + request.getRequestURI() + " # Type: " + request.getMethod());
        log.debug("[连接层] Details: " + JSONUtil.toJsonStr( request ));

    }

    private void processLimiter() {

        if( !globalLimiter.tryAcquire(100, TimeUnit.MILLISECONDS) ) {

            RequestCannotGetTokenEvent rcg = new RequestCannotGetTokenEvent(wr, RequestCannotGetTokenEvent.LimiterType.GLOBAL);

            if( !rcg.isCancelled() ) {

                wr.returnDataByFailed(ResultData.ResultEnum.SERVICE_LIMITED, "服务器繁忙");

                log.warn("[连接层] 请求已被拦截 - 服务器繁忙 (Global)");

                return;

            }

        }

        globalLimiter.acquire();

        this.headerProcessor();

    }

    private void headerProcessor() {

        HttpServletResponse response = wr.getResponse();

        if( !response.containsHeader("Access-Control-Allow-Origin") ) {

            response.addHeader("Access-Control-Allow-Origin", "*");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            response.addHeader("Access-Control-Allow-Headers", "*");
            response.addHeader("Access-Control-Max-Age", "1728000");

        }

        HttpServletRequest request = wr.getRequest();

        if ( request.getMethod().equals("OPTIONS") ) {

            RequestCorsTryEvent e = new RequestCorsTryEvent(wr);

            tframe.callEvent(e);

            if(e.isCancelled()) return;

            log.info("[连接层] 请求 " + request.getRequestURI() + " #来自: " + request.getSession().getId() + " 已批准跨域!");

            wr.returnDataByOK("SUCCESS");

            return;

        }

        this.analyser();

        if( !response.isCommitted() ) {

            wr.returnDataByFailed(ResultData.ResultEnum.NOT_FOUND, "请重新确认 URL 正误.");

        }

    }

    private void analyser() {

        new RequestAnalyser(wr);

    }



}
