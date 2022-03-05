package com.talex.talexframe.frame.core.pojo.dao.factory.mysql;

import cn.dev33.satoken.secure.SaBase64Util;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.BuilderParam;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.BuilderWhereParam;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br /> {@link com.talex.talexframe.frame.core.pojo.dao.factory.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:51 <br /> Project: TalexFrame <br />
 */
@SuppressWarnings("unused")
public class BuilderMap {

    @Getter
    private final List<BuilderParam> params = new ArrayList<>();

    public BuilderMap addParam(BuilderParam param) {

        params.add(param);

        return this;

    }

    public BuilderMap addMapParam(Map<String, Object> map) {

        map.forEach((key, value) -> params.add(new BuilderParam(key, value)));

        return this;

    }

    public Map<String, String> toMap() {

        return toMap(false);

    }

    /**
     *
     * @param encode should encode with base64
     *
     */
    public Map<String, String> toMap(boolean encode) {

        Map<String, String> map = new HashMap<>();

        for( BuilderParam param : params ) {

            Object value = param.getSubParamValue();

            if( ClassUtil.isBasicType(value.getClass()) ) {

                map.put(param.getSubParamName(), String.valueOf(value));

            } else {

                map.put(param.getSubParamName(), encode ? SaBase64Util.encode(JSONUtil.toJsonStr(value)) : JSONUtil.toJsonStr(value));

            }

        }

        return map;

    }

    public String toSqlWhereColumn() {

        return toSqlWhereColumn(false);

    }

    /**
     *
     * @param encode should encode with base64
     *
     */
    public String toSqlWhereColumn(boolean encode) {

        StringBuilder sb = new StringBuilder();

        for( BuilderParam param : params ) {

            Object value = param.getSubParamValue();
            BuilderWhereParam whereParam = (BuilderWhereParam) new BuilderWhereParam().setSubParamName(param.getSubParamName()).setSubParamValue(value);

            if( param instanceof BuilderWhereParam ) {

                whereParam = (BuilderWhereParam) param;

            }

            String type = whereParam.isUseLike() ? "like" : "=";
            String content;

            if( ClassUtil.isBasicType(value.getClass()) ) {

                content = String.valueOf(value);

            } else {

                content = encode ? SaBase64Util.encode(JSONUtil.toJsonStr(value)) : JSONUtil.toJsonStr(value);

            }

            sb.append("\"").append(param.getSubParamName()).append("\"").append(" ").
                    append(type).append(" \"").append(content).append("\" ").append(whereParam.isAfterAnd() ? " and " : " or ");

        }

        return sb.toString();

    }

}
