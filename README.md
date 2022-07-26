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

## 项目历史

曾是为《Minecraft》 HyCraft 所开发的后端，后用于 Pubsher 服务器系列开发。

目前用于 PVPIN Studio/KxordMarket/TalexWiki 的后台开发。

## 项目介绍

TalexFrame 简称 TF，是一款用于构建系统性后端的 Java 转换式框架，通过使用知名库的迁移，提供标准的封装模板和简易的开发流程，让您上手更迅速，更便捷。它基于标准的 SpringBoot Annotation 风格提供统一 API标准 开发。

## 模块系统

我们使用诸如 apis, commands 的模块进行隔离

而我们所提供的基于模块的功能甚至能强于 SpringBoot 本身

## 系统设计

系统大体分为 3 个核心模块，各模块功能分别为：

- Launcher: 框架整体的启动逻辑，包括环境判断等，引导到 core 的启动
- Core: 框架的整体核心
- Checker: 框架的活性，耗能判断，包括对整体接收度的引领

## 可以做啥

TalexFrame 通过 Talexs 系列生态，为您提供全栈解决方案，在不同应用场景中实现代码统一化，项目系统化，应用集成化。目前，根据您的需求场景，您可以将 TalexFrame 用于不同的场景：

- 增强型 SpringBoot 后端 （通过 talex-spring-boot-strater 使用）

- 修复型 Game 后台 （通过 TalexFrame + TGameAddon使用）

- 转换型 AllStack 服务器 （通过完全引用 TalexFrame 使用）

  针对不同应用场景，从上至下复杂度递增。除此之外，addon比plugin更加重量，这将在后续章节中逐步介绍。

## 项目优势

### 管理者

- 项目审核更有保障，选型更有参考
- 系列生态集成更容易，转换数据自处理
- 自然学习分配，自动调用转换

### 开发者

- 一站式开箱即用，多种场景满足不同需求
- 前后端代码同步，部署更方便审查更容易
- 知名框架运行时，并发和稳定性更有保障
- 熟悉风格一键换，支持不同框架编写风格

### 学习者

- 学习到如何开发一个框架
- 前后端全栈，全应用场景学习
- 不同模式下（指框架的集成度）的分配
- 较为规范的代码目录和格式
- 较为精炼的算法参考和运行模式

## 转换式框架

项目通过 INature 抽象接口对所有数据进行管理，底层封装 FrameCreator 意为框架缔造者。

### INature

INature 自然处理，包含 自然语言处理，自然数据处理与自然拓展处理。

通过 INature 你可以在中等及以下复杂度应用场景忽略底层数据交换，直接对对象进行较复杂的数据操作。在需要时，数据将自动提取，并在无需时自动释放保存。这将大大提高系统稳定性和高可用性，通过系列生态，分布部署的同步性得到保障。在应用服务前往另外一个应用服务时，数据确保提前加载以便提高响应速度。

> 将在后续通过 AI 模型训练提高 自然处理 效率和准确率。

#### INatureTransfer

ITransfer 自然转换，通过系统整体架构实现对外开发和谐统一的 api接口，使得对接外部语言诸如 JavaScript，Lua，Python 等脚本语言更加容易，并且针对此类外部语言，系列生态提供相应的 SDK解决方案，供您快速使用或参考。

#### IDataNature

IDataNature 自然数据处理，通过内部的预测算法与缓存算法，提前加载数据和及时保存数据，大幅度提高QPS，利用 RAM 更合理。

#### ILangNature

ILangNature 自然语言处理，通过内部的集成AI，分析用户语言。可实现 国际化，图片分析与识别，后台管理识别，后台操作步骤判别，日志分析，拓展分析，插件分析。

## 插件系统

插件系统大体分为 Addon 与 Plugin 两大类。

你可以通过使用任意基于Jvm的语言来开发插件，我们所推荐的有 Java和Kotlin。

无论如何，请一定记住不要使用 SpringBoot 的东西而是使用本身的东西，因为 SpringBoot 的东西在插件卸载后会残留，这是非常严重的问题。

框架提供的强大Api: FrameData, AutoSaveRepository ...

当然，你也可以使用我们的事件系统。详细的信息将在教程中提到

当你不知所措时，尝试 TFrame 吧

### Addon

Addon 是重量级拓展，与 Plugin 呈现互补趋势。

#### Plugin

Plugin 是轻量级拓展，具有 移植性强，占用率低，拓展性高等优势。

****

## 开发计划

正在逐步更新 **RPC框架模块 (Remote Procedure Call Module)**

## 墨菲安全

> 本项目由 MurphySec 进行安全保护。

[![OSCS Status](https://www.oscs1024.com/platform/badge/Talexs/TalexFrame.svg?size=large)](https://www.murphysec.com/dr/ok0G9SGTn0xsXi7Zex)

##  联系方式

通过QQ 2418876761 或者发送邮件到 TalexDreamSoul@gmail.com 或 2418876761@qq.com 以联系作者。

#### 当然 Issue 是被推荐的方式

