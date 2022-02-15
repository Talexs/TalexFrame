package com.talex.frame.talexframe.pojo.builder;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class SqlTableBuilder extends SqlBuilder{

    @NoArgsConstructor
    public static class TableParam {

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

        @Getter
        private boolean uniqueOnly = false;

        public TableParam setUniqueOnly(boolean only){

            this.uniqueOnly = only;
            return this;

        }

        @Getter
        private String columnContent = null;

        public TableParam setColumnContent(String content){

        	this.columnContent = content;
        	return this;

        }

    }

    @Getter
    @Setter
    private List<TableParam> map = new ArrayList<>();

    private final List<String> tableKeys = new ArrayList<>();

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

    /**
     *
     * 若不设置则不会追加代码
     * 追加代码结尾必须为 ,
     *
     */
    @Getter
    private String addonSql;

    public SqlTableBuilder setAddonSql(String addonSql) {

        this.addonSql = addonSql;
        return this;

    }

    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + this.tableName + "` (");
        String key = "PRIMARY KEY (`%stp%`)";
        StringBuilder uniqueKey = new StringBuilder("UNIQUE KEY `only` (");

        for(TableParam tp : map){

            StringBuilder tsb = new StringBuilder("`" + tp.getSubParamName() + "` " + tp.type).append(" ");

            if(tp.isMain()){

                key = key.replace("%stp%",tp.subParamName);

            }

            if( !StrUtil.isBlankIfStr( tp.getColumnContent() ) ) {

                String content = tp.getColumnContent();

                if( !content.endsWith(",") ) {

                    content = content + ",";

                }

                sb.append( content );

            } else {

                if(tp.defaultNull == null){

                    tsb.append("DEFAULT NULL,");

                } else if(tp.defaultNull.equalsIgnoreCase("null")){

                    tsb.append("NOT NULL,");

                } else {

                    tsb.append("DEFAULT \"").append(tp.defaultNull).append("\",");

                }

                if(tp.isUniqueOnly()) {

                    uniqueKey.append("`").append(tp.getSubParamName()).append("`").append(",");

                }

                sb.append(tsb);

            }

        }

        String unique = uniqueKey.toString();

        if( unique.endsWith(",") ) {

            unique = unique.substring(0,unique.length() - 1) + "),";

            sb.append(unique);

        }

        if( this.getAddonSql() != null ) {

            sb.append(this.getAddonSql());

        }

        return sb.append(key).append(")").toString();

    }

}
