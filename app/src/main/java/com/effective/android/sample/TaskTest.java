package com.effective.android.sample;

import android.text.TextUtils;
import android.util.Log;

import com.effective.android.anchors.AnchorsManager;
import com.effective.android.anchors.AnchorsRuntime;
import com.effective.android.anchors.Project;
import com.effective.android.anchors.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class TaskTest {

    public static final String PROJECT_1 = "PROJECT_1";
    public static final String TASK_10 = "TASK_10";
    public static final String TASK_11 = "TASK_11";
    public static final String TASK_12 = "TASK_12";
    public static final String TASK_13 = "TASK_13";

    public static final String PROJECT_2 = "PROJECT_2";
    public static final String TASK_20 = "TASK_20";
    public static final String TASK_21 = "TASK_21";
    public static final String TASK_22 = "TASK_22";
    public static final String TASK_23 = "TASK_23";

    public static final String PROJECT_3 = "PROJECT_3";
    public static final String TASK_30 = "TASK_30";
    public static final String TASK_31 = "TASK_31";
    public static final String TASK_32 = "TASK_32";
    public static final String TASK_33 = "TASK_33";

    public static final String PROJECT_4 = "PROJECT_4";
    public static final String TASK_40 = "TASK_40";
    public static final String TASK_41 = "TASK_41";
    public static final String TASK_42 = "TASK_42";
    public static final String TASK_43 = "TASK_43";

    public static final String PROJECT_5 = "PROJECT_5";
    public static final String TASK_50 = "TASK_50";
    public static final String TASK_51 = "TASK_51";
    public static final String TASK_52 = "TASK_52";
    public static final String TASK_53 = "TASK_53";

    public static final String PROJECT_6 = "PROJECT_6";
    public static final String TASK_60 = "TASK_60";
    public static final String TASK_61 = "TASK_61";
    public static final String TASK_62 = "TASK_62";
    public static final String TASK_63 = "TASK_63";

    public static final String PROJECT_7 = "PROJECT_7";
    public static final String TASK_70 = "TASK_70";
    public static final String TASK_71 = "TASK_71";
    public static final String TASK_72 = "TASK_72";
    public static final String TASK_73 = "TASK_73";

    public static final String PROJECT_8 = "PROJECT_8";
    public static final String TASK_80 = "TASK_80";
    public static final String TASK_81 = "TASK_81";
    public static final String TASK_82 = "TASK_82";
    public static final String TASK_83 = "TASK_83";

    public static final String PROJECT_9 = "PROJECT_9";
    public static final String TASK_90 = "TASK_90";
    public static final String TASK_91 = "TASK_91";
    public static final String TASK_92 = "TASK_92";
    public static final String TASK_93 = "TASK_93";


    public static final String TASK_A = "TASK_A";
    public static final String TASK_B = "TASK_B";
    public static final String TASK_C = "TASK_C";
    public static final String TASK_D = "TASK_D";


    public void start() {

       final  TestTaskFactory testTaskFactory = new TestTaskFactory();

        Project.Builder builder1 = new Project.Builder(PROJECT_1, testTaskFactory);
        builder1.add(TASK_10);
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

        Project.Builder builder8 = new Project.Builder(PROJECT_8, testTaskFactory);
        builder8.add(TASK_80);
        builder8.add(TASK_81).dependOn(TASK_80);
        builder8.add(TASK_82).dependOn(TASK_80);
        builder8.add(TASK_83).dependOn(TASK_82);
        Project project8 = builder8.build();

        Project.Builder builder9 = new Project.Builder(PROJECT_9, testTaskFactory);
        builder9.add(TASK_90);
        builder9.add(TASK_91).dependOn(TASK_90);
        builder9.add(TASK_92).dependOn(TASK_91);
        builder9.add(TASK_93).dependOn(TASK_92);
        Project project9 = builder9.build();


//
        final Task taskA = new TestTaskFactory.TASK_A();
        Task taskB = new TestTaskFactory.TASK_B();
        Task taskC = new TestTaskFactory.TASK_C();
        Task taskD = new TestTaskFactory.TASK_D();

        project9.dependOn(taskA);
        project8.dependOn(taskA);
        project7.dependOn(taskA);
        project6.dependOn(taskA);
        project5.dependOn(taskA);
        project4.dependOn(taskA);
        project3.dependOn(taskA);
        project2.dependOn(taskA);
        project1.dependOn(taskA);

        taskB.dependOn(taskA);
        taskC.dependOn(taskB);
        taskD.dependOn(taskB);
//        taskB.dependOn(taskD);
//
        AnchorsManager.getInstance()
                .debuggable(true)
                .addAnchors(TASK_A, TASK_90, TASK_12, TASK_42)
                .start(taskA);
    }
}
