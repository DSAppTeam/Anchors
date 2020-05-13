package com.effective.android.sample.data

import com.effective.android.anchors.*
import com.effective.android.anchors.AnchorsManager.debuggable
import com.effective.android.anchors.AnchorsManager.getInstance
import com.effective.android.sample.data.TestTaskFactory.*

class TaskTest {
    /**
     * 可通过DEPENDENCE_DETAIL 查看到有一下任务链
     * 2019-12-11 14:05:44.848 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_9_start(1576044344839) --> TASK_90 --> TASK_91 --> PROJECT_9_end(1576044344839)
     * 2019-12-11 14:05:44.848 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_9_start(1576044344839) --> TASK_90 --> TASK_92 --> TASK_93 --> PROJECT_9_end(1576044344839)
     * 2019-12-11 14:05:44.849 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_8_start(1576044344839) --> TASK_80 --> TASK_81 --> PROJECT_8_end(1576044344839)
     * 2019-12-11 14:05:44.849 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_8_start(1576044344839) --> TASK_80 --> TASK_82 --> TASK_83 --> PROJECT_8_end(1576044344839)
     * 2019-12-11 14:05:44.849 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_7_start(1576044344838) --> TASK_70 --> TASK_71 --> PROJECT_7_end(1576044344838)
     * 2019-12-11 14:05:44.849 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_7_start(1576044344838) --> TASK_70 --> TASK_72 --> TASK_73 --> PROJECT_7_end(1576044344838)
     * 2019-12-11 14:05:44.850 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_6_start(1576044344838) --> TASK_60 --> TASK_61 --> PROJECT_6_end(1576044344838)
     * 2019-12-11 14:05:44.850 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_6_start(1576044344838) --> TASK_60 --> TASK_62 --> TASK_63 --> PROJECT_6_end(1576044344838)
     * 2019-12-11 14:05:44.850 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_5_start(1576044344837) --> TASK_50 --> TASK_51 --> PROJECT_5_end(1576044344837)
     * 2019-12-11 14:05:44.851 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_5_start(1576044344837) --> TASK_50 --> TASK_52 --> TASK_53 --> PROJECT_5_end(1576044344837)
     * 2019-12-11 14:05:44.851 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_4_start(1576044344837) --> TASK_40 --> TASK_41 --> TASK_42 --> TASK_43 --> PROJECT_4_end(1576044344837)
     * 2019-12-11 14:05:44.852 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_3_start(1576044344836) --> TASK_30 --> TASK_31 --> TASK_32 --> TASK_33 --> PROJECT_3_end(1576044344836)
     * 2019-12-11 14:05:44.852 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_2_start(1576044344835) --> TASK_20 --> TASK_21 --> TASK_22 --> TASK_23 --> PROJECT_2_end(1576044344835)
     * 2019-12-11 14:05:44.852 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> PROJECT_1_start(1576044344835) --> TASK_10 --> TASK_11 --> TASK_12 --> TASK_13 --> PROJECT_1_end(1576044344835)
     * 2019-12-11 14:05:44.853 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> UITHREAD_TASK_B
     * 2019-12-11 14:05:44.853 32459-32459/com.effective.android.sample D/DEPENDENCE_DETAIL: UITHREAD_TASK_A --> UITHREAD_TASK_C
     *
     *
     *
     *
     * 设置了一下anchor
     * 2019-12-11 14:05:44.853 32459-32459/com.effective.android.sample D/ANCHOR_DETAIL: has some anchors！( "TASK_10" "TASK_93" )
     *
     *
     * 校验log：当且仅当anchor执行完毕，解除阻塞
     * 2019-12-11 14:05:44.805 32459-32459/com.effective.android.sample D/SampleApplication: onCreate - start
     *
     *
     * （TASK_10 完成）
     * （TASK_93 完成）
     *
     *
     * 2019-12-11 14:05:46.086 32459-32459/com.effective.android.sample D/ANCHOR_DETAIL: All anchors were released！
     * 2019-12-11 14:05:46.087 32459-32459/com.effective.android.sample D/SampleApplication: onCreate - end
     */
    fun startFromApplicationOnMainProcess() {
        val testTaskFactory = TestTaskFactory()
        val builder1 = Project.Builder(PROJECT_1, testTaskFactory)
        builder1.add(TASK_10)
        builder1.add(TASK_11).dependOn(TASK_10)
        builder1.add(TASK_12).dependOn(TASK_11)
        builder1.add(TASK_13).dependOn(TASK_12)
        val project1 = builder1.build()
        val builder2 = Project.Builder(PROJECT_2, testTaskFactory)
        builder2.add(TASK_20)
        builder2.add(TASK_21).dependOn(TASK_20)
        builder2.add(TASK_22).dependOn(TASK_21)
        builder2.add(TASK_23).dependOn(TASK_22)
        val project2 = builder2.build()
        val builder3 = Project.Builder(PROJECT_3, testTaskFactory)
        builder3.add(TASK_30)
        builder3.add(TASK_31).dependOn(TASK_30)
        builder3.add(TASK_32).dependOn(TASK_31)
        builder3.add(TASK_33).dependOn(TASK_32)
        val project3 = builder3.build()
        val builder4 = Project.Builder(PROJECT_4, testTaskFactory)
        builder4.add(TASK_40)
        builder4.add(TASK_41).dependOn(TASK_40)
        builder4.add(TASK_42).dependOn(TASK_41)
        builder4.add(TASK_43).dependOn(TASK_42)
        val project4 = builder4.build()
        val builder5 = Project.Builder(PROJECT_5, testTaskFactory)
        builder5.add(TASK_50)
        builder5.add(TASK_51).dependOn(TASK_50)
        builder5.add(TASK_52).dependOn(TASK_50)
        builder5.add(TASK_53).dependOn(TASK_52)
        val project5 = builder5.build()
        val builder6 = Project.Builder(PROJECT_6, testTaskFactory)
        builder6.add(TASK_60)
        builder6.add(TASK_61).dependOn(TASK_60)
        builder6.add(TASK_62).dependOn(TASK_60)
        builder6.add(TASK_63).dependOn(TASK_62)
        val project6 = builder6.build()
        val builder7 = Project.Builder(PROJECT_7, testTaskFactory)
        builder7.add(TASK_70)
        builder7.add(TASK_71).dependOn(TASK_70)
        builder7.add(TASK_72).dependOn(TASK_70)
        builder7.add(TASK_73).dependOn(TASK_72)
        val project7 = builder7.build()
        val builder8 = Project.Builder(PROJECT_8, testTaskFactory)
        builder8.add(TASK_80)
        builder8.add(TASK_81).dependOn(TASK_80)
        builder8.add(TASK_82).dependOn(TASK_80)
        builder8.add(TASK_83).dependOn(TASK_82)
        val project8 = builder8.build()
        val builder9 = Project.Builder(PROJECT_9, testTaskFactory)
        builder9.add(TASK_90)
        builder9.add(TASK_91).dependOn(TASK_90)
        builder9.add(TASK_92).dependOn(TASK_90)
        builder9.add(TASK_93).dependOn(TASK_92)
        val project9 = builder9.build()
        val UiTaskA: Task = UITHREAD_TASK_A()
        val UiTaskB: Task = UITHREAD_TASK_B()
        val UiTaskC: Task = UITHREAD_TASK_C()
        project9.dependOn(UiTaskA)
        project8.dependOn(UiTaskA)
        project7.dependOn(UiTaskA)
        project6.dependOn(UiTaskA)
        project5.dependOn(UiTaskA)
        project4.dependOn(UiTaskA)
        project3.dependOn(UiTaskA)
        project2.dependOn(UiTaskA)
        project1.dependOn(UiTaskA)
        UiTaskB.dependOn(UiTaskA)
        UiTaskC.dependOn(UiTaskA)
        getInstance().debuggable(true)
                .addAnchors(TASK_93, "TASK_E", "TASK_10")
                .start(UiTaskA)
    }

    fun startFromApplicationOnMainProcessByDsl() {
        getInstance()
                .debuggable { true }
                .taskFactory { TestTaskFactory() }
                .anchors { arrayOf(TASK_93, "TASK_E", TASK_10) }
                .block("TASK_10000") {
                    //根据业务进行  it.smash() or it.unlock()
                }
                .graphics {
                    UITHREAD_TASK_A.sons(
                            TASK_10.sons(
                                    TASK_11.sons(
                                            TASK_12.sons(
                                                    TASK_13))),
                            TASK_20.sons(
                                    TASK_21.sons(
                                            TASK_22.sons(TASK_23))),
                            TASK_30.sons(
                                    TASK_32.sons(
                                            TASK_32.sons(
                                                    TASK_33))),
                            TASK_40.sons(
                                    TASK_42.sons(
                                            TASK_42.sons(
                                                    TASK_43))),
                            TASK_50.sons(
                                    TASK_52.sons(
                                            TASK_52.sons(
                                                    TASK_53))),
                            TASK_60.sons(
                                    TASK_62.sons(
                                            TASK_62.sons(
                                                    TASK_63))),
                            TASK_70.sons(
                                    TASK_72.sons(
                                            TASK_72.sons(
                                                    TASK_73))),
                            TASK_80.sons(
                                    TASK_82.sons(
                                            TASK_82.sons(
                                                    TASK_83))),
                            TASK_90.sons(
                                    TASK_92.sons(
                                            TASK_92.sons(
                                                    TASK_93))),

                            UITHREAD_TASK_B.alsoParents(TASK_22),

                            UITHREAD_TASK_C
                    )
                    arrayOf(UITHREAD_TASK_A)
                }
                .startUp()
    }

    fun startFromApplicationOnPrivateProcess() {
        val testTaskFactory = TestTaskFactory()
        val builder8 = Project.Builder(PROJECT_8, testTaskFactory)
        builder8.add(TASK_80)
        builder8.add(TASK_81).dependOn(TASK_80)
        builder8.add(TASK_82).dependOn(TASK_80)
        builder8.add(TASK_83).dependOn(TASK_82)
        val project8 = builder8.build()
        val UiTaskA: Task = UITHREAD_TASK_A()
        val UiTaskB: Task = UITHREAD_TASK_B()
        project8.dependOn(UiTaskA)
        UiTaskB.dependOn(UiTaskA)
        getInstance().debuggable(true)
                .addAnchors(TASK_82)
                .start(UiTaskA)
    }

    fun startFromApplicationOnPublicProcess() {
        val testTaskFactory = TestTaskFactory()
        val builder9 = Project.Builder(PROJECT_9, testTaskFactory)
        builder9.add(TASK_90)
        builder9.add(TASK_91).dependOn(TASK_90)
        builder9.add(TASK_92).dependOn(TASK_90)
        builder9.add(TASK_93).dependOn(TASK_92)
        val project9 = builder9.build()
        val UiTaskA: Task = UITHREAD_TASK_A()
        val UiTaskC: Task = UITHREAD_TASK_C()
        project9.dependOn(UiTaskA)
        UiTaskC.dependOn(UiTaskA)
        getInstance().debuggable(true)
                .addAnchors(TASK_93)
                .start(UiTaskA)
    }


    fun startForTestLockableAnchor(): LockableAnchor {
        val testTaskFactory = TestTaskFactory()
        val builder1 = Project.Builder(PROJECT_1, testTaskFactory)
        builder1.add(TASK_10)
        builder1.add(TASK_11).dependOn(TASK_10)
        builder1.add(TASK_12).dependOn(TASK_11)
        builder1.add(TASK_13).dependOn(TASK_12)
        val project1 = builder1.build()
        val UiTaskA: Task = UITHREAD_TASK_A()
        val UiTaskB: Task = UITHREAD_TASK_B()
        val UiTaskC: Task = UITHREAD_TASK_C()
        project1.dependOn(UiTaskA)
        UiTaskB.dependOn(UiTaskA)
        UiTaskC.dependOn(UiTaskB)
        val anchorsManager = getInstance()
        anchorsManager.debuggable(true)
        val lockableAnchor = anchorsManager.requestBlockWhenFinish(testTaskFactory.getTask(TASK_10))
        anchorsManager.start(UiTaskA)
        return lockableAnchor
    }

    fun startForTestLockableAnchorByDsl(listener: (lockableAnchor: LockableAnchor) -> Unit): LockableAnchor? {
        getInstance()
                .debuggable { true }
                .taskFactory { TestTaskFactory() }
                .block(TASK_10) {
                    listener.invoke(it)
                }
                .graphics {
                    arrayOf(TASK_10.sons(TASK_11.sons(TASK_12.sons(TASK_13))))
                }
                .startUp()
        return getInstance().curBlockAnchor
    }

    fun startForLinkOneByDsl(runnable: Runnable) {
        val factory = TestTaskFactory();
        val end = factory.getTask(TASK_13);
        end.addTaskListener(object : TaskListener {
            override fun onStart(task: Task) {}
            override fun onRunning(task: Task) {}
            override fun onFinish(task: Task) {}
            override fun onRelease(task: Task) {
                runnable.run()
            }
        });
        getInstance()
                .debuggable { true }
                .taskFactory { factory }
                .graphics {
                    arrayOf(UITHREAD_TASK_A.sons(TASK_10.sons(TASK_11.sons(TASK_12.sons(TASK_13)))))
                }
                .startUp()
    }

    fun startForLinkTwoByDsl() {
        getInstance()
                .debuggable { true }
                .taskFactory { TestTaskFactory() }
                .graphics {
                    arrayOf(UITHREAD_TASK_A.sons(TASK_20.sons(TASK_21.sons(TASK_22.sons(TASK_23)))))
                }
                .startUp()
    }

    companion object {
        const val PROJECT_1 = "PROJECT_1"
        const val TASK_10 = "TASK_10"
        const val TASK_11 = "TASK_11"
        const val TASK_12 = "TASK_12"
        const val TASK_13 = "TASK_13"
        const val PROJECT_2 = "PROJECT_2"
        const val TASK_20 = "TASK_20"
        const val TASK_21 = "TASK_21"
        const val TASK_22 = "TASK_22"
        const val TASK_23 = "TASK_23"
        const val PROJECT_3 = "PROJECT_3"
        const val TASK_30 = "TASK_30"
        const val TASK_31 = "TASK_31"
        const val TASK_32 = "TASK_32"
        const val TASK_33 = "TASK_33"
        const val PROJECT_4 = "PROJECT_4"
        const val TASK_40 = "TASK_40"
        const val TASK_41 = "TASK_41"
        const val TASK_42 = "TASK_42"
        const val TASK_43 = "TASK_43"
        const val PROJECT_5 = "PROJECT_5"
        const val TASK_50 = "TASK_50"
        const val TASK_51 = "TASK_51"
        const val TASK_52 = "TASK_52"
        const val TASK_53 = "TASK_53"
        const val PROJECT_6 = "PROJECT_6"
        const val TASK_60 = "TASK_60"
        const val TASK_61 = "TASK_61"
        const val TASK_62 = "TASK_62"
        const val TASK_63 = "TASK_63"
        const val PROJECT_7 = "PROJECT_7"
        const val TASK_70 = "TASK_70"
        const val TASK_71 = "TASK_71"
        const val TASK_72 = "TASK_72"
        const val TASK_73 = "TASK_73"
        const val PROJECT_8 = "PROJECT_8"
        const val TASK_80 = "TASK_80"
        const val TASK_81 = "TASK_81"
        const val TASK_82 = "TASK_82"
        const val TASK_83 = "TASK_83"
        const val PROJECT_9 = "PROJECT_9"
        const val TASK_90 = "TASK_90"
        const val TASK_91 = "TASK_91"
        const val TASK_92 = "TASK_92"
        const val TASK_93 = "TASK_93"
        const val UITHREAD_TASK_A = "UITHREAD_TASK_A"
        const val UITHREAD_TASK_B = "UITHREAD_TASK_B"
        const val UITHREAD_TASK_C = "UITHREAD_TASK_C"
        const val ASYNC_TASK_1 = "ASYNC_TASK_1"
        const val ASYNC_TASK_2 = "ASYNC_TASK_2"
        const val ASYNC_TASK_3 = "ASYNC_TASK_3"
        const val ASYNC_TASK_4 = "ASYNC_TASK_4"
        const val ASYNC_TASK_5 = "ASYNC_TASK_5"
    }
}