package com.talexframe.frame.core.pojo.dao.factory.mysql;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.db.Session;
import com.alibaba.druid.pool.DruidDataSource;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorConnectFailedEvent;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorConnectedEvent;
import com.talexframe.frame.core.modules.event.events.dao.DAOProcessorPreShutdownEvent;
import com.talexframe.frame.core.pojo.dao.DataProcessorStatus;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.SqlBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert.SqlInsertBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlDelBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlLikeBuilder;
import com.talexframe.frame.core.pojo.dao.factory.mysql.builder.table.SqlTableBuilder;
import com.talexframe.frame.core.pojo.dao.interfaces.IConnectorProcessor;
import com.talexframe.frame.core.pojo.dao.interfaces.IDataProcessor;
import com.talexframe.frame.core.pojo.dao.interfaces.IProcessorConfig;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <br /> {@link com.talexframe.frame.core.pojo.dao.factory.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 22/03/05 下午 01:06 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class Mysql implements IDataProcessor, IConnectorProcessor {

    private final DruidDataSource dataSource = new DruidDataSource();

    @Getter
    private Session session;

    private DataProcessorStatus status = DataProcessorStatus.DISCONNECTED;

    // private final AtomicInteger counter = new AtomicInteger();

    @Override
    public int insertData(SqlInsertBuilder sab) {

        return prepareStatement(sab.toString());
    }

    @Override
    public int delData(SqlDelBuilder sdb) {

        return delData(sdb, 1);
    }

    public int delData(SqlDelBuilder sdb, int limit) {

        if ( limit > 0 ) {

            return prepareStatement(sdb.toString());

        } else {

            return prepareStatement(sdb.toStringWithLimit(limit));

        }

    }

    @Override
    public ResultSet likeData(SqlLikeBuilder slb) {

        return executeWithCallBack(slb.toString());
    }

    @Override
    public int autoAccess(SqlBuilder sb) {

        return prepareStatement(sb.toString());
    }

    @Override
    public void createTable(SqlTableBuilder stb) {

        prepareStatement(stb.toString());

    }

    @Override
    public ResultSet readAllData(String table) {

        return executeWithCallBack("SELECT * FROM " + table);

    }

    /**
     * 本着这里是提供容易方法的原则，用 and 而不是 like 因为有 SqlLikeBuilder 可以完成复杂的查询
     */
    @Override
    public ResultSet searchData(String table, String selectType, String value, int limit) {

        return executeWithCallBack("SELECT * FROM " + table + " WHERE " + selectType + " and '%" + value + "%' "
                + ( limit > 0 ? " LIMIT " + limit : "" ));

    }

    @Override
    public ResultSet searchData(String table, String selectType, String value) {

        return searchData(table, selectType, value, -1);

    }

    @SneakyThrows
    @Override
    public boolean hasData(String table, BuilderMap map) {

        ResultSet rs = executeWithCallBack(new SqlLikeBuilder(table).setMap(map).toString());

        return rs != null && rs.next();

    }

    @Override
    public int prepareStatement(String sql) {

        try {

            return this.session.execute(sql);

        } catch ( SQLException e ) {

            log.error("[数据库] [预备] 在执行指令的时候发生了错误: " + sql, e);

            return -1;

        }

    }

    @Override
    public ResultSet executeWithCallBack(String sql) {

        try {

            PreparedStatement ps = this.session.getConnection().prepareStatement(sql);

            ps.execute();

            return ps.getResultSet();

        } catch ( SQLException e ) {

            log.error("[数据库] [预备] 在执行指令的时候发生了错误: " + sql);

            e.printStackTrace();

            return null;

        }

    }

    @Override
    public boolean connect() {

        IProcessorConfig config = MysqlConfig.getInstance();

        try {

            dataSource.setUrl("jdbc:mysql://" + config.getIpAddress() + ":" + config.getPort()
                    + "/" + config.getDatabaseName() + ( config.getExtra() != null ? "?" + config.getExtra() : "" ));
            dataSource.setUsername(config.getUsername());
            dataSource.setPassword(config.getPassword());

            session = Session.create(dataSource);

            if ( !session.getConnection().isValid(1200) ) {

                throw new SQLException();

            }

            status = DataProcessorStatus.CONNECTED;

        } catch ( SQLException e ) {

            DAOProcessorConnectFailedEvent<Mysql> daoConnectFailedEvent = new DAOProcessorConnectFailedEvent<>(this, e);

            TFrame.tframe.callEvent(daoConnectFailedEvent);

            if ( !daoConnectFailedEvent.isCancelled() ) {

                log.error(dataSource.getUrl());

                e.printStackTrace();

            }

            status = DataProcessorStatus.FAILED_CONNECT;

            return false;

        }

        TFrame.tframe.callEvent(new DAOProcessorConnectedEvent<>(this));

        return true;
    }

    @SneakyThrows
    @Override
    public boolean disconnect() {

        status = DataProcessorStatus.DISCONNECTED;

        if ( this.session != null ) {

            DAOProcessorPreShutdownEvent<Mysql> daoProcessorPreShutdownEvent = new DAOProcessorPreShutdownEvent<>(this);

            TFrame.tframe.callEvent(daoProcessorPreShutdownEvent);

            if ( daoProcessorPreShutdownEvent.isCancelled() ) {

                return false;

            }

            log.warn("[数据库] MySQL数据库已停止服务!");

            this.session.close();
            this.dataSource.close();

        }

        return false;
    }

    @Override
    public DataProcessorStatus getStatus() {

        return status;
    }

    @Override
    public boolean checkStatus() {

        return getStatus(3000);
    }

    @SneakyThrows
    public boolean getStatus(int timeout) {

        return this.session.getConnection().isValid(timeout);
    }

    public void reConnect(int delay) {

        this.disconnect();

        ThreadUtil.execAsync(() -> {

            ThreadUtil.sleep(delay);

            connect();

        });

    }

    @Override
    public void reConnect() {

        reConnect(100);

    }

    @Override
    public boolean doCrash() {

        return true;
    }

}
