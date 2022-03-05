package com.talex.talexframe.frame.core.modules.network.connection;

import com.talex.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import com.talex.talexframe.frame.core.talex.TFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求解析器
 * <br /> {@link com.talex.talexframe.frame.core.modules.network.connection Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 06:57 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class RequestAnalyser {

    private TFrame tframe = TFrame.tframe;

    public RequestAnalyser( WrappedResponse wr ) {

        log.info("[解析层] ");

    }

}
