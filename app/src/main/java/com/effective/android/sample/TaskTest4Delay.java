package com.effective.android.sample;

import android.support.annotation.Nullable;

import com.effective.android.anchors.AnchorsManager;
import com.effective.android.anchors.ITaskCreator;
import com.effective.android.anchors.Project;
import com.effective.android.anchors.Task;

import java.util.Random;
import java.util.Set;

import static com.effective.android.sample.TaskTest.TASK_30;
import static com.effective.android.sample.TaskTest.TASK_31;
import static com.effective.android.sample.TaskTest.TASK_32;

/**
 * Created by timian on 2019-07-03.
 */
class TaskTest4Delay {

    public static final String PROJECT_1 = "PROJECT_1";
    public static final String TASK_10 = "TASK_10";
    public static final String TASK_11 = "TASK_11";
    public static final String TASK_12 = "TASK_12";
    public static final String TASK_13 = "TASK_13";

    public static final String TASK_20 = "TASK_20";
    public static final String TASK_21 = "TASK_21";
    public static final String TASK_22 = "TASK_22";
    public static final String TASK_23 = "TASK_23";

    public static final String TASK_DELAY_30 = "TASK_30";
    public static final String TASK_DELAY_31 = "TASK_31";
    public static final String TASK_DELAY_32 = "TASK_32";
    public static final String TASK_DELAY_33 = "TASK_33";


    public void start() {

        final Test4DelayFactory test4DelayFactory = new Test4DelayFactory();

        Project.Builder builder = new Project.Builder(PROJECT_1, test4DelayFactory);
        builder.add(TASK_10);
        builder.add(TASK_11).dependOn(TASK_10);
        builder.add(TASK_12).dependOn(TASK_11);
        builder.add(TASK_13).dependOn(TASK_12);

        builder.add(TASK_DELAY_30)
                .add(TASK_20).dependOn(TASK_DELAY_30)
                .add(TASK_21).dependOn(TASK_20)
                .add(TASK_22).dependOn(TASK_21)
                .add(TASK_23);

        builder.add(TASK_DELAY_31)
                .add(TASK_DELAY_32)
                .add(TASK_DELAY_33);
        Project project = builder.build();


        AnchorsManager.getInstance().debuggable(true)
                .addAnchors(TASK_21, TASK_12, TASK_13)
                .start(project);
    }


    private static class Test4DelayFactory extends Project.TaskFactory {

        public Test4DelayFactory() {
            super(new ITaskCreator() {
                @Nullable
                @Override
                public Task createTask(String taskName) {
                    switch (taskName) {
                        case TASK_10: {
                            TASK_10 task_10 = new TASK_10();
                            task_10.setPriority(10);
                            return task_10;
                        }
                        case TASK_11: {
                            TASK_11 TASK_11 = new TASK_11();
                            TASK_11.setPriority(10);
                            return TASK_11;
                        }
                        case TASK_12: {
                            TASK_12 TASK_12 = new TASK_12();
                            TASK_12.setPriority(10);
                            return TASK_12;
                        }
                        case TASK_13: {
                            TASK_13 TASK_13 = new TASK_13();
                            TASK_13.setPriority(10);
                            return TASK_13;
                        }
                        case TASK_20: {
                            return new TASK_20();
                        }
                        case TASK_21: {
                            return new TASK_21();
                        }
                        case TASK_22: {
                            return new TASK_22();
                        }
                        case TASK_23: {
                            return new TASK_23();
                        }

                        case TASK_DELAY_30: {
                            return new DelayTask(taskName, false, 1000);
                        }

                        case TASK_DELAY_31: {
                            return new DelayTask(taskName, true, 2000);   //异步任务延迟无效，for test
                        }

                        case TASK_DELAY_32: {
                            return new DelayTask(taskName, false, 3000);
                        }

                        case TASK_DELAY_33: {
                            return new DelayTask(taskName, true, 4000);    //异步任务延迟无效,for test
                        }
                    }
                    return null;
                }
            });
        }

        public static void doIo(long millis) {
            try {
                Thread.sleep(millis);
            } catch (Exception e) {

            }
        }

        public static void doJob(long millis) {
            long nowTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < nowTime + millis) {
                //程序阻塞指定时间
                int min = 10;
                int max = 99;
                Random random = new Random();
                int num = random.nextInt(max) % (max - min + 1) + min;
            }
        }

        public static class TASK_10 extends Task {

            public TASK_10() {
                super(TaskTest.TASK_10, true);
            }

            @Override
            protected void run(String name) {
                doJob(200);
            }
        }

        public static class TASK_11 extends Task {

            public TASK_11() {
                super(TaskTest.TASK_11, true);
            }

            @Override
            protected void run(String name) {
                doJob(200);
            }
        }

        public static class TASK_12 extends Task {

            public TASK_12() {
                super(TaskTest.TASK_12, true);
            }

            @Override
            protected void run(String name) {
                doJob(200);
            }
        }

        public static class TASK_13 extends Task {

            public TASK_13() {
                super(TaskTest.TASK_13, true);
            }

            @Override
            protected void run(String name) {
                doJob(200);
            }
        }

        public static class TASK_20 extends Task {

            public TASK_20() {
                super(TaskTest.TASK_20, true);
            }

            @Override
            protected void run(String name) {
                doJob(200);
            }
        }

        public static class TASK_21 extends Task {

            public TASK_21() {
                super(TaskTest.TASK_21, true);
            }

            @Override
            protected void run(String name) {
                doJob(200);
            }
        }

        public static class TASK_22 extends Task {

            public TASK_22() {
                super(TaskTest.TASK_22, true);
            }

            @Override
            protected void run(String name) {
                doIo(200);
            }
        }

        public static class TASK_23 extends Task {

            public TASK_23() {
                super(TaskTest.TASK_23, true);
            }

            @Override
            protected void run(String name) {
                doJob(200);
            }
        }


        public static class DelayTask extends Task {

            private long delayMills;

            public DelayTask(String taskName, boolean async, long delayMills) {
                super(taskName, async);
                this.delayMills = delayMills;
            }

            @Override
            protected void run(String name) {
                doJob(200);
            }

            @Override
            public long getDelayMills() {
                return delayMills;
            }
        }
    }
}
