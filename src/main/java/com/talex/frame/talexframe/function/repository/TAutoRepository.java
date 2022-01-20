package com.talex.frame.talexframe.function.repository;

import com.talex.frame.talexframe.dao.MajorDAO;
import com.talex.frame.talexframe.function.auto.data.AutoSaveData;
import com.talex.frame.talexframe.function.event.FrameListener;
import com.talex.frame.talexframe.function.event.TalexSubscribe;
import com.talex.frame.talexframe.function.event.events.frame.FrameFirstInstallEvent;
import com.talex.frame.talexframe.function.event.events.frame.FrameMajorDAOInitiatedEvent;
import com.talex.frame.talexframe.function.event.events.frame.FramePreUnInstallEvent;
import com.talex.frame.talexframe.function.plugins.addon.FramePluginListener;
import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.pojo.annotations.TAutoSave;
import com.talex.frame.talexframe.pojo.builder.SqlTableBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * 自动化存储库 # 自动存储数据
 * <br /> {@link com.talex.frame.talexframe.function.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:03 <br /> Project: TalexFrame <br />
 */
@Getter
public class TAutoRepository<T extends AutoSaveData> extends TRepository {

    private List<T> data;

    protected final TFrame tframe = TFrame.tframe;
    protected final WebPlugin ownPlugin;
    protected final String tableName;
    protected final Class<?> templateData;

    /**
     *
     * 当预估自己的数据过大时请调整!
     *
     */
    @Setter
    protected String infoType = "VARCHAR(512)";

    public TAutoRepository(String tableName, WebPlugin webPlugin) {

        super("AS_" + webPlugin.getName());

        this.ownPlugin = webPlugin;
        this.tableName = tableName;
        this.templateData = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        tframe.registerEvent(new RepositorySubEventListener(webPlugin));

    }

    private class RepositorySubEventListener extends FramePluginListener {

        public RepositorySubEventListener(WebPlugin provider) {

            super(provider);
        }

        @TalexSubscribe( once = true )
        public void onDaoInit(FrameMajorDAOInitiatedEvent event) {

            MajorDAO dao = event.getMajorDAO();

            data = (List<T>) dao.formAllAutoSaveData(tableName);

        }

        @TalexSubscribe
        public void onUninstall(FramePreUnInstallEvent event) {

            tframe.getMajorDao().saveAllAutoSaveData(tableName, (Collection<AutoSaveData>) data);

        }

        @TalexSubscribe( once = true )
        public void onFirstInstall(FrameFirstInstallEvent event) {

            JdbcTemplate jdbcTemplate = tframe.getMajorDao().getJdbcTemplate();

            SqlTableBuilder sqlTableBuilder = new SqlTableBuilder();

            for( Field field : templateData.getDeclaredFields()) {

                TAutoSave as = field.getAnnotation(TAutoSave.class);

                if(as == null || !as.isMySqlFiled()) { continue; }

                if( "info".equals(field.getName()) ) {

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

            jdbcTemplate.execute(sqlTableBuilder

                    .addTableParam(new SqlTableBuilder.TableParam()
                            .setDefaultNull(null).setType(infoType).setSubParamName("as_info").setMain(false)).toString());

        }

    }

}
