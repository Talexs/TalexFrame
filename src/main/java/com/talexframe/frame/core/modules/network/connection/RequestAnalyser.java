package com.talexframe.frame.core.modules.network.connection;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlPath;
import cn.hutool.json.JSONUtil;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddonAdapter;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talexframe.frame.utils.UrlUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 请求解析器 <br /> {@link com.talexframe.frame.core.modules.network.connection Package }
 *
 * @author TalexDreamSoul
 * 22/03/05 下午 06:57 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class RequestAnalyser {

    private static final NetworkManager networkManager = NetworkManager.INSTANCE;

    private final WrappedResponse wr;
    private final HttpServletRequest request;
    private final long time;

    public RequestAnalyser(WrappedResponse wr, long time) {

        this.wr = wr;
        this.time = time;
        this.request = wr.getRequest();

        process();

    }

    @SneakyThrows
    private void process() {

        log.info("[解析层] RequestID: " + request.getSession().getId());

        // 解析请求
        String originalUrl = request.getRequestURI();
        UrlBuilder urlBuilder = UrlBuilder.ofHttp(originalUrl, Charset.forName(request.getCharacterEncoding()));

        UrlPath urlPath = urlBuilder.getPath();

        if( urlPath.getSegments().size() == 0 ) {

            log.info("[解析层] 请求路径为空!");

            wr.returnDataByFailed("请求路径为空!");

            return;

        }

        String parsedUrl = UrlUtil.formatUrl(urlPath.toString());

        networkManager.matchUrlReceivers(parsedUrl).forEach((reqReceiver) -> {

            AtomicBoolean could = new AtomicBoolean(true);
            LinkedList<ReceiverAddon> clsReceiverAddons = ReceiverAddonAdapter.getReceiverAddons(ReceiverAddon.ReceiverAddonType.CLASS_APP);
            clsReceiverAddons.forEach((addon) -> {

                if( !could.get() ) return;

                could.set(addon.onPreCheckAppReceiver(reqReceiver, wr));

            });

            if( !could.get() ) return;

            String subUrl = parsedUrl.replaceFirst(reqReceiver.getTApp().getDefaultPath(), "");

            log.debug("[解析层] 匹配到的路径: " + subUrl);

            reqReceiver.matchUrlSubReceivers(subUrl).forEach((subReqReceiver) -> {

                if( wr.getResponse().isCommitted() ) return;

                Collection<ReceiverAddon> receiverAddons = ReceiverAddonAdapter.getReceiverAddons(ReceiverAddon.ReceiverAddonType.METHOD_APP);

                receiverAddons.forEach((addon) -> {

                    if( !could.get() ) return;

                    could.set(addon.onPreInvokeMethod(subReqReceiver, wr));

                });

                Object obj = subReqReceiver.onRequest(reqReceiver, subReqReceiver, wr, time);

                receiverAddons.forEach((addon) -> addon.onPostAddParam(subReqReceiver, wr, obj));

                if ( obj != null ) {

                    String tStr = JSONUtil.toJsonStr(obj);

                    wr.returnData(tStr);

                    log.info("[应用层] OK Return: " + tStr);

                }

            });

            clsReceiverAddons.forEach((addon) -> addon.onPostCheckAppReceiver(reqReceiver, wr));

        });

    }

}
