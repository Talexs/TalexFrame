package com.talexframe.frame.core.modules.network.connection.app.addon.method;

import com.talexframe.frame.core.modules.network.connection.app.ClassAppReceiver;
import com.talexframe.frame.core.modules.network.connection.app.addon.ReceiverAddon;
import com.talexframe.frame.core.modules.network.connection.app.subapp.MethodAppReceiver;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverMethodAddon extends ReceiverAddon {

    public ReceiverMethodAddon() {

        super("ReceiverMethod", new ReceiverAddonType[] { ReceiverAddonType.CLASS_APP, ReceiverAddonType.METHOD_APP });

        super.priority = ReceiverAddonPriority.SUPER_HIGHEST;

    }

    @Override
    public boolean onPreCheckAppReceiver(ClassAppReceiver classAppReceiver, WrappedResponse wr) {

        TReqSupportMethod supportMethod = classAppReceiver.getOwnClass().getAnnotation(TReqSupportMethod.class);

        if( supportMethod == null ) return true;

        return got(supportMethod, wr);

    }

    @Override
    public boolean onPreInvokeMethod(MethodAppReceiver methodAppReceiver, WrappedResponse wr) {

        TReqSupportMethod supportMethod = methodAppReceiver.getMethod().getAnnotation(TReqSupportMethod.class);

        if( supportMethod == null ) return true;

        TReqBody tReqBody = methodAppReceiver.getMethod().getAnnotation(TReqBody.class);

        if( tReqBody != null ) {

            if( Arrays.stream(supportMethod.value()).anyMatch(method -> method == HttpMethod.GET) ) {

                throw new RuntimeException("[解析层] MethodNotSupport # GET: 当为 GET 时无法解析 Body Data ！");

            } else {

                methodAppReceiver.setParseData(true);

            }

        }

        return got(supportMethod, wr);

    }

    private boolean got(TReqSupportMethod supportMethod, WrappedResponse wr) {

        HttpServletRequest request = wr.getRequest();

        for( HttpMethod method : supportMethod.value() ) {

            if( method.name().equals(request.getMethod()) ) return true;

        }

        return false;

    }

}
