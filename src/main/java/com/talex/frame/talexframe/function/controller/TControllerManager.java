package com.talex.frame.talexframe.function.controller;

import com.talex.frame.talexframe.function.plugins.core.WebPlugin;
import com.talex.frame.talexframe.function.repository.TAutoRepository;
import com.talex.frame.talexframe.function.repository.TRepository;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * <br /> {@link com.talex.frame.talexframe.function.repository Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/20 18:49 <br /> Project: TalexFrame <br />
 */
@Getter
public class TControllerManager {

    private final Map<String, TController> controllers = new HashMap<>();
    private final Map<TController, String> controllerPluginMap = new HashMap<>();

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
     * @param repository 管理器
     *
     * @return 注册是否成功
     */
    public boolean registerController(WebPlugin plugin, TController controller) {

        if( this.controllers.containsKey(controller.getProvider()) ) {

            return false;

        }

        this.controllers.put(controller.getProvider(), controller);
        this.controllerPluginMap.put(controller, plugin.getName());

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
    public boolean unRegisterController(WebPlugin plugin, TController controller) {

        if( !this.controllers.containsKey(controller.getProvider()) ) {

            return false;

        }

        this.controllers.remove(controller.getProvider(), controller);
        this.controllerPluginMap.remove(controller, plugin.getName());

        return true;

    }

}
