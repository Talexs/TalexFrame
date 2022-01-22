package com.talex.frame.talexframe.function.plugins.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author TalexDreamSoul
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PluginInfo {

    public enum PluginSupportVersion {

//        <4.0.0
        FOUR("4.0.0"),

//        <=4.0.0
        FOUR_PLUS("4.0.0a"),

//        5.0.0
        FIVE("5.0.0"),

//        5.0.0 - 5.1.0
        FIVE_PLUS("5.0.0a"),

//        5.2.0
        FIVE_PLUS_PRO("5.2.0a"),

//        5.x(>2).0
        FIVE_SNAPSHOT("5.2a.0"),

//        6.0.0-PV
        SIX_PREVIEW("6.0.0-pv"),

//        6.0.0
        SIX_NORMAL("6.0.0"),

//        6.0.0-alpha
        SIX_ALPHA("6.0.0-a"),

//        6.0.0-beta
        SIX_BETA("6.0.0-beta"),

//        7.0.0
        SEVEN("7.0.0");

        @Getter
        private final String version;

        PluginSupportVersion(String version) {

            this.version = version;

        }

    }

//    插件名
    private String pluginName;

//    插件作者
    private String pluginAuthor;

//    插件版本
    private String pluginVersion;

//    插件主类
    private String mainClassPath;

//    插件介绍
    private String description;

//    插件网页
    private String website;

//    插件支持的框架版本
    private PluginSupportVersion supportVersion;

}
