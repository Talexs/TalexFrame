package com.talex.talexframe.frame.core.modules.mysql;

import cn.hutool.core.thread.ThreadUtil;
import com.talex.talexframe.frame.core.modules.mysql.core.MysqlInfo;
import com.talex.talexframe.frame.core.pojo.builder.*;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.*;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.data.SqlDataBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.insert.SqlInsertBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.liker.SqlLikeBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.table.SqlTableBuilder;
import com.talex.talexframe.frame.core.pojo.dao.factory.mysql.builder.update.SqlUpdBuilder;
import com.talex.talexframe.frame.core.talex.TFrame;
import com.talex.talexframe.frame.core.modules.event.events.dao.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <br /> {@link com.talex.frame.talexframe.function.mysql Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/21 15:30 <br /> Project: TalexFrame <br />
 */
@Slf4j
public class MysqlManager {

    private Connection connection;

    private final TFrame talexs;

    private final MysqlInfo mysqlInfo;

    private final AtomicInteger counter = new AtomicInteger();

    public MysqlManager(TFrame tframe, MysqlInfo info) {

        this.talexs = tframe;
        this.mysqlInfo = info;

        log.info("[Module] MySql loaded!");

        connectMySQL();

    }

    @Override
    public void checkStatus() {

        DAOProcessorPreCheckStatusEvent mysqlPreStatusCheckEvent = new DAOProcessorPreCheckStatusEvent(this);
        talexs.callEvent(mysqlPreStatusCheckEvent);

        try {

            if (!this.connection.isValid(3000)) {

                if (this.counter.get() >= 3) {

                    log.error("[数据库] 审查器 # 数据库长时间不稳定,正在开始重启...");

                    reConnect();

                    this.counter.set(0);

                    return;

                }

                 log.error("[数据库] 审查器 # 当前数据库连接不稳定,波动较大,请注意重启服务器!");

                this.counter.incrementAndGet();

            }

        } catch ( SQLException e ) {

            e.printStackTrace();

        }

        DAOProcessorPostCheckStatusEvent mysqlStatusCheckEvent = new DAOProcessorPostCheckStatusEvent(this);
        talexs.callEvent(mysqlStatusCheckEvent);

    }

    @Override
    public void reConnect() {

        this.shutdown();

        ThreadUtil.execAsync(() -> {

            ThreadUtil.sleep(50);

            connectMySQL();

        });

    }

    @Override
    public boolean connectMySQL() {

        try {

            this.connection = DriverManager.getConnection(
                    "jdbc:dao://" + mysqlInfo.getIp() + ":" + mysqlInfo.getPort()
                            + "/" + mysqlInfo.getDatabaseName() + "?autoReconnect=true&serverTimezone=Asia/Shanghai&useSSL=" + mysqlInfo.isUseSSL()
                    , mysqlInfo.getUsername(), mysqlInfo.getPassword());

        } catch (SQLException e) {

            DAOProcessorConnectFailedEvent DAOProcessorConnectFailedEvent = new DAOProcessorConnectFailedEvent(this, e);

            talexs.callEvent(DAOProcessorConnectFailedEvent);

            if (!DAOProcessorConnectFailedEvent.isCancelled()) {

                e.printStackTrace();

            }

            return false;

        }

        DAOProcessorConnectedEvent mysqlConnectedEvent = new DAOProcessorConnectedEvent(this);
        talexs.callEvent(mysqlConnectedEvent);

        log.info("[Module] Mysql 已连接成功 !");

        return true;

    }

    @Override
    public boolean autoAccess(SqlBuilder sb) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(sb.toString());
            ps.execute();
            return true;
        }
        catch (SQLException throwable) {
            log.error("[数据库] [自动执行] 发生异常: " + throwable.getMessage() + " # 命令: " + sb);
            return false;
        }
    }

    @Override
    public ResultSet likeData(SqlLikeBuilder slb) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(slb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return ps.executeQuery();
        }
        catch (SQLException throwable) {
            log.error("[数据库] [搜索数据] 发生异常: " + throwable.getMessage() + " # " + slb);
            return null;
        }
    }

    @Override
    public boolean updateData(SqlUpdBuilder upd) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(upd.toString());
            return ps.execute();
        }
        catch (SQLException e) {
            log.error("[数据库] [更新数据] 发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addData(SqlInsertBuilder sab) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(sab.toString());
            return ps.execute();
        }
        catch (SQLException e) {
            log.error("[数据库] [添加数据] 发生异常: " + e.getMessage() + " # 命令: " + sab);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteData(String table, String type, String value) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(SqlCommand.DELETE_DATA.commandToString().replaceFirst("%table_name%", table.replace("--", "")).replaceFirst("%username%", type.replace("--", "")).replaceFirst("%value%", value));
            return ps.execute();
        }
        catch (SQLException e) {
            log.error("[数据库] [删除数据] 发生异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void joinTable(SqlTableBuilder stb) {
        String cmd = stb.toString();
        try {
            PreparedStatement ps = this.connection.prepareStatement(cmd);
            ps.executeUpdate();
        }
        catch (SQLException e) {
             log.error("[数据库] 在创建数据表的时候发生了异常,执行命令: " + cmd);
             log.info("[数据库] 错误信息: " + e.getMessage());
             log.info("[数据库] 快速查错连接: @" + e.getStackTrace()[0]);
        }
    }

    @Override
    public ResultSet readSearchAllData(String table) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(SqlCommand.SELECT_ALL_DATA.commandToString().replaceFirst("%table_name%", table), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return ps.executeQuery();
        }
        catch (SQLException e) {
             log.error("FindAllData # " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet readSearchData(String table, String selectType, String value, int limit) {
        String s = "NONE";
        try {
            PreparedStatement ps = this.connection.prepareStatement((SqlCommand.SELECT_DATA.commandToString() + " LIMIT " + limit).replaceFirst("%select_type%", selectType).replaceFirst("%table_name%", table).replaceFirst("%username%", value), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return ps.executeQuery();
        }
        catch (SQLException e) {
             log.error(e.getMessage() + " # Execute: " + s);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet readSearchData(String table, String selectType, String value) {
        String s = "NONE";
        try {
            PreparedStatement ps = this.connection.prepareStatement(SqlCommand.SELECT_DATA.commandToString().replaceFirst("%select_type%", selectType).replaceFirst("%table_name%", table).replaceFirst("%username%", value), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return ps.executeQuery();
        }
        catch (SQLException e) {
             log.error(e.getMessage() + " # Execute: " + s);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SneakyThrows
    public boolean hasData(String table, SqlDataBuilder.DataParam dataParam) {
        String cmd = "SELECT * FROM " + table + " WHERE " + dataParam.getSubParamName() + " = \"" + dataParam.getSubParamValue() + "\" LIMIT 1";
        ResultSet rs = this.connection.prepareStatement(cmd).executeQuery();
        return rs != null && rs.next();
    }

    @Override
    public void shutdown() {
        if (this.connection == null) {
            return;
        }
        DAOProcessorPreShutdownEvent daoProcessorPreShutdownEvent = new DAOProcessorPreShutdownEvent(this);
        talexs.callEvent(daoProcessorPreShutdownEvent);
        if ( daoProcessorPreShutdownEvent.isCancelled()) {
            return;
        }
        log.warn("[数据库] 数据库已停止服务!");
        try {
            this.connection.close();
        }
        catch ( SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public boolean getStatus(int timeout) {
        return this.connection.isValid(timeout);
    }

    @Override
    public boolean isServiceNull() {
        return this.connection == null;
    }

    @Override
    public boolean prepareStatement(String sql) {
        try {
            return this.connection.prepareStatement(sql).execute();
        }
        catch (SQLException e) {
             log.error("[数据库] [预备] 在执行指令的时候发生了错误: " + sql);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ResultSet executeWithCallBack(String sql) {
        try {
            return this.connection.prepareStatement(sql).executeQuery();
        }
        catch (SQLException e) {
             log.error("[数据库] [预备] 在执行指令的时候发生了错误: " + sql);
            e.printStackTrace();
            return null;
        }
    }

}
