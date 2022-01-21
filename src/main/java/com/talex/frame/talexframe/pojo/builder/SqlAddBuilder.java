package com.talex.frame.talexframe.pojo.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class SqlAddBuilder extends SqlBuilder {

    public static class AddParam {

        @Getter
        private String subParamName;

        public AddParam setSubParamName(String name){

            this.subParamName = name;
            return this;

        }

        @Getter
        private String subParamValue;

        public AddParam setSubParamValue(String value){

            this.subParamValue = value;
            return this;

        }

    }

    @Getter
    @Setter
    private List<AddParam> map = new ArrayList<>();

    public SqlAddBuilder addTableParam(AddParam tp){

        this.map.add(tp);
        return this;

    }

    @Getter
    private String tableName = "default_table_" + System.currentTimeMillis();

    public SqlAddBuilder setTableName(String name){

        this.tableName = name;
        return this;

    }

    public enum AddType {

        /** 忽略 **/
        IGNORE("IGNORE"),

        /** 替换 **/
        REPLACE("REPLACE");

        @Getter
        String displayName;

        AddType(String displayName){

            this.displayName = displayName;

        }

    }

    @Getter
    private AddType addType = AddType.IGNORE;

    public SqlAddBuilder setType(AddType type){

        this.addType = type;
        return this;

    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder((addType == AddType.IGNORE ? "INSERT " + addType.getDisplayName() : "REPLACE") + " INTO `" + this.tableName + "` (");
        StringBuilder key = new StringBuilder("VALUES (");
        int i = 0;

        for(AddParam tp : map){

            ++i;
            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "`");

            if(map.size() <= i){

                key.append("\"").append(tp.getSubParamValue()).append("\"");

            } else {

                key.append("\"").append(tp.getSubParamValue()).append("\", ");
                tsb.append(", ");

            }

            sb.append(tsb);

        }

        return sb.append(") ").append(key).append(")").toString();

    }

}
