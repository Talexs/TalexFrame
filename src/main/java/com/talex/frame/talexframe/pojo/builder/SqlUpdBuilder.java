package com.talex.frame.talexframe.pojo.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class SqlUpdBuilder extends SqlBuilder{

    public static class UpdParam {

        @Getter
        private String subParamName;

        public UpdParam setSubParamName(String name){

            this.subParamName = name;
            return this;

        }

        @Getter
        private String subParamValue;

        public UpdParam setSubParamValue(String value){

            this.subParamValue = value;
            return this;

        }

    }

    @Getter
    @Setter
    private List<UpdParam> map = new ArrayList<>();

    public SqlUpdBuilder addTableParam(UpdParam tp){

        this.map.add(tp);
        return this;

    }

    @Getter
    private String tableName = "default_table_" + System.currentTimeMillis();

    public SqlUpdBuilder setTableName(String name){

        this.tableName = name;
        return this;

    }

    public SqlUpdBuilder setWhereParam(UpdParam addParam){

        this.whereParam = addParam;
        return this;

    }

    private UpdParam whereParam;

    @Override
    public String toString(){

//        UPDATE `friend_list` SET `list` = \"" + StringUtil.Base64_Encode(String.join(" ",list)) + "\" WHERE name = \"" + current + "\"

        StringBuilder sb = new StringBuilder("UPDATE `" + this.tableName + "` SET ");
        int i = 0;

        for(UpdParam tp : map){

            ++i;
            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "` = \"" + tp.getSubParamValue() + "\"");

            if(map.size() > i){

                tsb.append(", ");

            }

            sb.append(tsb);

        }

        if(whereParam == null){

            return sb.toString();

        }

        return sb.append(" WHERE ").append(whereParam.getSubParamName()).append(" = \"").append(whereParam.getSubParamValue()).append("\"").toString();

    }

}
