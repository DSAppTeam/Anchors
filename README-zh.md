### Anchors

<img src="https://raw.githubusercontent.com/YummyLau/hexo/master/source/pics/anchors/anchor_logo.png" width = "1300" height = "150" alt="图片名称" align=center />

![](https://travis-ci.org/YummyLau/Anchors.svg?branch=master)
![Language](https://img.shields.io/badge/language-java-orange.svg)
![Language](https://img.shields.io/badge/language-kotlin-orange.svg)
[![](https://jitpack.io/v/DSAppTeam/Anchors.svg)](https://jitpack.io/#DSAppTeam/Anchors)

README: [English](https://github.com/YummyLau/Anchors/blob/master/README.md) | [中文](https://github.com/YummyLau/Anchors/blob/master/README-zh.md)

#### 版本更新
* 1.0.2（2019/06/14） 新增支持直接打开 project 节点
* 1.0.3（2019/12/11） 新增支持节点等待功能
* 1.0.4（2019/12/31） 优化线上反馈多线程同步通知下一节点启动的问题
* 1.0.5（2020/01/20） 新增节点释放监听入口，新增多进程/等待/重启新链等 demo 场景（见 Sample）
* 1.1.0（2020/05/13） 支持 kotlin 及 DSL 特性
* 1.1.1 (2020/07/31)  优化 DSL block api
* 1.1.2 (2020/10/08)  优化依赖树遍历速度，修复 Log-TASK_DETAIL 依赖任务无信息的问题
* 1.1.3 (2020/11/10)  支持多个 block 节点，AnchorManager不再作为单例开放，支持自定义线程池，taskListener 支持 DSL 选择性覆盖方法
* 1.1.4（2021/04/28）优化日志并优化多线程方案
* 1.1.5（2022/06/09）优化多线程方案


#### 简介

`Anchors` 是一个基于图结构，支持同异步依赖任务初始化 Android 启动框架。其锚点提供 "勾住" 依赖的功能，能灵活解决初始化过程中复杂的同步问题。参考 `alpha` 并改进其部分细节, 更贴合 Android 启动的场景, 同时支持优化依赖初始化流程, 选择较优的路径进行初始化。

关于 `alpha` 的思考，请查看 [关于Android异步启动框架alpha的思考](https://yummylau.com/2019/03/15/%E6%BA%90%E7%A0%81%E8%A7%A3%E6%9E%90_alpha%E7%9A%84%E7%A0%94%E7%A9%B6%E4%B8%8E%E6%94%B9%E8%BF%9B/)

较 `alpha` 的优势

* 支持配置 anchors 等待任务链，常用于 application#onCreate 前保证某些初始化任务完成之后再进入 activity 生命周期回调。
* 支持主动请求阻塞等待任务，常用于任务链上的某些初始化任务需要用户逻辑确认。
* 支持同异步任务链

#### 使用需知

> 1. Anchors的设计是为了 app 启动时候做复杂的初始化工作能高效便捷完成，而不是用于业务逻辑中用于初始化某些依赖。
> 2. api 中设置 anchor 会阻塞等待直到 anchor 完成之后才继续走 AnchorsManager#start 后的代码块，application 中 之所以能这么处理是因为没有频繁的 ui 操作。 anchor 之后的任务会在自主由框架调度，同步任务会通过 handler#post 发送到主线程排队处理，异步任务会通过框架内线程池驱动。
> 3. 等待功能在不设置 anchor 的场景下使用。如果设置了 anchor ，则等待任务应该是后置于 anchor 避免 uiThead 阻塞。
> 4. 同异步混合链及 anchor 功能的结合使用，可以灵活处理很多复杂初始化场景，但是要充分理解使用功能时的线程背景。

#### 使用方法
1. 在项目根目路添加 JitPack 仓库，不再使用 JCenter

	```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
	```

2. 在 **app** 模块下添加依赖

	```
	implementation 'com.github.DSAppTeam:Anchors:v1.1.5'
	```

3. 添加依赖图并启动

    框架支持 java 和 kotlin 语言，针对两种语言，Demo 中构建了 `JDatas` 来实现 java 场景逻辑，`Datas` 类来实现 kotlin 逻辑。以 "图" 的形式接收启动依赖集，图的构建节点的链接来实现。
   
   
    ```
    ==>  以 java 为例子，代码可参考 JDatas 类
    
    构建一个 Task, 第一个参数指定 name,也是唯一 id，第二个参数指定该 Task 是否异步运行
    Task task = new Task("name",false) {
        @Override
        protected void run(@NotNull String s) {
            //todo
        }
    };
    
    构建一个 Project，Project 是 Task 子类，用于描述多 Task 场景，由于使用 <TaskName> 构建，传递一个工厂统一处理
    以下构建  task1 <- task2 <- task3 <- task4 逻辑， A -> B 表示 A 依赖 B
    TestTaskFactory testTaskFactory = new TestTaskFactory();
    Project.Builder builder = new Project.Builder("name", testTaskFactory);
    builder9.add("task1Name");
    builder9.add("task2Name").dependOn("task1Name");
    builder9.add("task3Name").dependOn("task2Name");
    builder9.add("task4Name").dependOn("task4Name");
    Project project = builder.build();
    
    组合，其实上述 project#dependOn 就是一种组合方式
    project.dependOn(task);
        ...
    通过各种组合即可构建一张依赖图，之后调用 start 传递图头部节点即可启动
    AnchorsManager.getInstance()
        .start(task);
    
    需要打开调试时 
    AnchorsManager.getInstance()
        .debuggable(true)
        .start(task);
    
    需要设置 anchor 时，<anchorYouNeed> 代表某些 task，这些 task 需要在 Application#onCreate 结束前保证初始完毕
    AnchorsManager.getInstance()
        .addAnchors(anchorYouNeed)
        .start(task);
    
    对于某些场景，你可能需要监听某个 task 的运行状态，则可使用 block 阻塞功能，阻塞等待后可根据业务逻辑解除/破坏等待，waitTaskYouNeed 为你所需要等待的任务
    AnchorsManager anchorsManager = AnchorsManager.getInstance();
    LockableAnchor lockableAnchor = anchorsManager.requestBlockWhenFinish(waitTaskYouNeed); 
    lockableAnchor.setLockListener(...){
        //lockableAnchor.unlock() 解除等待，继续任务链
        //lockableAnchor.smash() 破坏等待，终止任务链
    }
    anchorsManager.start(task);
    
    ==> koltin 也支持上述所有流程，同时也提供了 dsl 的构建形式构建依赖图，代码可参考 Datas 类
    通过调用 graphics 方法来描述一张依赖图，使用 <TaskName> 构建，传递一个工厂统一处理
    AnchorsManager.getInstance()
        .debuggable { true }
        .taskFactory { TestTaskFactory() }     //根据id生成task的工厂
        .anchors { arrayOf(TASK_93, TASK_10) } //anchor 对应的 task id
        .block("TASK_10000") {			       // block 场景的 task id 及 处理监听的 lambda
            //根据业务进行  it.smash() or it.unlock()
        }
        .graphics {							      // 构建依赖图
            UITHREAD_TASK_A.sons(
                    TASK_10.sons(
                            TASK_11.sons(
                                    TASK_12.sons(
                                            TASK_13))),
                    TASK_20.sons(
                            TASK_21.sons(
                                    TASK_22.sons(TASK_23))),
    
                    UITHREAD_TASK_B.alsoParents(TASK_22),
    
                    UITHREAD_TASK_C
            )
            arrayOf(UITHREAD_TASK_A)
        }
        .startUp()
    其中 anchorYouNeed 为你所需要添加的锚点, waitTaskYouNeed 为你所需要等待的任务,dependencyGraphHead 为依赖图的头部。
    ```

#### Sample

代码逻辑请参考 **app** 模块下的 sample。

下面针对 demo 中涉及的主要场景做下阐述。

* 多进程初始化

	**SampleApplication.class** 中针对多进程进行实践，满足绝大部分初始化场景。```SampleApplication#onCreate```
	会在涉及新进程业务启动时被再次调用，所以不同进程的初始化场景可根据进程名称进行特定定制。
	代码可参考 ```SampleApplication#initDependenciesCompatMultiProcess```  . 
	触发拉起新进程可参考 ```MainActivity#testPrivateProcess```  或者  ```MainActivity#testPublicProcess``` 。

* 某初始化链中间节点需要等待响应

	某些非常苛刻的初始化链可能需要等待某些条件。（注意：这里的响应应该是 UI 线程的响应，如果是异步响应，则可以作为一个节点提前主动初始化了。）比如某些 app 初始化的时候需要用户选择 ”兴趣场景“ 进而初始化后续页面的所有逻辑等。代码可参考 ```MainActivity#testUserChoose```

* 某初始化链完成之后可能会再启动另一条新链

	这类功能也支持，但是实际上框架更提倡在 application 中统一管理所有初始化链。因为框架强调的是 **任意初始化任务应该是属于业务重量级初始化代码或者第三方SDK初始化** 。
	代码可参考 ```MainActivity#testRestartNewDependenciesLink``` 。


#### Debug 信息

**debuggale** 模式下能打印不同维度的 log 作为调试信息输出，同时针对每个依赖任务做 `Trace` 追踪, 可以通过 *python systrace.py* 来输出 **trace.html** 进行性能分析。

`Anchors` 定义不同的 **TAG** 用于过滤 log, 需要打开 Debug 模式。

* `Anchors`, 最基础的 TAG
* `TASK_DETAIL`, 过滤依赖任务的详情

	```
	2019-03-18 14:19:45.687 22493-22493/com.effective.android.sample D/TASK_DETAIL: TASK_DETAIL
	======================= task (UITHREAD_TASK_A ) =======================
	| 依赖任务 :
	| 是否是锚点任务 : false
	| 线程信息 : main
	| 开始时刻 : 1552889985401 ms
	| 等待运行耗时 : 85 ms
	| 运行任务耗时 : 200 ms
	| 结束时刻 : 1552889985686
	==============================================
	```
* `ANCHOR_DETAIL`, 过滤输出锚点任务信息

	```
	2019-03-18 14:42:33.354 24719-24719/com.effective.android.sample W/ANCHOR_DETAIL: anchor "TASK_100" no found !
	2019-03-18 14:42:33.354 24719-24719/com.effective.android.sample W/ANCHOR_DETAIL: anchor "TASK_E" no found !
	2019-03-18 14:42:33.355 24719-24719/com.effective.android.sample D/ANCHOR_DETAIL: has some anchors！( "TASK_93" )
	2019-03-18 14:42:34.188 24719-24746/com.effective.android.sample D/ANCHOR_DETAIL: TASK_DETAIL
    ======================= task (TASK_93 ) =======================
    | 依赖任务 : TASK_92
    | 是否是锚点任务 : true
    | 线程信息 : Anchors Thread #7
    | 开始时刻 : 1552891353984 ms
    | 等待运行耗时 : 4 ms
    | 运行任务耗时 : 200 ms
    | 结束时刻 : 1552891354188
    ==============================================
	2019-03-18 14:42:34.194 24719-24719/com.effective.android.sample D/ANCHOR_DETAIL: All anchors were released！
	```
	
* `LOCK_DETAIL`, 过滤输出等待信息

	```
	2019-12-11 14:53:11.784 6183-6437/com.effective.android.sample D/LOCK_DETAIL: Anchors Thread #9- lock( TASK_10 )
	2019-12-11 14:53:13.229 6183-6183/com.effective.android.sample D/LOCK_DETAIL: main- unlock( TASK_10 )
	2019-12-11 14:53:13.229 6183-6183/com.effective.android.sample D/LOCK_DETAIL: Continue the task chain...
	
	```

* `DEPENDENCE_DETAIL`, 过滤依赖图信息

	```
	2019-03-18 14:27:53.724 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_9_start(1552890473721) --> TASK_90 --> TASK_91 --> PROJECT_9_end(1552890473721)
	2019-03-18 14:27:53.724 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_9_start(1552890473721) --> TASK_90 --> TASK_92 --> TASK_93 --> PROJECT_9_end(1552890473721)
	2019-03-18 14:27:53.724 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_8_start(1552890473721) --> TASK_80 --> TASK_81 --> PROJECT_8_end(1552890473721)
	2019-03-18 14:27:53.724 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_8_start(1552890473721) --> TASK_80 --> TASK_82 --> TASK_83 --> PROJECT_8_end(1552890473721)
	2019-03-18 14:27:53.725 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_7_start(1552890473720) --> TASK_70 --> TASK_71 --> PROJECT_7_end(1552890473720)
	2019-03-18 14:27:53.725 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_7_start(1552890473720) --> TASK_70 --> TASK_72 --> TASK_73 --> PROJECT_7_end(1552890473720)
	2019-03-18 14:27:53.725 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_6_start(1552890473720) --> TASK_60 --> TASK_61 --> PROJECT_6_end(1552890473720)
	2019-03-18 14:27:53.725 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_6_start(1552890473720) --> TASK_60 --> TASK_62 --> TASK_63 --> PROJECT_6_end(1552890473720)
	2019-03-18 14:27:53.725 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_5_start(1552890473720) --> TASK_50 --> TASK_51 --> PROJECT_5_end(1552890473720)
	2019-03-18 14:27:53.725 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_5_start(1552890473720) --> TASK_50 --> TASK_52 --> TASK_53 --> PROJECT_5_end(1552890473720)
	2019-03-18 14:27:53.725 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_4_start(1552890473720) --> TASK_40 --> TASK_41 --> TASK_42 --> TASK_43 --> PROJECT_4_end(1552890473720)
	2019-03-18 14:27:53.726 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_3_start(1552890473720) --> TASK_30 --> TASK_31 --> TASK_32 --> TASK_33 --> PROJECT_3_end(1552890473720)
	2019-03-18 14:27:53.726 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_2_start(1552890473719) --> TASK_20 --> TASK_21 --> TASK_22 --> TASK_23 --> PROJECT_2_end(1552890473719)
	2019-03-18 14:27:53.726 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_1_start(1552890473719) --> TASK_10 --> TASK_11 --> TASK_12 --> TASK_13 --> PROJECT_1_end(1552890473719)
	2019-03-18 14:27:53.726 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> UITHREAD_TASK_B
	2019-03-18 14:27:53.726 22843-22843/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> UITHREAD_TASK_C
	```

#### 效果对比

下面是没有使用锚点和使用锚点场景下, **Trace** 给出的执行时间

<img src="https://raw.githubusercontent.com/YummyLau/hexo/master/source/pics/anchors/anchor_1.png" width = "1860" height = "400" alt="图片名称" align=center />

依赖图中有着一条 `UITHREAD_TASK_A -> TASK_90 -> TASK_92 -> Task_93`依赖。假设我们的这条依赖路径是后续业务的前置条件,则我们需要等待该业务完成之后再进行自身的业务代码。如果不是则我们不关系他们的结束时机。在使用锚点功能时，我们勾住 `TASK_93`，则从始端到该锚点的优先级将被提升。从上图可以看到执行该依赖链的时间缩短了。

> 依赖图用于解决任务执行时任务间的依赖关系，而锚点设置则是用于解决执行依赖与代码调用点之间的同步关系。


#### 期望
编写该项目只是希望能提高日常开发的效率，专注于处理业务 。如果更好的做法或者意见建议，欢迎写信到 yummyl.lau@gmail.com, 提出 **Issues** 或发起 **Pull requests** , 任何问题都会第一时间得到处理解决。
