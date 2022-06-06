package com.talexframe.frame.core.pojo.wrapper;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 结果包装类 <br /> {@link com.talexframe.frame.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 19:29 <br /> Project: TalexFrame <br />
 */
@Getter
@AllArgsConstructor
@Slf4j
public class WrappedResponse {

    private BodyCopyHttpServletRequestWrapper request;
    private HttpServletResponse response;

    /**
     * 注意，此重定向请求会被浏览器缓存，谨慎使用!
     *
     * @param directTarget 目标
     */
    @SneakyThrows
    public void returnCachedDirect(String directTarget) {

        response.setStatus(301);

        response.sendRedirect(directTarget);

        log.info("[应用层] (301) Redirect: " + directTarget);

    }

    @SneakyThrows
    public void returnDirect(String directTarget) {

        response.setStatus(302);

        response.sendRedirect(directTarget);

        log.info("[应用层] (302) Redirect: " + directTarget);

    }

    public WrappedResponse addHeader(String key, String value) {

        response.addHeader(key, value);

        return this;

    }

    @SneakyThrows
    public void returnDataByFailed(ResultData.ResultEnum rEnum, Object data) {

        returnData(ResultData.FAILED(rEnum, data));

    }

    @SneakyThrows
    public void returnDataByFailed(Object data) {

        this.returnData(ResultData.FAILED(data));

    }

    @SneakyThrows
    public void returnDataByOK(Object data) {

        this.returnData(ResultData.SUCCESS(data));

    }

    @SneakyThrows
    public void returnData(Object data) {

        if ( response.isCommitted() ) {

            return;

        }

        String str = JSONUtil.toJsonStr(data);

        response.setStatus(200);
        // response.setContentLength(str.length());
        response.setContentType("application/json");

        OutputStream os = response.getOutputStream();
        os.write(str.getBytes(StandardCharsets.UTF_8));

        os.flush();

        log.info("[应用层] Return: " + str);

    }

    @SneakyThrows
    public void returnDataByBlob(byte[] data) {

        response.setStatus(200);
        // response.setContentType("multipart/form-data");

        OutputStream os = response.getOutputStream();
        os.write(data);
        os.flush();

        log.info("[应用层] ReturnBlob (" + data.length + ")");

    }

    @SneakyThrows
    public void returnDataByBlob(InputStream is) {

        response.setStatus(200);
        // response.setContentType("multipart/form-data");

        OutputStream os = response.getOutputStream();

        byte[] bytes = new byte[4096];
        long size = 0;
        while ( ( size += is.read(bytes) ) != -1 ) {

            os.write(bytes);

        }

        os.flush();

        log.info("[应用层] ReturnBlob (" + size + ")");

    }

}
