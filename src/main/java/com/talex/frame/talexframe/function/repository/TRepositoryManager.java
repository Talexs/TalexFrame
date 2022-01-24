package com.talex.frame.talexframe.function.repository;

import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import lombok.Getter;

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
    public boolean registerRepository(WebPlugin plugin, TRepository repository) {

        if( this.repositories.containsKey(repository.getProvider()) ) {

            return false;

        }

        this.tableNameClzMap.put(repository.getProvider(), repository.getClass());
        this.repositories.put(repository.getClass(), repository);
        this.repositoryPluginMap.put(repository, plugin.getName());

        if( repository instanceof TAutoRepository ) {

            ( (TAutoRepository<?>) repository ).onInstall();

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

        if( !this.repositories.containsKey(repository.getProvider()) ) {

            return false;

        }

        if( repository instanceof TAutoRepository ) {

            ((TAutoRepository<?>) repository).saveAllDataToMysql();

        }

        this.tableNameClzMap.remove(repository.getProvider(), repository.getClass());
        this.repositories.remove(repository.getClass(), repository);
        this.repositoryPluginMap.remove(repository, plugin.getName());

        return true;

    }

}
