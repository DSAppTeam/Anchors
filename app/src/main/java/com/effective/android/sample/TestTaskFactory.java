package com.effective.android.sample;

import android.support.annotation.Nullable;


import com.effective.android.anchors.Project;
import com.effective.android.anchors.Task;
import com.effective.android.anchors.TaskCreator;

import java.util.Random;

public class TestTaskFactory extends Project.TaskFactory {

    public TestTaskFactory() {
        super(new TaskCreator() {
            @Nullable
            @Override
            public Task createTask(String taskName) {
                switch (taskName) {
                    case TaskTest.TASK_10: {
                        TASK_10 task_10 = new TASK_10();
                        task_10.setPriority(10);
                        return task_10;
                    }
                    case TaskTest.TASK_11: {
                        TASK_11 TASK_11 = new TASK_11();
                        TASK_11.setPriority(10);
                        return TASK_11;
                    }
                    case TaskTest.TASK_12: {
                        TASK_12 TASK_12 = new TASK_12();
                        TASK_12.setPriority(10);
                        return TASK_12;
                    }
                    case TaskTest.TASK_13: {
                        TASK_13 TASK_13 = new TASK_13();
                        TASK_13.setPriority(10);
                        return TASK_13;
                    }
                    case TaskTest.TASK_20: {
                        return new TASK_20();
                    }
                    case TaskTest.TASK_21: {
                        return new TASK_21();
                    }
                    case TaskTest.TASK_22: {
                        return new TASK_22();
                    }
                    case TaskTest.TASK_23: {
                        return new TASK_23();
                    }
                    case TaskTest.TASK_30: {
                        return new TASK_30();
                    }
                    case TaskTest.TASK_31: {
                        return new TASK_31();
                    }
                    case TaskTest.TASK_32: {
                        return new TASK_32();
                    }
                    case TaskTest.TASK_33: {
                        return new TASK_33();
                    }
                    case TaskTest.TASK_40: {
                        return new TASK_40();
                    }
                    case TaskTest.TASK_41: {
                        return new TASK_41();
                    }
                    case TaskTest.TASK_42: {
                        return new TASK_42();
                    }
                    case TaskTest.TASK_43: {
                        return new TASK_43();
                    }
                    case TaskTest.TASK_50: {
                        return new TASK_50();
                    }
                    case TaskTest.TASK_51: {
                        return new TASK_51();
                    }
                    case TaskTest.TASK_52: {
                        return new TASK_52();
                    }
                    case TaskTest.TASK_53: {
                        return new TASK_53();
                    }

                    case TaskTest.TASK_60: {
                        return new TASK_60();
                    }
                    case TaskTest.TASK_61: {
                        return new TASK_61();
                    }
                    case TaskTest.TASK_62: {
                        return new TASK_62();
                    }
                    case TaskTest.TASK_63: {
                        return new TASK_63();
                    }
                    case TaskTest.TASK_70: {
                        return new TASK_70();
                    }
                    case TaskTest.TASK_71: {
                        return new TASK_71();
                    }
                    case TaskTest.TASK_72: {
                        return new TASK_72();
                    }
                    case TaskTest.TASK_73: {
                        return new TASK_73();
                    }
                    case TaskTest.TASK_80: {
                        return new TASK_80();
                    }
                    case TaskTest.TASK_81: {
                        return new TASK_81();
                    }
                    case TaskTest.TASK_82: {
                        return new TASK_82();
                    }
                    case TaskTest.TASK_83: {
                        return new TASK_83();
                    }
                    case TaskTest.TASK_90: {
                        return new TASK_90();
                    }
                    case TaskTest.TASK_91: {
                        return new TASK_91();
                    }
                    case TaskTest.TASK_92: {
                        return new TASK_92();
                    }
                    case TaskTest.TASK_93: {
                        return new TASK_93();
                    }
                    case TaskTest.UITHREAD_TASK_A: {
                        return new UITHREAD_TASK_A();
                    }
                    case TaskTest.UITHREAD_TASK_B: {
                        return new UITHREAD_TASK_B();
                    }
                    case TaskTest.UITHREAD_TASK_C: {
                        return new UITHREAD_TASK_C();
                    }
                    case TaskTest.ASYNC_TASK_1: {
                        return new ASYNC_TASK_1();
                    }
                    case TaskTest.ASYNC_TASK_2: {
                        return new ASYNC_TASK_2();
                    }
                    case TaskTest.ASYNC_TASK_3: {
                        return new ASYNC_TASK_3();
                    }
                    case TaskTest.ASYNC_TASK_4: {
                        return new ASYNC_TASK_4();
                    }
                    case TaskTest.ASYNC_TASK_5: {
                        return new ASYNC_TASK_5();
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
            doJob(1000);
        }
    }

    public static class TASK_11 extends Task {

        public TASK_11() {
            super(TaskTest.TASK_11,true);
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

    public static class TASK_30 extends Task {

        public TASK_30() {
            super(TaskTest.TASK_30, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_31 extends Task {

        public TASK_31() {
            super(TaskTest.TASK_31, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_32 extends Task {

        public TASK_32() {
            super(TaskTest.TASK_32, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_33 extends Task {

        public TASK_33() {
            super(TaskTest.TASK_33, true);
        }

        @Override
        protected void run(String name) {
            doIo(200);
        }
    }

    public static class TASK_40 extends Task {

        public TASK_40() {
            super(TaskTest.TASK_40, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_41 extends Task {

        public TASK_41() {
            super(TaskTest.TASK_41, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_42 extends Task {

        public TASK_42() {
            super(TaskTest.TASK_42, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_43 extends Task {

        public TASK_43() {
            super(TaskTest.TASK_43, true);
        }

        @Override
        protected void run(String name) {
            doIo(200);
        }
    }

    public static class TASK_50 extends Task {

        public TASK_50() {
            super(TaskTest.TASK_50, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_51 extends Task {

        public TASK_51() {
            super(TaskTest.TASK_51, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_52 extends Task {

        public TASK_52() {
            super(TaskTest.TASK_52, true);
        }

        @Override
        protected void run(String name) {
            doIo(200);
        }
    }

    public static class TASK_53 extends Task {

        public TASK_53() {
            super(TaskTest.TASK_53, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_60 extends Task {

        public TASK_60() {
            super(TaskTest.TASK_60,true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_61 extends Task {

        public TASK_61() {
            super(TaskTest.TASK_61,true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_62 extends Task {

        public TASK_62() {
            super(TaskTest.TASK_62,true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_63 extends Task {

        public TASK_63() {
            super(TaskTest.TASK_63, true);
        }

        @Override
        protected void run(String name) {
            doIo(200);
        }
    }

    public static class TASK_70 extends Task {

        public TASK_70() {
            super(TaskTest.TASK_70, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_71 extends Task {

        public TASK_71() {
            super(TaskTest.TASK_71, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_72 extends Task {

        public TASK_72() {
            super(TaskTest.TASK_72, true);
        }

        @Override
        protected void run(String name) {
            doIo(200);
        }
    }

    public static class TASK_73 extends Task {

        public TASK_73() {
            super(TaskTest.TASK_73, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_80 extends Task {

        public TASK_80() {
            super(TaskTest.TASK_80, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_81 extends Task {

        public TASK_81() {
            super(TaskTest.TASK_81, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_82 extends Task {

        public TASK_82() {
            super(TaskTest.TASK_82, true);
        }

        @Override
        protected void run(String name) {
            doIo(200);
        }
    }

    public static class TASK_83 extends Task {

        public TASK_83() {
            super(TaskTest.TASK_83, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_90 extends Task {

        public TASK_90() {
            super(TaskTest.TASK_90, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_91 extends Task {

        public TASK_91() {
            super(TaskTest.TASK_91, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class TASK_92 extends Task {

        public TASK_92() {
            super(TaskTest.TASK_92);
        }

        @Override
        protected void run(String name) {
            doIo(200);
        }
    }

    public static class TASK_93 extends Task {

        public TASK_93() {
            super(TaskTest.TASK_93, true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }


    public static class UITHREAD_TASK_A extends Task {

        public UITHREAD_TASK_A() {
            super(TaskTest.UITHREAD_TASK_A);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class UITHREAD_TASK_B extends Task {

        public UITHREAD_TASK_B() {
            super(TaskTest.UITHREAD_TASK_B);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class UITHREAD_TASK_C extends Task {

        public UITHREAD_TASK_C() {
            super(TaskTest.UITHREAD_TASK_C);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class ASYNC_TASK_1 extends Task {

        public ASYNC_TASK_1() {
            super(TaskTest.ASYNC_TASK_1,true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class ASYNC_TASK_2 extends Task {

        public ASYNC_TASK_2() {
            super(TaskTest.ASYNC_TASK_2,true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class ASYNC_TASK_3 extends Task {

        public ASYNC_TASK_3() {
            super(TaskTest.ASYNC_TASK_3,true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class ASYNC_TASK_4 extends Task {

        public ASYNC_TASK_4() {
            super(TaskTest.ASYNC_TASK_4,true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }

    public static class ASYNC_TASK_5 extends Task {

        public ASYNC_TASK_5() {
            super(TaskTest.ASYNC_TASK_5,true);
        }

        @Override
        protected void run(String name) {
            doJob(200);
        }
    }


}
