package com.talex.frame.talexframe.function.plugins.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.talex.frame.talexframe.function.talex.FrameCreator;
import com.talex.frame.talexframe.function.talex.TFrame;
import com.talex.frame.talexframe.mapper.frame.FrameSender;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Getter
public abstract class WebPlugin extends FrameCreator implements IPlugin {

    protected final TFrame tframe = TFrame.tframe;
    protected FrameSender sender = TFrame.tframe.getFrameSender();
    protected final JarFile jarFile;

    protected final File pluginDataFolder;
    protected final File tempDataFolder;

    public WebPlugin(String pluginName, PluginInfo info) {

        super("PLUGIN", pluginName);

        this.name = pluginName;
        this.info = info;

        this.jarFile = tframe.getPluginManager().getPluginJarFiles().get(this.getName());

        this.pluginDataFolder = new File(tframe.getPluginManager().getPluginFolder() + "/" + pluginName);
        this.tempDataFolder = new File(this.pluginDataFolder + "/temp");

        if(!this.pluginDataFolder.exists()) {

            this.pluginDataFolder.mkdirs();

        }

        if(!this.tempDataFolder.exists()) {

            this.tempDataFolder.mkdirs();

        }

    }

    @SneakyThrows
    protected void output(String path) {

        String reference = pluginDataFolder + "/";
        FileUtil.mkdir( reference );

        Enumeration<JarEntry> entries = jarFile.entries();

        for( JarEntry entry = entries.nextElement(); entries.hasMoreElements(); entry = entries.nextElement() ) {

            if( entry.getName().startsWith(path) ) {

                if( entry.isDirectory() ) {

                    File file = new File( reference + "/" + entry.getName() );

                    if( !file.exists() ) {

                        file.mkdirs();

                        tframe.getFrameSender().sendConsoleMessage("[PluginInfo] [" + getName() + "] 已创建文件夹: " + entry.getName());

                    }

                    continue;

                }

                FileOutputStream os = new FileOutputStream(reference + "/" + entry.getName());

                IoUtil.write(os, true, IoUtil.readBytes(jarFile.getInputStream(entry)));

                tframe.getFrameSender().sendConsoleMessage("[PluginInfo] [" + getName() + "] 已写入文件: " + entry.getName());

            }

        }

    }

    private final String name;

    @Setter
    private PluginInfo info;

}
