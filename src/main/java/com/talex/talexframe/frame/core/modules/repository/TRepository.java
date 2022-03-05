package com.talex.talexframe.frame.core.modules.repository;

import com.talex.talexframe.frame.core.pojo.dao.factory.DAOManager;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.Mysql;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert.SqlInsertBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlDelBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlLikeBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.table.SqlTableBuilder;
import com.talex.talexframe.frame.core.talex.FrameCreator;
import com.talex.talexframe.frame.core.talex.TFrame;
import lombok.Getter;

import java.sql.ResultSet;

/**
 * <br /> {@link com.talex.talexframe.frame.core.modules.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:02 <br /> Project: TalexFrame <br />
 */
@Getter
@SuppressWarnings("unused")
public class TRepository extends FrameCreator {

    protected final TFrame tframe = TFrame.tframe;
    protected final Mysql mysql = new DAOManager.ProcessorGetter<Mysql>().getProcessor();
    protected TRepositoryManager repositoryManager = tframe.getRepositoryManager();

    /**
     *
     * provider 请一定填写 table name
     *
     * @param provider TableName
     */
    public TRepository(String provider) {

        super("TREPOSITORY", provider);
    }

    public SqlInsertBuilder newSqlAddBuilder() {
        return new SqlInsertBuilder(getProvider());
    }

    public SqlLikeBuilder newSqlLikeBuilder() {
        return new SqlLikeBuilder(getProvider());
    }

    public SqlTableBuilder newSqlTableBuilder() {
        return new SqlTableBuilder(getProvider());
    }

    public boolean autoAccess(SqlBuilder sb) {
        return this.mysql.autoAccess(sb);
    }

    public ResultSet likeData(SqlLikeBuilder slb) {
        return this.mysql.likeData(slb);
    }

    public boolean prepareStatement(String sql) {
        return this.mysql.prepareStatement(sql);
    }

    public boolean insertData(SqlInsertBuilder sab) {
        return this.mysql.insertData(sab);
    }

    public boolean deleteData(SqlDelBuilder sdb) {
        return this.mysql.delData(sdb);
    }

    public void createTable(SqlTableBuilder stb) {
        this.mysql.createTable(stb);
    }

    public ResultSet readSearchAllData() {
        return this.mysql.readAllData(getProvider());
    }

    public ResultSet readSearchData(String selectType, String value) {
        return this.mysql.searchData(getProvider(), selectType, value);
    }

    public ResultSet readSearchData(String selectType, String value, int limit) {
        return this.mysql.searchData(getProvider(), selectType, value, limit);
    }

}
