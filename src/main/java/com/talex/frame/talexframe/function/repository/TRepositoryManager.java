package com.talex.frame.talexframe.function.repository;

import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * <br /> {@link com.talex.frame.talexframe.function.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 17:01 <br /> Project: TalexFrame <br />
 */
@Getter
public class TRepositoryManager {

    private final Map<Class<? extends TRepository>, TRepository> repositories = new HashMap<>();

    /**
     *
     * TableName -> RepositoryClass 映射
     *
     */
    private final Map<String, Class<? extends TRepository>> tableNameClzMap = new HashMap<>();
    private final Map<TRepository, String> repositoryPluginMap = new HashMap<>();

    private static TRepositoryManager manager;

    public static TRepositoryManager init() {

        if( manager == null ) manager = new TRepositoryManager();

        return manager;

    }

    private TRepositoryManager() {



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
