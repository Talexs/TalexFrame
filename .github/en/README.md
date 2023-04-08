# TalexFrame

[简体中文](./../../README.md) | English


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




## Project Background

Inspired by the Minecraft server Bukkit and its framework series, we have improved the framework for the main functions, and decoupled the subsidiary functions using ModulePlugin.

## Project History

It was originally developed as a backend for "Minecraft" HyCraft and later used in the Pubsher server series development.

Currently used in the backend development of PVPIN Studio/KxordMarket/TalexWiki.

## Project Introduction

TalexFrame, also known as TF, is a Java conversion framework used to build systemic backends. By using the migration of well-known libraries, it provides standard encapsulation templates and easy development processes, making your learning and implementation faster and more convenient. It provides a unified API development based on the standard SpringBoot Annotation style.

## Module System

We use modules like apis and commands for isolation.

And the module-based functionality we provide can even be stronger than SpringBoot itself.

## System Design

The system is divided into three core modules, each with its own functionality:

- Launcher: The overall startup logic of the framework, including environmental judgment, guiding to the startup of the core
- Core: The overall core of the framework
- Checker: The vitality of the framework, energy consumption judgment, including leading the overall acceptance

## What Can You Do?

Through the Talexs ecosystem, TalexFrame provides you with an all-stack solution, unifying code in different application scenarios, systemizing projects, and integrating applications. Currently, according to your demand scenario, you can use TalexFrame in different scenarios:

- Enhanced SpringBoot Backend (using talex-spring-boot-strater)
- Repair-type game backend (using TalexFrame + TGameAddon)
- Transformational AllStack Server (using TalexFrame entirely)

The complexity increases from top to bottom for different application scenarios. In addition, addons are heavier than plugins, which will be gradually introduced in later chapters.

## Project Advantages

### Manager

- Project audit is more secure, and selection is more referential
- Series ecology integration is easier, and data conversion is self-processed
- Natural learning allocation, automatic conversion call

### Developer

- One-stop development and use, multiple scenarios meet different needs
- Front-end and back-end code synchronization, deployment is more convenient and review is easier
- Well-known framework runtime, concurrency and stability are more guaranteed
- Familiar style switching, supporting different framework writing styles

### Learner

- Learn how to develop a framework
- Full-stack, full-application scenario learning
- Allocation under different modes (referring to the integration degree of the framework)
- More standardized code directory and format
- More concise algorithm references and operation modes

## Conversion Framework

The project manages all data through the INature abstract interface, and the underlying encapsulation FrameCreator means the creator of the framework.

### INature

INature Natural processing, including natural language processing, natural data processing, and natural expansion processing.

Through INature, you can ignore the underlying data exchange in medium and below complexity application scenarios and directly operate on more complex data. When necessary, the data will be automatically extracted, and when not needed, it will be automatically released and saved. This will greatly improve system stability and high availability, and through the series ecology, the synchronization of distributed deployment is guaranteed. When the application service goes to another application service, the data is guaranteed to be loaded in advance to improve response speed.

> The efficiency and accuracy of natural processing will be improved through AI model training in the future.

#### INatureTransfer

ITransfer natural conversion, through the overall architecture of the system, implements a harmonious and unified API interface for external development, making it easier to dock with external languages such as JavaScript, Lua, Python, and providing corresponding SDK solutions for such external languages according to the Talexs ecosystem. Reference or use it quickly.

#### IDataNature

IDataNature natural data processing, through internal predictive algorithms and machine learning models, provides more accurate and efficient data processing capabilities. It reduces the complexity of traditional data manipulation and allows developers to focus more on business logic.

### FrameCreator

FrameCreator is the underlying encapsulation of TalexFrame, which provides a standardized development process for developers to build their own applications. It is responsible for managing the core modules and addons, and provides an interface for developers to easily integrate additional functionality. The main advantages of using FrameCreator are:

- Standardized development process: developers can follow the same set of guidelines to develop their applications, making it easier to maintain and update.
- Easy integration: developers can integrate additional functionality through addons, without having to change the core codebase.
- High performance: FrameCreator optimizes the system structure and utilizes multi-threading, resulting in faster execution times and higher application performance.

## Conclusion

TalexFrame provides an easy-to-use solution for building systemic backends. With its modular architecture, standardized development process, and high performance, it can be used in a wide range of application scenarios. Whether you are a developer, manager or learner, TalexFrame has something to offer.
