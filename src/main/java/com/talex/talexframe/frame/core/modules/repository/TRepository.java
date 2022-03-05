package com.talex.talexframe.frame.core.modules.repository;

import com.talex.talexframe.frame.core.modules.mysql.MysqlManager;
import com.talex.talexframe.frame.core.pojo.builder.*;
import com.talex.talexframe.frame.core.talex.FrameCreator;
import com.talex.talexframe.frame.core.talex.TFrame;
import com.talex.frame.talexframe.pojo.builder.*;
import lombok.Getter;

import java.sql.ResultSet;

/**
 * <br /> {@link com.talex.frame.talexframe.function.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:02 <br /> Project: TalexFrame <br />
 */
@Getter
public class TRepository extends FrameCreator {

    protected final TFrame tframe = TFrame.tframe;
    protected final MysqlManager mysql = tframe.getMysqlManager();
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

    public SqlDataBuilder newSqlDataBuilder() {
        return new SqlDataBuilder(getProvider());
    }

    public SqlAddBuilder newSqlAddBuilder() {
        return new SqlAddBuilder().setTableName(getProvider());
    }

    public SqlLikeBuilder newSqlLikeBuilder() {
        return new SqlLikeBuilder().setTableName(getProvider());
    }

    public SqlTableBuilder newSqlTableBuilder() {
        return new SqlTableBuilder().setTableName(getProvider());
    }

    public SqlUpdBuilder newSqlUpdBuilder() {
        return new SqlUpdBuilder().setTableName(getProvider());
    }

    public boolean autoAccess(SqlBuilder sb) {
        return this.mysql.autoAccess(sb);
    }

    public ResultSet likeData(SqlLikeBuilder slb) {
        return this.mysql.likeData(slb.setTableName(getProvider()));
    }

    public boolean prepareStatement(String sql) {
        return this.mysql.prepareStatement(sql);
    }

    public boolean updateData(SqlUpdBuilder upd) {
        return this.mysql.updateData(upd.setTableName(getProvider()));
    }

    public boolean addData(SqlAddBuilder sab) {
        return this.mysql.addData(sab.setTableName(getProvider()));
    }

    public boolean deleteData(String type, String value) {
        return this.mysql.deleteData(getProvider(), type, value);
    }

    public void joinTable(SqlTableBuilder stb) {
        this.mysql.joinTable(stb);
    }

    public ResultSet readSearchAllData() {
        return this.mysql.readSearchAllData(getProvider());
    }

    public ResultSet readSearchData(String selectType, String value) {
        return this.mysql.readSearchData(getProvider(), selectType, value);
    }

    public ResultSet readSearchData(String selectType, String value, int limit) {
        return this.mysql.readSearchData(getProvider(), selectType, value, limit);
    }


}
