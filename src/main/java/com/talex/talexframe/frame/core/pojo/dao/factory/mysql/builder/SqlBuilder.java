package com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder;

import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.Mysql;
import lombok.Getter;

public abstract class SqlBuilder {

    protected Mysql mysql = Mysql.getInstance();

    @Getter
    protected String tableName;

    public SqlBuilder(String tableName) {

        this.tableName = tableName;

    }

    @Override
    public abstract String toString();

}
