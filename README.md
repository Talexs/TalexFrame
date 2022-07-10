# TalexFrame

简体中文 | [English](./.github/en/README.md)

```

/__  ___/                            //   / /                                   
   / /   ___     //  ___             //___   __      ___      _   __      ___    
  / /  //   ) ) // //___) ) \\ / /  / ___  //  ) ) //   ) ) // ) )  ) ) //___) ) 
 / /  //   / / // //         \/ /  //     //      //   / / // / /  / / //        
/ /  ((___( ( // ((____      / /\ //     //      ((___( ( // / /  / / ((____     
                                                                                   
```


     _____ ___   ___  
    |___  / _ \ / _ \ 
       / / | | | | | |
      / /| |_| | |_| |
     /_(_)\___(_)___/ 




## 项目背景

受到 Minecraft服务端 Bukkit及系列框架 启发，针对主体功能进行框架化完善，对从属功能使用 ModulePlugin 进行解耦化操作

## 项目介绍

一个简单的渐进式框架，为您提供类似 Bukkit 的开发体验

项目主要可用于开发 Web网页 也可用作游戏后台

## 可以做啥

通过 插件 实现功能的快速迭代 （也可以理解**微服务**）

通过命令与插件等实现快速的功能开发 避免重复启动 SpringBoot 的时间

使用本框架将无需等待各个模块的加载，数据库等的连接耗时

## 项目历史

曾是为网易我的世界HyCraft打造的后端，后来用于 Pubsher 目前用于 PVPIN Studio 的后台开发

目前，该项目也正在为 Kxord 项目进行保驾护航。

## 模块系统

我们使用诸如 apis, commands 的模块进行隔离

而我们所提供的基于模块的功能甚至能强于 SpringBoot 本身

## 插件系统

你可以通过使用任意基于Jvm的语言来开发插件，我们所推荐的有 Java和Kotlin。

无论如何，请一定记住不要使用 SpringBoot 的东西而是使用本身的东西，因为 SpringBoot 的东西在插件卸载后会残留，这是非常严重的问题。

框架提供的强大Api: FrameData, AutoSaveRepository ...

当然，你也可以使用我们的事件系统。详细的信息将在教程中提到

**当你不知所措时，尝试 TFrame 吧**

## 开发计划

正在逐步更新 **RPC框架模块 (Remote Procedure Call Module)**

## 墨菲安全

> 本项目由 MurphySec 进行安全保护。

[![OSCS Status](https://www.oscs1024.com/platform/badge/Talexs/TalexFrame.svg?size=large)](https://www.murphysec.com/dr/ok0G9SGTn0xsXi7Zex)

##  联系方式

通过QQ 2418876761 或者发送邮件到 TalexDreamSoul@gmail.com 或 2418876761@qq.com 以联系作者。

#### 当然 Issue 是被推荐的方式

