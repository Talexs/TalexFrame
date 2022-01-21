package com.talex.frame.talexframe.dao;

import lombok.extern.slf4j.Slf4j;

/**
 * 封装 SpringBoot 事务到框架 -> 插件
 * <br /> {@link com.talex.frame.talexframe.dao Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 14:46 <br /> Project: TalexFrame <br />
 */
// @Component
@Slf4j
@Deprecated
public class MajorDAO {

    public static MajorDAO instance;

    // @Getter
    // @Autowired
    // private JdbcTemplate jdbcTemplate;
    //
    // @Getter
    // @Autowired
    // private DataSource dataSource;

    // @Getter
    // @Resource
    // private EntityManagerFactory entityManagerFactory;

    public MajorDAO() {

        instance = this;

    }
    //
    // /**
    //  *
    //  * 将数据表中的 Row 转换为一个类(字段相符合)
    //  *
    //  * @param table 要转换的表名
    //  * @param clz 相对应的类
    //  *
    //  */
    // public List<?> formAllData(String table, Class<?> clz) {
    //
    //     return jdbcTemplate.query("select * from " + table, new BeanPropertyRowMapper<>(clz));
    //
    // }
    //
    // /**
    //  *
    //  * 将数据表中的 Row 转换为 {@link AutoSaveData}
    //  *
    //  * @param table 要转换的表名
    //  *
    //  */
    // public List<AutoSaveData> formAllAutoSaveData(String table, Class<? extends AutoSaveData> clz) {
    //
    //     return jdbcTemplate.query("select * from " + table, rs -> {
    //
    //         List<AutoSaveData> list = new ArrayList<>();
    //
    //         while( rs.next() ) {
    //
    //             String str = rs.getString("as_info");
    //
    //             if( StrUtil.isBlankIfStr(str) ) continue;
    //
    //             str = Base64.decodeStr(str);
    //
    //             WrappedData<?> as = AutoSaveData.deserialize(clz, JSONUtil.parseObj(str));
    //
    //             list.add( as.getValue() );
    //
    //             log.debug(" -> 数据加载: " + as.getValue().getMainKey());
    //
    //         }
    //
    //         return list;
    //     });
    //
    // }
    //
    // /**
    //  *
    //  * 将 {@link AutoSaveData} 保存到数据库
    //  *
    //  * @param table 要保存的表名
    //  * @param data 数据实例
    //  *
    //  */
    // @SneakyThrows
    // public void saveAutoSaveData(String table, AutoSaveData data) {
    //
    //     Class<? extends AutoSaveData> clz = data.getClass();
    //
    //     StringBuilder keys = new StringBuilder();
    //     StringBuilder values = new StringBuilder();
    //     StringBuilder update = new StringBuilder();
    //
    //     for( Field field : clz.getDeclaredFields() ) {
    //
    //         TAutoSave as = field.getAnnotation(TAutoSave.class);
    //
    //         if(as == null) { continue; }
    //
    //         keys.append("as_").append(field.getName()).append(", ");
    //
    //         if( as.update() ) {
    //
    //             String ts = String.valueOf(System.currentTimeMillis());
    //
    //             values.append("\"").append(ts).append("\", ");
    //             update.append("as_").append(field.getName()).append("=\"").append(ts).append("\", ");
    //
    //         } else {
    //
    //             Object obj = field.get(data);
    //             if( field.getType() == String.class ) {
    //
    //                 values.append("\"").append(obj).append("\", ");
    //                 update.append("as_").append(field.getName()).append("=\"").append(obj).append("\", ");
    //
    //             } else {
    //
    //                 values.append(obj).append(", ");
    //                 update.append("as_").append(field.getName()).append("=").append(obj).append(", ");
    //
    //             }
    //
    //         }
    //
    //     }
    //
    //     String encStr = Base64.encode(data.toJSONObject().toString());
    //
    //     keys.append("as_total_info");
    //     values.append(encStr);
    //     update.append("as_total_info=\"").append(encStr).append("\"");
    //
    //     String sql = "INSERT INTO " + table + "(" + keys + ") VALUES(" +
    //             values + ") ON DUPLICATE KEY UPDATE " + update.substring(0, update.length() - 2);
    //
    //     TFrame.tframe.getMysqlManager().prepareStatement(sql);
    //
    // }
    //
    // public void saveAllAutoSaveData(String table, Collection<AutoSaveData> datas) {
    //
    //     datas.forEach(data -> saveAutoSaveData(table, data));
    //
    // }

}
