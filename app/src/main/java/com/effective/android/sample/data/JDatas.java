package com.effective.android.sample.data;

import com.effective.android.anchors.AnchorsManager;
import com.effective.android.anchors.task.lock.LockableAnchor;
import com.effective.android.anchors.task.project.Project;
import com.effective.android.anchors.task.Task;
import com.effective.android.anchors.task.listener.TaskListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.effective.android.anchors.AnchorsManager.getInstance;
import static com.effective.android.sample.data.Datas.*;

public class JDatas {
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
    public void startFromApplicationOnMainProcess() {
        TestTaskFactory testTaskFactory = new TestTaskFactory();
        Project.Builder builder1 = new Project.Builder(PROJECT_1, testTaskFactory);
        builder1.add(Datas.TASK_10);
        builder1.add(TASK_11).dependOn(TASK_10);
        builder1.add(TASK_12).dependOn(TASK_11);
        builder1.add(TASK_13).dependOn(TASK_12);
        Project project1 = builder1.build();
        Project.Builder builder2 = new Project.Builder(PROJECT_2, testTaskFactory);
        builder2.add(TASK_20);
        builder2.add(TASK_21).dependOn(TASK_20);
        builder2.add(TASK_22).dependOn(TASK_21);
        builder2.add(TASK_23).dependOn(TASK_22);
        Project project2 = builder2.build();
        Project.Builder builder3 = new Project.Builder(PROJECT_3, testTaskFactory);
        builder3.add(TASK_30);
        builder3.add(TASK_31).dependOn(TASK_30);
        builder3.add(TASK_32).dependOn(TASK_31);
        builder3.add(TASK_33).dependOn(TASK_32);
        Project project3 = builder3.build();
        Project.Builder builder4 = new Project.Builder(PROJECT_4, testTaskFactory);
        builder4.add(TASK_40);
        builder4.add(TASK_41).dependOn(TASK_40);
        builder4.add(TASK_42).dependOn(TASK_41);
        builder4.add(TASK_43).dependOn(TASK_42);
        Project project4 = builder4.build();
        Project.Builder builder5 = new Project.Builder(PROJECT_5, testTaskFactory);
        builder5.add(TASK_50);
        builder5.add(TASK_51).dependOn(TASK_50);
        builder5.add(TASK_52).dependOn(TASK_50);
        builder5.add(TASK_53).dependOn(TASK_52);
        Project project5 = builder5.build();
        Project.Builder builder6 = new Project.Builder(PROJECT_6, testTaskFactory);
        builder6.add(TASK_60);
        builder6.add(TASK_61).dependOn(TASK_60);
        builder6.add(TASK_62).dependOn(TASK_60);
        builder6.add(TASK_63).dependOn(TASK_62);
        Project project6 = builder6.build();
        Project.Builder builder7 = new Project.Builder(PROJECT_7, testTaskFactory);
        builder7.add(TASK_70);
        builder7.add(TASK_71).dependOn(TASK_70);
        builder7.add(TASK_72).dependOn(TASK_70);
        builder7.add(TASK_73).dependOn(TASK_72);
        Project project7 = builder7.build();
        Project.Builder builder8 =new Project.Builder(PROJECT_8, testTaskFactory);
        builder8.add(TASK_80);
        builder8.add(TASK_81).dependOn(TASK_80);
        builder8.add(TASK_82).dependOn(TASK_80);
        builder8.add(TASK_83).dependOn(TASK_82);
        Project project8 = builder8.build();
        Project.Builder builder9 = new Project.Builder(PROJECT_9, testTaskFactory);
        builder9.add(TASK_90);
        builder9.add(TASK_91).dependOn(TASK_90);
        builder9.add(TASK_92).dependOn(TASK_90);
        builder9.add(TASK_93).dependOn(TASK_92);
        Project project9 = builder9.build();
        TestTask UiTaskA = new UITHREAD_TASK_A();
        TestTask UiTaskB = new UITHREAD_TASK_B();
        TestTask UiTaskC = new UITHREAD_TASK_C();
        project9.dependOn(UiTaskA);
        project8.dependOn(UiTaskA);
        project7.dependOn(UiTaskA);
        project6.dependOn(UiTaskA);
        project5.dependOn(UiTaskA);
        project4.dependOn(UiTaskA);
        project3.dependOn(UiTaskA);
        project2.dependOn(UiTaskA);
        project1.dependOn(UiTaskA);
        UiTaskB.dependOn(UiTaskA);
        UiTaskC.dependOn(UiTaskA);
        getInstance().debuggable(true)
                .addAnchors(TASK_93, "TASK_E", "TASK_10")
                .start(UiTaskA);
    }

    public void startFromApplicationOnPrivateProcess() {
        TestTaskFactory testTaskFactory = new TestTaskFactory();
        Project.Builder builder8 = new Project.Builder(PROJECT_8, testTaskFactory);
        builder8.add(TASK_80);
        builder8.add(TASK_81).dependOn(TASK_80);
        builder8.add(TASK_82).dependOn(TASK_80);
        builder8.add(TASK_83).dependOn(TASK_82);
        Project project8 = builder8.build();
        TestTask UiTaskA = new UITHREAD_TASK_A();
        TestTask UiTaskB = new UITHREAD_TASK_B();
        project8.dependOn(UiTaskA);
        UiTaskB.dependOn(UiTaskA);
        getInstance().debuggable(true)
                .addAnchors(TASK_82)
                .start(UiTaskA);
    }

    public void startFromApplicationOnPublicProcess() {
        TestTaskFactory testTaskFactory = new TestTaskFactory();
        Project.Builder builder9 = new Project.Builder(PROJECT_9, testTaskFactory);
        builder9.add(TASK_90);
        builder9.add(TASK_91).dependOn(TASK_90);
        builder9.add(TASK_92).dependOn(TASK_90);
        builder9.add(TASK_93).dependOn(TASK_92);
        Project project9 = builder9.build();
        TestTask UiTaskA = new UITHREAD_TASK_A();
        TestTask UiTaskC = new UITHREAD_TASK_C();
        project9.dependOn(UiTaskA);
        UiTaskC.dependOn(UiTaskA);
        AnchorsManager.getInstance().debuggable(true)
                .addAnchors(TASK_93)
                .start(UiTaskA);
    }


    public List<LockableAnchor> startForTestLockableAnchor(){
        TestTaskFactory testTaskFactory = new TestTaskFactory();
        Project.Builder  builder1 = new Project.Builder(PROJECT_1, testTaskFactory);
        builder1.add(TASK_10);
        builder1.add(TASK_11).dependOn(TASK_10);
        builder1.add(TASK_12).dependOn(TASK_11);
        builder1.add(TASK_13).dependOn(TASK_12);
        Project project1 = builder1.build();
        TestTask UiTaskA = new UITHREAD_TASK_A();
        TestTask UiTaskB = new UITHREAD_TASK_B();
        TestTask UiTaskC = new UITHREAD_TASK_C();
        project1.dependOn(UiTaskA);
        UiTaskB.dependOn(UiTaskA);
        UiTaskC.dependOn(UiTaskB);
        AnchorsManager anchorsManager = getInstance();
        anchorsManager.debuggable(true);
        final LockableAnchor lockableAnchor1 = anchorsManager.requestBlockWhenFinish(testTaskFactory.getTask(TASK_10));
        final LockableAnchor lockableAnchor2 = anchorsManager.requestBlockWhenFinish(testTaskFactory.getTask(TASK_11));
        anchorsManager.start(UiTaskA);
        List<LockableAnchor> result = new ArrayList<>();
        result.add(lockableAnchor1);
        result.add(lockableAnchor2);
        return result;
    }

    public AnchorsManager startForLinkOneByDsl(final Runnable runnable) {
        TestTaskFactory factory = new TestTaskFactory();
        Task end = factory.getTask(TASK_13);
        end.addTaskListener(new TaskListener() {
            @Override
            public void onStart(@NotNull Task task) {

            }

            @Override
            public void onRunning(@NotNull Task task) {

            }

            @Override
            public void onFinish(@NotNull Task task) {
                if(runnable != null){
                    runnable.run();
                }
            }

            @Override
            public void onRelease(@NotNull Task task) {

            }
        });
        Project.Builder builder= new Project.Builder(PROJECT_9, factory);
        builder.add(TASK_10);
        builder.add(TASK_11).dependOn(TASK_10);
        builder.add(TASK_12).dependOn(TASK_11);
        builder.add(TASK_13).dependOn(TASK_12);
        Project project = builder.build();
        TestTask UiTaskA = new UITHREAD_TASK_A();
        project.dependOn(UiTaskA);
        AnchorsManager anchorsManager = getInstance();
        anchorsManager.debuggable(true).addAnchor(TASK_13).start(UiTaskA);
        return anchorsManager;
    }

    public void startForLinkTwoByDsl() {
        TestTaskFactory factory = new TestTaskFactory();
        Project.Builder builder= new Project.Builder(PROJECT_9, factory);
        builder.add(TASK_20);
        builder.add(TASK_21).dependOn(TASK_20);
        builder.add(TASK_22).dependOn(TASK_21);
        builder.add(TASK_23).dependOn(TASK_22);
        Project project = builder.build();
        TestTask UiTaskA = new UITHREAD_TASK_A();
        project.dependOn(UiTaskA);
        getInstance().debuggable(true)
                .start(UiTaskA);
    }

    public void startAllAsyncTask() {
        TestTaskFactory factory = new TestTaskFactory();
        Project.Builder builder= new Project.Builder(PROJECT_9, factory);
        builder.add(ASYNC_TASK_1);
        builder.add(ASYNC_TASK_2).dependOn(ASYNC_TASK_1);
        builder.add(ASYNC_TASK_3).dependOn(ASYNC_TASK_2);
        builder.add(ASYNC_TASK_4).dependOn(ASYNC_TASK_3);
        builder.add(ASYNC_TASK_5).dependOn(ASYNC_TASK_4);
        Project project = builder.build();
        getInstance().debuggable(true).addAnchor(ASYNC_TASK_1)
                .start(project);
    }

    public void startAllSyncTask() {
        TestTaskFactory factory = new TestTaskFactory();
        Project.Builder builder= new Project.Builder(PROJECT_9, factory);
        builder.add(UITHREAD_TASK_A);
        builder.add(UITHREAD_TASK_B).dependOn(UITHREAD_TASK_A);
        builder.add(UITHREAD_TASK_C).dependOn(UITHREAD_TASK_B);
        Project project = builder.build();
        getInstance().debuggable(true).addAnchor(UITHREAD_TASK_C)
                .start(project);
    }
}
