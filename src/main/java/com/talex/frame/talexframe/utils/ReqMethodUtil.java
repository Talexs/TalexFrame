package com.talex.frame.talexframe.utils;

import com.talex.frame.talexframe.pojo.annotations.TReqSupportMethod;

/**
 * <br /> {@link com.talex.frame.talexframe.utils Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/29 0:49 <br /> Project: TalexFrame <br />
 */
public class ReqMethodUtil {

    public static boolean checkStatus(String method, TReqSupportMethod reqSupportMethod) {

        if( method.equalsIgnoreCase("GET") && reqSupportMethod.get() ) {

            return true;

        }

        if( method.equalsIgnoreCase("POST") && reqSupportMethod.post() ) {

            return true;

        }

        if( method.equalsIgnoreCase("PUT") && reqSupportMethod.put() ) {

            return true;

        }

        if( method.equalsIgnoreCase("DELETE") && reqSupportMethod.delete() ) {

            return true;

        }

        if( method.equalsIgnoreCase("PATCH") && reqSupportMethod.patch() ) {

            return true;

        }

        if ( method.equalsIgnoreCase("HEAD") && reqSupportMethod.head() ) {

            return true;

        }

        if( method.equalsIgnoreCase("OPTIONS") && reqSupportMethod.options() ) {

            return true;

        }

        return false;

    }
}
