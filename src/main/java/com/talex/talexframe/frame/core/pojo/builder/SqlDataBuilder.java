package com.talex.talexframe.frame.core.pojo.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * <p>对MySql数据字段进行操作 整合 添加{@link SqlAddBuilder} 更新{@link SqlUpdBuilder}
 *
 * @see SqlAddBuilder 添加
 * @see SqlUpdBuilder 更新
 *
 * @author TalexDreamSoul
 * @date 2021/8/5 19:00
 * <p>
 * Project: TalexsWeb
 * <p>
 */
public class SqlDataBuilder extends SqlBuilder {

    private enum SqlDataType {

        /**
         *
         * 设置输出为 @{link SqlAddBuilder}
         *
         */
        ADD(),

        /**
         *
         * 设置输出为 @{link SqlUpdBuilder}
         *
         */
        UPD()

    }

    public static class DataParam {

        @Getter
        private String subParamName;

        public DataParam setName(String name){

            this.subParamName = name;
            return this;

        }

        @Getter
        private String subParamValue;

        public DataParam setValue(String value){

            this.subParamValue = value;
            return this;

        }

        public DataParam(String name, String value) {

            this.subParamName = name;
            this.subParamValue = value;

        }

    }

    @Getter
    @Setter
    private HashMap<String, DataParam> map = new HashMap<>();

    public SqlDataBuilder addParam(DataParam tp){

        this.map.put(tp.subParamName, tp);

        return this;

    }

    @Getter
    private final String tableName;

    public SqlDataBuilder(String table) {

        this.tableName = table;

    }

    @Getter
    private SqlAddBuilder.AddType addType = SqlAddBuilder.AddType.IGNORE;

    public SqlDataBuilder setType(SqlAddBuilder.AddType type){

        this.addType = type;

        return this;

    }

    @Getter
    private SqlDataType dataType = SqlDataType.ADD;

    @Getter
    private DataParam whereParam;

    @SuppressWarnings("unused")
    public SqlDataBuilder setWhereParam(DataParam whereParam) {

        this.whereParam = whereParam;

        return this;

    }

    @SuppressWarnings("unused")
    public SqlDataBuilder setWhereParam(String where) {

        this.whereParam = map.get(where);

        return this;

    }

    @SuppressWarnings("unused")
    public SqlDataBuilder setDataType(SqlDataType dataType) {

        this.dataType = dataType;

        return this;

    }

    public String toString(SqlDataType dataType) {

        return dataType == SqlDataType.ADD ? toStringByAdd() : toStringByUpd();

    }

    @Override
    public String toString(){

        return dataType == SqlDataType.ADD ? toStringByAdd() : toStringByUpd();

    }

    public String toStringByUpd() {

        StringBuilder sb = new StringBuilder("UPDATE `" + this.tableName + "` SET ");
        int i = 0;

        for(DataParam tp : map.values()){

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

    public String toStringByAdd() {

        StringBuilder sb = new StringBuilder((addType == SqlAddBuilder.AddType.IGNORE ? "INSERT " + addType.getDisplayName() : "REPLACE") + " INTO `" + this.tableName + "` (");
        StringBuilder key = new StringBuilder("VALUES (");
        int i = 0;

        List<DataParam> list = new ArrayList<>(map.values());

        list.add(whereParam);

        for( DataParam tp : list ){

            ++i;
            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "`");

            if(list.size() <= i){

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
