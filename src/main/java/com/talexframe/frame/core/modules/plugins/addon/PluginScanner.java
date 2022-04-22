package com.talexframe.frame.core.modules.plugins.addon;

import cn.hutool.core.util.ClassUtil;
import com.talexframe.frame.core.modules.plugins.adapt.PluginCompAdapter;
import com.talexframe.frame.core.modules.plugins.core.PluginManager;
import com.talexframe.frame.core.modules.plugins.core.WebPlugin;
import com.talexframe.frame.core.talex.FrameCreator;
import com.talexframe.frame.core.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * {@link com.talexframe.frame.core.modules.plugins.addon Package }
 *
 * @author TalexDreamSoul 22/04/02 下午 07:53 Project: TalexFrame
 */
@Slf4j
public class PluginScanner extends FrameCreator {

    private final WebPlugin plugin;

    private static final TFrame tframe = TFrame.tframe;

    private static LinkedHashSet<PluginCompAdapter<?>> adapters = new LinkedHashSet<>();

    private static boolean sorted = false;

    private final PluginManager pluginManager = tframe.getPluginManager();

    @Getter
    private Set<Runnable> runnables = new HashSet<>();

    public void pushService( Runnable runnable ) {

        runnables.add( runnable );

    }

    public PluginScanner(WebPlugin plugin) {

        super("PluginScanner", plugin.getName());

        this.plugin = plugin;

    }

    public static void adapt(PluginCompAdapter<?> adapter) {

        if( adapters.contains(adapter) ) throw new RuntimeException("PluginCompAdapter already exists");

        adapters.add( adapter );

        sorted = false;

    }

    private Set<PluginCompAdapter<?>> getSortedList() {

        if( sorted ) return adapters;

        adapters = adapters.stream().sorted((o1, o2) -> o2.getPriority() - o1.getPriority()).collect(Collectors.toCollection(LinkedHashSet::new));

        sorted = true;

        log.debug("PluginCompAdapters sorted: {}", adapters);

        return new LinkedHashSet<>(adapters);

    }

    @SneakyThrows
    public void scan() {

        JarFile jarFile = pluginManager.getPluginJarFiles().get( plugin.getName() );

        URLClassLoader classLoader = pluginManager.getPluginClassLoaders().get( plugin.getName() );

        Set<Class<?>> classes = new HashSet<>();

        log.debug("Scanning plugin: {}", plugin.getName());

        for ( Enumeration<JarEntry> en = jarFile.entries(); en.hasMoreElements(); ) {

            JarEntry jarEntry = en.nextElement();

            if( jarEntry.getName().endsWith(".class") ) {

                String clzName = jarEntry.getName().replace("/", ".").substring(0, jarEntry.getName().length() - 6);

                log.debug("Scanning class: {}", clzName);

                Class<?> clz = classLoader.loadClass( clzName );

                if( FrameCreator.class.isAssignableFrom(clz) ) {

                    classes.add(clz);

                }

            }

        }

        log.debug("Found {} classes", classes.size());

        for( PluginCompAdapter<?> adapter : getSortedList() ) {

            log.debug("--> PluginCompAdapter: {}", adapter.getClass().getName());

            for (Class<?> clazz : classes) {

                if( !adapter.shouldAccess((Class<? extends FrameCreator>) clazz) ) continue;

                log.debug("# --> PluginCompAdapter: {}", clazz.getName());

                Constructor<?>[] constructors = clazz.getConstructors();

                if( constructors.length != 1 ) {

                    log.warn("[PluginScanner] PluginCompAdapter " + clazz.getName() + " has a wrong constructor");

                    continue;

                }

                try {

                    if( !adapter.inject( this, this.plugin, constructors[0].newInstance() ) ) {

                        log.warn("[PluginScanner] PluginCompAdapter " + clazz.getName() + " inject failed");

                    }

                } catch ( InvocationTargetException e ) {

                    log.error("[PluginScanner] PluginCompAdapter inject failed | For constructors: {}", (Object) constructors);

                    tframe.crash( e.getCause() );

                }

            }

        }

        runnables.forEach( Runnable::run );

    }

    public void logout() {

        Set<Class<?>> classes = ClassUtil.scanPackage(this.plugin.getClass().getPackage().getName(), FrameCreator.class::isAssignableFrom);

        for( PluginCompAdapter<?> adapter : getSortedList() ) {

            for (Class<?> clazz : classes) {

                if( !adapter.shouldAccess((Class<? extends FrameCreator>) clazz) ) continue;

                if( !adapter.logoutInstance(this, this.plugin, (Class<? extends FrameCreator>) clazz) ) {

                    TFrame.tframe.crash(new RuntimeException("PluginCompAdapter logout failed"));

                    return;

                }

            }

        }

    }

}