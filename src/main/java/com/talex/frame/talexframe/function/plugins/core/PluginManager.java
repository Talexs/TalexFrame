package com.talex.frame.talexframe.function.plugins.core;

import cn.hutool.core.io.IoUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.talex.frame.talexframe.TalexFrameApplication;
import com.talex.frame.talexframe.function.controller.TController;
import com.talex.frame.talexframe.function.controller.TControllerManager;
import com.talex.frame.talexframe.function.event.events.frame.FrameFirstInstallEvent;
import com.talex.frame.talexframe.function.repository.TRepository;
import com.talex.frame.talexframe.function.repository.TRepositoryManager;
import com.talex.frame.talexframe.function.talex.TFrame;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author TalexDreamSoul
 */
@Slf4j
public class PluginManager {

    @Getter
    private final File pluginFolder;

    @Getter
    private final HashMap<String, WebPlugin> pluginHashMap = new HashMap<>();

    @Getter
    private final HashMap<String, String> nameClassIndex = new HashMap<>();

    @Getter
    private final HashMap<String, JarFile> pluginJarFiles = new HashMap<>();

    @Getter
    private final HashMap<String, URLClassLoader> pluginClassLoaders = new HashMap<>();

    @SneakyThrows
    public PluginManager(File mainFolder){

        if( mainFolder == null ) {

            throw new NullPointerException("文件为空");

        }

        this.pluginFolder = mainFolder;

        if( !this.pluginFolder.exists() ) {  this.pluginFolder.mkdir();
            TFrame.tframe.callEvent(new FrameFirstInstallEvent(System.nanoTime() - TalexFrameApplication.startedTimeStamp)); }

    }

    public void loadAllPluginsInFolder() {

        String[] list = this.pluginFolder.list();

        if( list != null ) {

            for( String targetFile : Objects.requireNonNull(list) ) {

                loadPlugin(targetFile);

            }

        }

        log.info("[Module] Plugin loaded!");

    }

    @SneakyThrows
    private PluginInfo getPluginMainClassPathFromPluginName(String pluginName){

        JarFile jarFile = new JarFile(this.pluginFolder + "/" + pluginName + ".jar");

        this.pluginJarFiles.put(pluginName, jarFile);

        for (Enumeration<JarEntry> en = jarFile.entries(); en.hasMoreElements(); ) {

            JarEntry jarEntry = en.nextElement();
            if(jarEntry.getName().contains("plugin.yml")) {

                InputStream is = jarFile.getInputStream(jarEntry);

                String a = IoUtil.read(is, Charset.defaultCharset());

                jarFile.close();

                try {

                    JsonObject json = JsonParser.parseString(a).getAsJsonObject();

                    return new PluginInfo()
                            .setPluginAuthor(json.get("author").getAsString())
                            .setMainClassPath(json.get("mainClass").getAsString())
                            .setDescription(json.get("description").getAsString())
                            .setPluginName(json.get("name").getAsString())
                            .setPluginVersion(json.get("version").getAsString())
                            .setWebsite(json.get("website").getAsString())
                            .setSupportVersion(PluginInfo.PluginSupportVersion.valueOf(json.get("supportVersion").getAsString()));

                } catch (JsonSyntaxException e) {

                    throw new RuntimeException("Load web plugin error due to load " + pluginName);

                }

            }

        }

        jarFile.close();
        log.error("[插件] 无法搜索到主类地址,插件加载失败!");

        return null;

    }

    public void loadPlugin(String pluginName){

        if(pluginHashMap.containsKey(pluginName)) {

            log.warn("[插件] " + pluginName + " 早已加载!");
            return;

        }

        URL[] urls = getUrls(this.pluginFolder);

        URLClassLoader urlClassLoader = new URLClassLoader(urls, this.getClass().getClassLoader());

        this.pluginClassLoaders.put(pluginName, urlClassLoader);

        WebPlugin webPlugin = getPluginInstance(pluginName, Objects.requireNonNull(getPluginMainClassPathFromPluginName(pluginName)));

        this.pluginHashMap.put(pluginName, webPlugin);

        log.info("[插件] " + webPlugin.getName() + " v" + webPlugin.getInfo().getPluginVersion() + " 已加载完毕!");

    }

    private URL[] getUrls(File dir) {

        List<URL> results = new ArrayList<>();

        try {

            Files.newDirectoryStream(dir.toPath(), "*.jar")
                    .forEach(path -> results.add(getUrl(path)));

        } catch (IOException e) {

            e.printStackTrace();

        }

        return results.toArray(new URL[0]);

    }

    private URL getUrl(Path path) {

        try {

            return path.toUri().toURL();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e.getMessage(), e);

        }

    }

    @SneakyThrows
    public boolean unloadPlugin(String pluginName){

        if(!pluginHashMap.containsKey(pluginName)) {

            log.warn("[插件] 未从插件列表中找到: " + pluginName);
            return false;

        }

        WebPlugin plugin = this.pluginHashMap.get(pluginName);

        if(plugin == null) {
            return false;
        }

        String classPath = this.nameClassIndex.get(pluginName);

        if(classPath == null) {

            log.error("[插件] 无法获取到插件主类: " + pluginName);
            return false;

        }

        classPath = classPath.replace("." + pluginName, "");

        URLClassLoader urlClassLoader = this.pluginClassLoaders.get(pluginName);

        if(urlClassLoader == null) {

            log.error("[插件] 无法获取到插件URLClassLoader: " + pluginName);
            return false;

        }

        JarFile jarFile = this.pluginJarFiles.get(pluginName);

        if(jarFile == null) {

            log.error("[插件] 无法获取到插件JarFile: " + pluginName);
            return false;

        }

//        HttpRequestManager.getInstance().releasePlugin(plugin);

        plugin.onDisable();

        int i = 0;

        TControllerManager controllerManager = TFrame.tframe.getControllerManager();

        for( Map.Entry<TController, String> entry : controllerManager.getControllerPluginMap().entrySet() ) {

            if( entry.getValue().equalsIgnoreCase(pluginName) ) {

                controllerManager.unRegisterController(plugin, entry.getKey());

                ++i;

            }

        }

        if( i != 0)
            log.info("[Plugin] 已自动注销 " + i + " 个控制器类.");

        i = 0;

        TRepositoryManager repositoryManager = TFrame.tframe.getRepositoryManager();

        for( Map.Entry<TRepository, String> entry : repositoryManager.getRepositoryPluginMap().entrySet() ) {

            if( entry.getValue().equalsIgnoreCase(pluginName) ) {

                repositoryManager.unRegisterRepository(plugin, entry.getKey());

                ++i;

            }

        }

        if( i != 0)
            log.info("[Plugin] 已自动注销 " + i + " 个储存器类.");

        urlClassLoader.setPackageAssertionStatus(classPath,false);

        urlClassLoader.close();
        jarFile.close();

        this.pluginJarFiles.remove(pluginName);
        this.pluginHashMap.remove(pluginName);
        this.nameClassIndex.remove(pluginName);

        return true;

    }

    @SneakyThrows
    private WebPlugin getPluginInstance(String pluginName, PluginInfo info){

        String pluginMainClassPath = info.getMainClassPath();

        if(pluginMainClassPath == null) {

            throw new RuntimeException("MAIN CLASS ERROR - Loading plugin error! " + pluginName);

        }

        this.nameClassIndex.put(pluginName, pluginMainClassPath);

        URLClassLoader urlClassLoader = this.pluginClassLoaders.get(pluginName);

        if(urlClassLoader == null) {

            throw new NullPointerException("插件: " + pluginName);

        }

        Class<?> clazz = urlClassLoader.loadClass(pluginMainClassPath);

        Object instance = null;

        try {

            instance = clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {

            e.printStackTrace();

        }

        if(instance == null) {

            log.error("[插件] Instance error of " + pluginName);

            throw new RuntimeException("Instance error of " + pluginName);

        }

        if(!(instance instanceof WebPlugin )){

            log.error("[插件] 无法创建插件实例!");

            throw new RuntimeException("错误的插件: " + pluginMainClassPath);

        }

        WebPlugin wp = (WebPlugin) instance;

        wp.onEnable();

        return wp;

    }

}
