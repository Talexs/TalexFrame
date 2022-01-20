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

    private final Map<String, TRepository> repositories = new HashMap<>();
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

        this.repositories.put(repository.getProvider(), repository);
        this.repositoryPluginMap.put(repository, plugin.getName());

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

        this.repositories.remove(repository.getProvider(), repository);
        this.repositoryPluginMap.remove(repository, plugin.getName());

        return true;

    }

}
