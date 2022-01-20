package com.talex.frame.talexframe.wrapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    private HttpServletRequest request;
    private HttpServletResponse response;

}
