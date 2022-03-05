package com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker;

import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.BuilderMap;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors( chain = true )
public class SqlLikeBuilder extends SqlBuilder {

    @Getter
    @Setter
    private BuilderMap map = new BuilderMap();

    public SqlLikeBuilder(String tableName) {

        super(tableName);

    }

    @Override
    public String toString(){

        return "SELECT * FROM `" + this.tableName + "` WHERE " + map.toSqlWhereColumn();

    }

}
