package com.effective.android.anchors;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Project extends Task {

    private Task endTask;
    private Task startTask;

    private Project(String id) {
        super(id);
    }

    @NonNull
    protected Task getStartTask() {
        return startTask;
    }

    @NonNull
    protected Task getEndTask() {
        return endTask;
    }

    @Override
    protected void behind(Task task) {
        endTask.behind(task);
    }

    @Override
    public void dependOn(Task task) {
        startTask.dependOn(task);
    }

    @Override
    protected void removeBehind(Task task) {
        endTask.removeBehind(task);
    }

    @Override
    protected void removeDependence(Task task) {
        startTask.removeDependence(task);
    }

    @Override
    protected synchronized void start() {
        startTask.start();
    }


    @Override
    public void run(String name) {
        //不需要处理
    }

    @Override
    void recycle() {
        super.recycle();
        endTask = null;
        startTask = null;
    }

    public static class Builder {

        private Task mCurrentAddTask;
        private Task mFinishTask;
        private Task mStartTask;
        private boolean mCurrentTaskShouldDependOnStartTask;
        private Project mProject;
        private TaskFactory mFactory;
        private int mPriority;              //默认project优先级为project内所有task的优先级，如果没有设置则取 max(project内所有task的)


        public Builder(@NonNull String projectName, @NonNull TaskFactory taskFactory) {
            this.mCurrentAddTask = null;
            this.mCurrentTaskShouldDependOnStartTask = false;
            this.mProject = new Project(projectName);
            long criticalTime = System.currentTimeMillis();
            this.mStartTask = new CriticalTask(projectName + "_start(" + criticalTime + ")");
            this.mFinishTask = new CriticalTask(projectName + "_end(" + criticalTime + ")");
            this.mFactory = taskFactory;
            if (mFactory == null) {
                throw new IllegalArgumentException("taskFactory cant's be null");
            }
        }

        public Project build() {
            if (mCurrentAddTask != null) {
                if (mCurrentTaskShouldDependOnStartTask) {
                    mStartTask.behind(mCurrentAddTask);
                }
            } else {
                mStartTask.behind(mFinishTask);
            }
            mStartTask.setPriority(mPriority);
            mFinishTask.setPriority(mPriority);
            mProject.startTask = mStartTask;
            mProject.endTask = mFinishTask;
            return mProject;
        }


        public Builder add(String taskName) {
            Task task = mFactory.getTask(taskName);
            if (task.getPriority() > mPriority) {
                mPriority = task.getPriority();
            }
            return add(mFactory.getTask(taskName));
        }

        public Builder add(Task task) {
            if (mCurrentTaskShouldDependOnStartTask && mCurrentAddTask != null) {
                mStartTask.behind(mCurrentAddTask);
            }
            mCurrentAddTask = task;
            mCurrentTaskShouldDependOnStartTask = true;
            mCurrentAddTask.behind(mFinishTask);
            return this;
        }

        public Builder dependOn(String taskName) {
            return dependOn(mFactory.getTask(taskName));
        }

        public Builder dependOn(Task task) {
            task.behind(mCurrentAddTask);
            mFinishTask.removeDependence(task);
            mCurrentTaskShouldDependOnStartTask = false;
            return this;
        }

        public Builder dependOn(String... names) {
            if (names != null & names.length > 0) {
                for (String name : names) {
                    Task task = mFactory.getTask(name);
                    task.behind(mCurrentAddTask);
                    mFinishTask.removeDependence(task);
                }
                mCurrentTaskShouldDependOnStartTask = false;
            }
            return Builder.this;
        }
    }

    public static class TaskFactory {

        private Map<String, Task> mCacheTask;
        private ITaskCreator mTaskCreator;

        public TaskFactory(ITaskCreator creator) {
            mTaskCreator = creator;
            mCacheTask = new HashMap<>();
        }

        public synchronized Task getTask(String taskId) {
            Task task = mCacheTask.get(taskId);

            if (task != null) {
                return task;
            }
            task = mTaskCreator.createTask(taskId);

            if (task == null) {
                throw new IllegalArgumentException("Create task fail. Make sure ITaskCreator can create a task with only taskId");
            }
            mCacheTask.put(taskId, task);
            return task;
        }
    }

    /**
     * 作为临界节点，标识 project 的开始和结束。
     * 同个 project 下可能需要等待 {次后节点们} 统一结束直接才能进入结束节点。
     */
    private static class CriticalTask extends Task {

        public CriticalTask(String name) {
            super(name);
        }

        @Override
        public void run(String name) {
            //noting to do
        }
    }
}
