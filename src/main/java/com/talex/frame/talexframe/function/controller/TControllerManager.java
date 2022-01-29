package com.talex.frame.talexframe.function.controller;

import com.google.common.util.concurrent.RateLimiter;
import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import com.talex.frame.talexframe.function.repository.TRepository;
import com.talex.frame.talexframe.function.repository.TRepositoryManager;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.imple.IUnRegisterHandler;
import com.talex.frame.talexframe.pojo.annotations.TRepoInject;
import com.talex.frame.talexframe.pojo.annotations.TRequestLimit;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <br /> {@link com.talex.frame.talexframe.function.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 18:49 <br /> Project: TalexFrame <br />
 */
@Getter
public class TControllerManager {

    private final ConcurrentMap<Class<?>, TController> controllers = new ConcurrentHashMap<>(32);
    private final ConcurrentMap<TController, String> controllerPluginMap = new ConcurrentHashMap<>(32);

    private static TControllerManager manager;

    public static TControllerManager init() {

        if( manager == null ) manager = new TControllerManager();

        return manager;

    }

    private TControllerManager() {



    }

    /**
     *
     * 注册一个管理器
     *
     * @param plugin 管理器插件
     * @param controller 管理器
     *
     * @return 注册是否成功
     */
    @SneakyThrows
    public boolean registerController(WebPlugin plugin, TController controller) {

        if( this.controllers.containsKey(controller.getClass()) ) {

            return false;

        }

        this.controllers.put(controller.getClass(), controller);
        this.controllerPluginMap.put(controller, plugin.getName());

        Class<?> clz = controller.getClass();

        TRequestLimit clzLimit = clz.getAnnotation(TRequestLimit.class);

        if( clzLimit != null ) {

            RateLimiter limiter = RateLimiter.create(clzLimit.QPS(), clzLimit.timeout(), clzLimit.timeUnit());

            TFrame.tframe.getRateLimiterManager().getClassLimiterMapper().put(clz, limiter);

        }

        for( Method method : clz.getMethods() ) {

            TRequestLimit methodLimit = method.getAnnotation(TRequestLimit.class);

            if( methodLimit != null ) {

                RateLimiter limiter = RateLimiter.create(methodLimit.QPS(), methodLimit.timeout(), methodLimit.timeUnit());

                TFrame.tframe.getRateLimiterManager().getMethodLimiterMapper().put(method, limiter);

            }

        }

        TRepositoryManager repoManager = TFrame.tframe.getRepositoryManager();

        /** 扫描类中所有字段 带有 TRepInject 的字段，自动从 TRepositoryManager 中根据字段类型注入 **/
        for( Field field : clz.getDeclaredFields() ) {

            TRepoInject repoInject = field.getAnnotation(TRepoInject.class);

            if( repoInject != null ) {

                Class<?> repClz = field.getType();

                field.setAccessible(true);

                TRepository tRep = repoManager.getASRepositoryByClass(repClz);

                if( tRep == null ) {

                    throw new NullPointerException("Inject repository with null - " + repClz.getName());

                }

                field.set(controller, tRep);

            }

        }

        return true;

    }

    /**
     *
     * 注销一个储存库
     *
     * @param plugin 储存库插件
     * @param controller 管理器
     *
     * @return 注销是否成功
     */
    public boolean unRegisterController(WebPlugin plugin, TController controller) {

        if( !this.controllers.containsKey(controller.getClass()) ) {

            return false;

        }

        if( controller instanceof IUnRegisterHandler ) {

            ((IUnRegisterHandler) controller).onUnRegister();

        }

        this.controllers.remove(controller.getClass(), controller);
        this.controllerPluginMap.remove(controller, plugin.getName());

        Class<?> clz = controller.getClass();

        TFrame.tframe.getRateLimiterManager().getClassLimiterMapper().remove(clz);

        for( Method method : clz.getMethods() ) {

            TFrame.tframe.getRateLimiterManager().getMethodLimiterMapper().remove(method);

        }

        return true;

    }

}
