package com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.max.value;

import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.ReceiverValidateAddon;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverMaxValueValidateAddon extends ReceiverValidateAddon<TMaxValueValid> {

    public ReceiverMaxValueValidateAddon() {

        super("ReceiverMaxValueValidate");

    }

    @Override
    public boolean validate(WrappedResponse wr, TMaxValueValid tMaxValueValid, Object addedParam) {

        if( !(addedParam instanceof Number) ) return false;

        return ((Number)addedParam).intValue() <= tMaxValueValid.value();

    }

    @Override
    public String getErrorMessage() {

        return "不满足最大值区间!";

    }

}
