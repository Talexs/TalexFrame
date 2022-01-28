package com.talex.frame.talexframe.function.repository;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.talex.frame.talexframe.function.auto.data.AutoSaveData;
import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.annotations.TAutoSave;
import com.talex.frame.talexframe.pojo.builder.SqlTableBuilder;
import com.talex.frame.talexframe.wrapper.WrappedData;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自动化存储库 # 自动存储数据
 * <br /> {@link com.talex.frame.talexframe.function.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:03 <br /> Project: TalexFrame <br />
 */
@Getter
@Slf4j
public class TAutoRepository<T extends AutoSaveData> extends TRepository {

    protected ConcurrentMap<String, T> dataMap;

    protected final WebPlugin ownPlugin;
    protected final Class<? extends AutoSaveData> templateData;

    /**
     *
     * 当预估自己的数据过大时请调整!
     *
     */
    @Setter
    protected String infoType = "VARCHAR(512)";

    public TAutoRepository(String tableName, WebPlugin webPlugin) {

        super(tableName);

        this.ownPlugin = webPlugin;
        this.templateData = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     *
     * 是否默认读取所有数据到 dataMap 中, 默认为假
     *
     */
    public boolean doCached() { return false; }

    public T getData(String identifyId) {
        if (identifyId == null) {
            return null;
        }
        return this.dataMap.get(identifyId);
    }

    public void delData(T data) {
        this.dataMap.remove(data.getMainKey(), data);
        this.onSingleDataRemoved(new WrappedData<T>(data));
    }

    public void onSingleDataRemoved(WrappedData<T> data) {
    }

    public void addData(T data) {
        this.dataMap.put(data.getMainKey(), new WrappedData<T>(data).getValue());
        this.onSingleDataLoaded(new WrappedData<T>(data));
    }

    /**
     *
     * @param data 数据实例
     *             默认 不cache 所以每次数据一更新就自动塞到数据库
     *
     * @return 返回真不放入数据map
     */
    public boolean onSingleDataLoaded(WrappedData<T> data) {

        saveAllDataToMysql();

        dataMap.clear();

        return false;
    }

    @SneakyThrows
    public void onInstall() {

        initTable();

        this.dataMap = new ConcurrentHashMap<>();

        if( !doCached() ) return;

        ResultSet rs = readSearchAllData();

        while ( rs != null && rs.next()) {
            WrappedData<T> data = (WrappedData<T>) AutoSaveData.deserialize(templateData, JSONUtil.parseObj(Base64.decodeStr(rs.getString("as_info"))));
            if (data.getValue() == null || ((AutoSaveData)data.getValue()).getMainKey() == null) {
                log.error("[AutoSaveData] FatalError!! # " + templateData.getName());
            }
            if (onSingleDataLoaded(data)) continue;
            dataMap.put((data.getValue()).getMainKey(), data.getValue());
        }

        onDataLoaded();

    }

    public void onUninstall() {

        saveAllDataToMysql();

    }

    @SneakyThrows
    public void saveAllDataToMysql() {

        if( dataMap == null ) return;

        for (AutoSaveData data : dataMap.values()) {

            Class<? extends AutoSaveData> clz = data.getClass();

            StringBuilder keys = new StringBuilder();
            StringBuilder values = new StringBuilder();
            StringBuilder update = new StringBuilder();

            for( Field field : clz.getDeclaredFields() ) {

                TAutoSave as = field.getAnnotation(TAutoSave.class);

                if(as == null || !as.isMySqlFiled()) { continue; }

                if( !field.isAccessible() ) field.setAccessible(true);

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

            keys.append("as_info");
            values.append("\"").append(encStr).append("\"");
            update.append("as_info=\"").append(encStr).append("\"");

            String sql = "INSERT INTO " + getProvider() + "(" + keys + ") VALUES(" +
                    values + ") ON DUPLICATE KEY UPDATE " + update;

            TFrame.tframe.getMysqlManager().prepareStatement(sql);

        }

        log.debug("[BaseAutoSaveData] " + getProvider() + " @" + getClass() + " 存储完毕!");

    }

    private void initTable() {

        SqlTableBuilder sqlTableBuilder = newSqlTableBuilder();

        for( Field field : templateData.getDeclaredFields()) {

            TAutoSave as = field.getAnnotation(TAutoSave.class);

            if(as == null || !as.isMySqlFiled()) { continue; }

            if( !StrUtil.isBlankIfStr(as.columnContent()) ) {

                sqlTableBuilder.addTableParam(new SqlTableBuilder.TableParam().setColumnContent(as.columnContent()));

            } else {

                if( "info".equalsIgnoreCase(field.getName()) ) {

                    throw new IllegalArgumentException("MysqlField name cannot be info, because it will recover data.");

                }

                if(!field.getType().isPrimitive() && field.getType() != String.class) {

                    throw new IllegalArgumentException("MysqlField type can only use primitive type (" + field.getType().getName() + ")");

                }

                String defaultNull = as.defaultNull();

                if( "null".equalsIgnoreCase(defaultNull)) {

                    defaultNull = null;

                } else if( "n_null".equalsIgnoreCase(defaultNull)) {

                    defaultNull = "null";

                }

                sqlTableBuilder.addTableParam(new SqlTableBuilder.TableParam()

                        .setSubParamName("as_" + field.getName())
                        .setType(as.type())
                        .setDefaultNull(defaultNull)
                        .setMain(as.isMain())

                );

            }

        }

        this.joinTable(sqlTableBuilder

                .addTableParam(new SqlTableBuilder.TableParam()
                        .setDefaultNull(null).setType(infoType).setSubParamName("as_info").setMain(false)));

    }

    public void onDataLoaded() {}

}
