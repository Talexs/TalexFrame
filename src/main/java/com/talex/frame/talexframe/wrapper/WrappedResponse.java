package com.talex.frame.talexframe.wrapper;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 结果包装类
 * <br /> {@link com.talex.frame.talexframe.wrapper Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 19:29 <br /> Project: TalexFrame <br />
 */
@Getter
@AllArgsConstructor
public class WrappedResponse {

    private BodyCopyHttpServletRequestWrapper request;
    private HttpServletResponse response;

    /**
     *
     * 注意，此重定向请求会被浏览器缓存，谨慎使用!
     *
     * @param directTarget 目标
     */
    @SneakyThrows
    public void returnCachedDirect(String directTarget) {

        response.setStatus(301);

        response.sendRedirect(directTarget);

    }

    @SneakyThrows
    public void returnDirect(String directTarget) {

        response.setStatus(302);

        response.sendRedirect(directTarget);

    }

    public WrappedResponse addHeader(String key, String value) {

        response.addHeader(key, value);

        return this;

    }

    @SneakyThrows
    public void returnDataByFailed(ResultData.ResultEnum rEnum, Object data) {

        if( response.isCommitted() ) return;

        String str = JSONUtil.toJsonStr(ResultData.FAILED(rEnum, data));

        response.setStatus(200);
        // response.setContentLength(str.length());
        response.setContentType("application/json");

        OutputStream os = response.getOutputStream();
        os.write(str.getBytes(StandardCharsets.UTF_8));
        os.flush();

    }

    @SneakyThrows
    public void returnDataByFailed(Object data) {

        if( response.isCommitted() ) return;

        String str = JSONUtil.toJsonStr(ResultData.FAILED(data));

        response.setStatus(200);
        // response.setContentLength(str.length());
        response.setContentType("application/json");

        OutputStream os = response.getOutputStream();
        os.write(str.getBytes(StandardCharsets.UTF_8));
        os.flush();

    }

    @SneakyThrows
    public void returnDataByOK(Object data) {

        String str = JSONUtil.toJsonStr(ResultData.SUCCESS(data));

        response.setStatus(200);
        // response.setContentLength(str.length());
        response.setContentType("application/json");

        OutputStream os = response.getOutputStream();
        os.write(str.getBytes(StandardCharsets.UTF_8));
        os.flush();

    }

}
