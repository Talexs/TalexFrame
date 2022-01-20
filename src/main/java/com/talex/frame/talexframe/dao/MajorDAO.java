package com.talex.frame.talexframe.dao;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import com.talex.frame.talexframe.pojo.annotations.TAutoSave;
import com.talex.frame.talexframe.function.auto.data.AutoSaveData;
import com.talex.frame.talexframe.function.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * 封装 SpringBoot 事务到框架 -> 插件
 * <br /> {@link com.talex.frame.talexframe.dao Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 14:46 <br /> Project: TalexFrame <br />
 */
@Component
public class MajorDAO {

    @Getter
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MajorDAO() {

        MajorDAO instance = this;

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while( true ) {

                    if( TFrame.tframe != null ) {

                        TFrame.tframe.setMajorDAO(instance);

                        break;

                    }

                }

            }

        });

        thread.start();

    }

    /**
     *
     * 将数据表中的 Row 转换为一个类(字段相符合)
     *
     * @param table 要转换的表名
     * @param clz 相对应的类
     *
     */
    public List<?> formAllData(String table, Class<?> clz) {

        return jdbcTemplate.query("select * from " + table, new BeanPropertyRowMapper<>(clz));

    }

    /**
     *
     * 将数据表中的 Row 转换为 {@link AutoSaveData}
     *
     * @param table 要转换的表名
     *
     */
    public List<AutoSaveData> formAllAutoSaveData(String table) {

        return jdbcTemplate.query("select * from " + table, new ResultSetExtractor<List<AutoSaveData>>() {

            @Override
            public List<AutoSaveData> extractData(ResultSet rs) throws SQLException, DataAccessException {

                return null;
            }
        });

    }

    /**
     *
     * 将 {@link AutoSaveData} 保存到数据库
     *
     * @param table 要保存的表名
     * @param data 数据实例
     *
     */
    @SneakyThrows
    public void saveAutoSaveData(String table, AutoSaveData data) {

        Class<? extends AutoSaveData> clz = data.getClass();
        JSONObject json = new JSONObject();

        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        StringBuilder update = new StringBuilder();

        for( Field field : clz.getDeclaredFields() ) {

            TAutoSave as = field.getAnnotation(TAutoSave.class);

            if(as == null) { continue; }

            keys.append("as_").append(field.getName()).append(", ");

            if( as.update() ) {

                String ts = String.valueOf(System.currentTimeMillis());

                values.append("\"").append(ts).append("\", ");
                update.append("as_").append(field.getName()).append("=\"").append(ts).append("\", ");

            } else {

                Object obj = field.get(data);
                if( field.getType() == String.class ) {

                    values.append("\"").append(obj).append("\", ");
                    update.append("as_").append(field.getName()).append("=\"").append(obj).append("\", ");

                } else {

                    values.append(obj).append(", ");
                    update.append("as_").append(field.getName()).append("=").append(obj).append(", ");

                }

            }

        }

        String encStr = Base64.encode(data.toJSONObject().toString());

        keys.append("as_total_info");
        values.append(encStr);
        update.append("as_total_info=\"").append(encStr).append("\"");

        String sql = "INSERT INTO " + table + "(" + keys + ") VALUES(" +
                values + ") ON DUPLICATE KEY UPDATE " + update.substring(0, update.length() - 2);

        jdbcTemplate.execute(sql);

    }

    public void saveAllAutoSaveData(String table, Collection<AutoSaveData> datas) {

        datas.forEach(data -> saveAutoSaveData(table, data));

    }

}
