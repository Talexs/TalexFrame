package com.talex.frame.talexframe.pojo.builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class SqlTableBuilder extends SqlBuilder{

    @NoArgsConstructor
    public static class TableParam{

        @Getter
        private String subParamName;

        public TableParam setSubParamName(String name){

            this.subParamName = name;
            return this;

        }

        public TableParam(String subParamName) {

            this.subParamName = subParamName;

        }

        @Getter
        private String type = "TEXT(1024)";

        public TableParam setType(String type){

            this.type = type;
            return this;

        }

        @Getter
        private String defaultNull;

        public TableParam setDefaultNull(String defaultNull){

            this.defaultNull = defaultNull;
            return this;

        }

        @Getter
        private boolean main = false;

        public TableParam setMain(boolean main){

            this.main = main;
            return this;

        }

    }

    @Getter
    @Setter
    private List<TableParam> map = new ArrayList<>();

    private List<String> tableKeys = new ArrayList<>();

    public void addTableParamIgnore(TableParam tp){

        if(tableKeys.contains(tp.getSubParamName())) {

            return;

        }

        this.map.add(tp);
        tableKeys.add(tp.getSubParamName());

    }

    @SneakyThrows
    public SqlTableBuilder addTableParam(TableParam tp){

        if(tableKeys.contains(tp.getSubParamName())) {

            throw new Exception("此 Key 早已存在: " + tp.getSubParamName());

        }

        this.map.add(tp);
        tableKeys.add(tp.getSubParamName());

        return this;

    }

    @Getter
    private String tableName = "default_table_" + System.currentTimeMillis();

    public SqlTableBuilder setTableName(String name){

        this.tableName = name;
        return this;

    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + this.tableName + "` (");
        String key = "PRIMARY KEY (`%stp%`)";

        for(TableParam tp : map){

            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "` " + tp.type).append(" ");

            if(tp.isMain()){

                key = key.replace("%stp%",tp.subParamName);

            }

            if(tp.defaultNull == null){

                tsb.append("DEFAULT NULL,");

            }else if(tp.defaultNull.equalsIgnoreCase("null")){

                tsb.append("NOT NULL,");

            }else{

                tsb.append("DEFAULT \"").append(tp.defaultNull).append("\",");

            }

            sb.append(tsb);

        }

        return sb.append(key).append(")").toString();

    }

}
