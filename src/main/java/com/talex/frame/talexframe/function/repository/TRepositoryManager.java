package com.talex.frame.talexframe.function.repository;

import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import com.talex.frame.talexframe.imple.IUnRegisterHandler;
import com.talex.frame.talexframe.pojo.annotations.TRepoInject;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <br /> {@link com.talex.frame.talexframe.function.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:01 <br /> Project: TalexFrame <br />
 */
@Getter
public class TRepositoryManager {

    private final ConcurrentMap<Class<? extends TRepository>, TRepository> repositories = new ConcurrentHashMap<>();

    /**
     *
     * TableName -> RepositoryClass 映射
     *
     */
    private final ConcurrentMap<String, Class<? extends TRepository>> tableNameClzMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<TRepository, String> repositoryPluginMap = new ConcurrentHashMap<>();

    private static TRepositoryManager manager;

    public static TRepositoryManager init() {

        if( manager == null ) manager = new TRepositoryManager();

        return manager;

    }

    private TRepositoryManager() {



    }

    public TRepository getRepositoryByTableName(String tableName) {

        return getRepositoryByClass(tableNameClzMap.get(tableName));

    }

    public TRepository getRepositoryByClass(Class<?> clz) {

        return repositories.get(clz);

    }

    public TAutoRepository<?> getASRepositoryByTableName(String tableName) {

        return (TAutoRepository<?>) getRepositoryByClass(tableNameClzMap.get(tableName));

    }

    public TAutoRepository<?> getASRepositoryByClass(Class<?> clz) {

        return (TAutoRepository<?>) repositories.get(clz);

    }

    /**
     *
     * 注册一个储存库
     *
     * @param plugin 储存库插件
     * @param repository 储存库
     *
     * @return 注册是否成功
     */
    @SneakyThrows
    public boolean registerRepository(WebPlugin plugin, TRepository repository) {

        if( this.repositories.containsKey(repository.getClass()) ) {

            return false;

        }

        this.tableNameClzMap.put(repository.getProvider(), repository.getClass());
        this.repositories.put(repository.getClass(), repository);
        this.repositoryPluginMap.put(repository, plugin.getName());

        if( repository instanceof TAutoRepository ) {

            ( (TAutoRepository<?>) repository ).onInstall();

        }

        Class<?> clz = repository.getClass();

        /* 扫描类中所有字段 带有 TRepInject 的字段，自动从 TRepositoryManager 中根据字段类型注入 **/
        for( Field field : clz.getDeclaredFields() ) {

            TRepoInject repoInject = field.getAnnotation(TRepoInject.class);

            if( repoInject != null ) {

                Class<?> repClz = field.getType();

                field.setAccessible(true);

                TRepository tRep = getASRepositoryByClass(repClz);

                if( tRep == null ) {

                    throw new NullPointerException("Inject repository with null - " + repClz.getName());

                }

                field.set(repository, tRep);

            }

        }

        return true;

    }

    /**
     *
     * 注销一个储存库
     *
     * @param plugin 储存库插件
     * @param repository 储存库
     *
     * @return 注销是否成功
     */
    public boolean unRegisterRepository(WebPlugin plugin, TRepository repository) {

        if( !this.repositories.containsKey(repository.getClass()) ) {

            return false;

        }

        if( repository instanceof TAutoRepository ) {

            ((TAutoRepository<?>) repository).saveAllDataToMysql();

        }

        if( repository instanceof IUnRegisterHandler ) {

            ((IUnRegisterHandler) repository).onUnRegister();

        }

        this.tableNameClzMap.remove(repository.getProvider(), repository.getClass());
        this.repositories.remove(repository.getClass(), repository);
        this.repositoryPluginMap.remove(repository, plugin.getName());

        return true;

    }

}
