package com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.nonull;

import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.ReceiverValidateAddon;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverNoNullValidateAddon extends ReceiverValidateAddon<TNoNullValid> {

    public ReceiverNoNullValidateAddon() {

        super("ReceiverNoNullValidate");

    }

    @Override
    public boolean validate(WrappedResponse wr, TNoNullValid tNoNullValid, Object addedParam) {

        return addedParam != null;

    }

    @Override
    public String getErrorMessage() {

        return "参数不能为空!";

    }

}
