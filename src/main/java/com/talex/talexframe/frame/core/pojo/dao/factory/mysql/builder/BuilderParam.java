package com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <br /> {@link com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:50 <br /> Project: TalexFrame <br />
 */
@NoArgsConstructor
@AllArgsConstructor
public class BuilderParam{

    @Getter
    private String subParamName;

    public BuilderParam setSubParamName(String name){

        this.subParamName = name;
        return this;

    }

    @Getter
    private Object subParamValue;

    public BuilderParam setSubParamValue(Object value){

        this.subParamValue = value;
        return this;

    }

}
