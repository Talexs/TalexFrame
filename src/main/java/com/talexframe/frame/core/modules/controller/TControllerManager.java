package com.talexframe.frame.core.modules.controller;

import com.talexframe.frame.core.modules.network.connection.RequestAnalyser;
import com.talexframe.frame.core.modules.network.interfaces.IUnRegisterHandler;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.modules.repository.TRepository;
import com.talexframe.frame.core.modules.repository.TRepositoryManager;
import com.talexframe.frame.core.pojo.annotations.TRepoInject;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <br /> {@link com.talexframe.frame.core.modules.controller Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 18:49 <br /> Project: TalexFrame <br />
 */
@Getter
public class TControllerManager {

    private static TControllerManager manager;
    private final ConcurrentMap<Class<?>, TController> controllers = new ConcurrentHashMap<>(32);
    private final ConcurrentMap<TController, String> controllerPluginMap = new ConcurrentHashMap<>(32);

    private TControllerManager() {


    }

    public static TControllerManager init() {

        if ( manager == null ) {
            manager = new TControllerManager();
        }

        return manager;

    }

    /**
     * 注册一个管理器
     *
     * @param plugin     管理器插件
     * @param controller 管理器
     *
     * @return 注册是否成功
     */
    @SneakyThrows
    public boolean registerController(WebPlugin plugin, TController controller) {

        if ( this.controllers.containsKey(controller.getClass()) ) {

            return false;

        }

        this.controllers.put(controller.getClass(), controller);
        this.controllerPluginMap.put(controller, plugin.getName());

        TRepositoryManager repoManager = TFrame.tframe.getRepositoryManager();

        /*

          扫描类中所有字段 带有 TRepInject 的字段，自动从 TRepositoryManager 中根据字段类型注入

         */
        for ( Field field : controller.getClass().getDeclaredFields() ) {

            TRepoInject repoInject = field.getAnnotation(TRepoInject.class);

            if ( repoInject != null ) {

                Class<?> repClz = field.getType();

                field.setAccessible(true);

                TRepository tRep = repoManager.getASRepositoryByClass(repClz);

                if ( tRep == null ) {

                    throw new NullPointerException("Inject repository with null - " + repClz.getName());

                }

                field.set(controller, tRep);

            }

        }

        /**
         *
         * 让 NetworkMananager 扫一下这个类
         *
         */
        RequestAnalyser.scanRequests(controller);

        return true;

    }

    /**
     * 注销一个储存库
     *
     * @param plugin     储存库插件
     * @param controller 管理器
     *
     * @return 注销是否成功
     */
    public boolean unRegisterController(WebPlugin plugin, TController controller) {

        if ( !this.controllers.containsKey(controller.getClass()) ) {

            return false;

        }

        if ( controller instanceof IUnRegisterHandler ) {

            ( (IUnRegisterHandler) controller ).onUnRegister();

        }

        RequestAnalyser.removeRequests(controller);

        this.controllers.remove(controller.getClass(), controller);
        this.controllerPluginMap.remove(controller, plugin.getName());

        return true;

    }

}
