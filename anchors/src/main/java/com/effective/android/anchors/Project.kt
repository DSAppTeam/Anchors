package com.effective.android.anchors


class Project private constructor(id: String) : Task(id) {
    lateinit var endTask: Task
    lateinit var startTask: Task

    override fun behind(task: Task) {
        endTask.behind(task)
    }

    override fun dependOn(task: Task) {
        startTask.dependOn(task)
    }

    override fun removeBehind(task: Task) {
        endTask.removeBehind(task)
    }

    override fun removeDependence(task: Task) {
        startTask.removeDependence(task)
    }

    @Synchronized
    override fun start() {
        startTask.start()
    }

    public override fun run(name: String) { //不需要处理
    }

    override fun release() {
        super.release()
        endTask.release()
        startTask.release()
    }

    /**
     * project 的构建内部，避免了回环的发生。
     * 当出现project 内 task 循环依赖是，循环依赖会自动断开。
     */
    class Builder(projectName: String, val taskFactory: TaskFactory) {
        private var mCurrentAddTask: Task? = null
        private val mFinishTask: Task
        private val mStartTask: Task
        private var mCurrentTaskShouldDependOnStartTask = false
        private val mProject: Project = Project(projectName)
        private var mPriority //默认project优先级为project内所有task的优先级，如果没有设置则取 max(project内所有task的)
                = 0

        fun build(): Project {
            if (mCurrentAddTask != null) {
                if (mCurrentTaskShouldDependOnStartTask) {
                    mStartTask.behind(mCurrentAddTask!!)
                }
            } else {
                mStartTask.behind(mFinishTask)
            }
            mStartTask.priority = mPriority
            mFinishTask.priority = mPriority
            mProject.startTask = mStartTask
            mProject.endTask = mFinishTask
            return mProject
        }

        fun add(taskName: String?): Builder {
            val task = taskFactory.getTask(taskName)
            if (task.priority > mPriority) {
                mPriority = task.priority
            }
            return add(taskFactory.getTask(taskName))
        }

        fun add(task: Task?): Builder {
            if (mCurrentTaskShouldDependOnStartTask && mCurrentAddTask != null) {
                mStartTask.behind(mCurrentAddTask!!)
            }
            mCurrentAddTask = task
            mCurrentTaskShouldDependOnStartTask = true
            mCurrentAddTask!!.behind(mFinishTask)
            return this
        }

        fun dependOn(taskName: String?): Builder {
            return dependOn(taskFactory.getTask(taskName))
        }

        fun dependOn(task: Task): Builder {
            task.behind(mCurrentAddTask!!)
            mFinishTask.removeDependence(task)
            mCurrentTaskShouldDependOnStartTask = false
            return this
        }

        fun dependOn(vararg names: String): Builder {
            if (names.isNotEmpty()) {
                for (name in names) {
                    val task = taskFactory.getTask(name)
                    task.behind(mCurrentAddTask!!)
                    mFinishTask.removeDependence(task)
                }
                mCurrentTaskShouldDependOnStartTask = false
            }
            return this@Builder
        }

        init {
            val criticalTime = System.currentTimeMillis()
            mStartTask = CriticalTask(projectName + "_start(" + criticalTime + ")")
            mFinishTask = CriticalTask(projectName + "_end(" + criticalTime + ")")
        }
    }

    open class TaskFactory {

        private val mCacheTask = mutableMapOf<String, Task>()
        private val mTaskCreator: TaskCreator

        constructor(taskCreator: TaskCreator) {
            mTaskCreator = taskCreator
        }

        constructor(init: TaskCreatorBuilder.() -> Unit) {
            val builder = TaskCreatorBuilder()
            mTaskCreator = object : TaskCreator {
                override fun createTask(taskName: String): Task {
                    return builder.also(init).createTask.invoke(taskName);
                }
            }
        }


        @Synchronized
        fun getTask(taskId: String?): Task {
            var task = mCacheTask[taskId]
            if (task != null) {
                return task
            }
            task = mTaskCreator.createTask(taskId!!)
            mCacheTask[taskId] = task
            return task
        }
    }


    /**
     * 作为临界节点，标识 project 的开始和结束。
     * 同个 project 下可能需要等待 {次后节点们} 统一结束直接才能进入结束节点。
     */
    private class CriticalTask internal constructor(name: String) : Task(name) {
        public override fun run(name: String) { //noting to do
        }
    }
}

