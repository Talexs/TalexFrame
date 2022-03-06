package com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder;

import com.talex.talexframe.frame.core.pojo.FrameBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.Mysql;
import lombok.Getter;

public abstract class SqlBuilder extends FrameBuilder {

    protected static Mysql mysql = new DAOManager.ProcessorGetter<Mysql>().getProcessor();

    @Getter
    protected String tableName;

    public SqlBuilder(String provider, String tableName) {

        super(provider);

        this.tableName = tableName;

    }

    @Override
    public abstract String toString();

}
