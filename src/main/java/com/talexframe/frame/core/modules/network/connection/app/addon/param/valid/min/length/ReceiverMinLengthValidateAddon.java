package com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.min.length;

import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.ReceiverValidateAddon;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverMinLengthValidateAddon extends ReceiverValidateAddon<TMinLengthValid> {

    public ReceiverMinLengthValidateAddon() {

        super("ReceiverMinLengthValidate");

    }

    @Override
    public boolean validate(WrappedResponse wr, TMinLengthValid tMinLengthValid, Object addedParam) {

        return addedParam != null && ((String) addedParam).length() >= tMinLengthValid.value();

    }

    @Override
    public String getErrorMessage() {

        return "不满足最小长度区间!";

    }

}
