package com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.sql;

import com.talexframe.frame.core.modules.network.connection.app.addon.param.valid.ReceiverValidateAddon;
import com.talexframe.frame.core.pojo.wrapper.WrappedResponse;

/**
 * {@link com.talexframe.frame.core.modules.network.connection.app.addon.login Package }
 *
 * @author TalexDreamSoul 22/06/06 下午 04:47 Project: TalexFrame
 */
@SuppressWarnings( "UnstableApiUsage" )
public class ReceiverParamSqlValidateAddon extends ReceiverValidateAddon<TParamSqlValid> {

    public ReceiverParamSqlValidateAddon() {

        super("ReceiverParamSqlValidate");

    }

    @Override
    public boolean validate(WrappedResponse wr, TParamSqlValid tParamSqlValid, Object addedParam) {

        String str = (String) addedParam;

        str = str.toLowerCase();

        String[] badStrArray = (tParamSqlValid.normal() + "|" + tParamSqlValid.extra())
                .split("\\|");

        for ( final String s : badStrArray ) {

            if ( str.contains(s) ) {

                return false;

            }

        }

        return true;

    }

    @Override
    public String getErrorMessage() {

        return "你传入的参数非法!";

    }

}
