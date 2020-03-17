package com.effective.android.anchors

class Project(id: String) : Task(id) {
    lateinit var endTask: Task
    lateinit var startTask: Task

    override fun behindBy(task: Task) {
        endTask.behindBy(task)
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

    override fun run(name: String) {
        //不需要处理
    }

    /**
     * project 的构建内部，避免了回环的发生。
     * 当出现project 内 task 循环依赖是，循环依赖会自动断开。
     */
    class Builder(projectName: String, val taskFactory: TaskFactory) {
        private val mStartTask: Task
        private val mFinishTask: Task
        private var mCurrentAddTask: Task? = null
        private var mCurrentTaskShouldDependOnStartTask = false
        private val mProject = Project(projectName)
        private var mPriority = 0 //默认project优先级为project内所有task的优先级，如果没有设置则取 max(project内所有task的)

        init {
            val criticalTime = System.currentTimeMillis()
            mStartTask = CriticalTask(projectName + "_start(" + criticalTime + ")")
            mFinishTask = CriticalTask(projectName + "_end(" + criticalTime + ")")
        }

        fun build(): Project {
            if (mCurrentAddTask != null) {
                if (mCurrentTaskShouldDependOnStartTask) {
                    mStartTask.behindBy(mCurrentAddTask!!)
                }
            } else {
                mStartTask.behindBy(mFinishTask)
            }
            mStartTask.priority = mPriority
            mFinishTask.priority = mPriority
            mProject.startTask = mStartTask
            mProject.endTask = mFinishTask
            return mProject
        }

        fun add(taskName: String): Builder {
            val task = taskFactory.getTask(taskName)
            if (task.priority > mPriority) {
                mPriority = task.priority
            }
            return add(task)
        }

        fun add(task: Task): Builder {
            if (mCurrentTaskShouldDependOnStartTask && mCurrentAddTask != null) {
                mStartTask.behindBy(mCurrentAddTask!!)
            }
            mCurrentAddTask = task
            mCurrentTaskShouldDependOnStartTask = true
            task.behindBy(mFinishTask)
            return this
        }

        fun dependOn(taskName: String): Builder {
            return dependOn(taskFactory.getTask(taskName))
        }

        fun dependOn(task: Task): Builder {
            task.behindBy(mCurrentAddTask!!)
            mFinishTask.removeDependence(task)
            mCurrentTaskShouldDependOnStartTask = false
            return this
        }

        fun dependOn(vararg names: String): Builder {
            if (names.isNotEmpty()) {
                for (name in names) {
                    val task = taskFactory.getTask(name)
                    task.behindBy(mCurrentAddTask!!)
                    mFinishTask.removeDependence(task)
                }
                mCurrentTaskShouldDependOnStartTask = false
            }
            return this@Builder
        }
    }

    open class TaskFactory(private val mTaskCreator: TaskCreator) {
        private val mCacheTask= mutableMapOf<String, Task>()
        @Synchronized
        fun getTask(taskId: String): Task {
            var task = mCacheTask[taskId]
            if (task != null) {
                return task
            }
            task = mTaskCreator.createTask(taskId)
            requireNotNull(task) { "Create task fail. Make sure TaskCreator can create a task with only taskId" }
            mCacheTask[taskId] = task
            return task
        }
    }

    /**
     * 作为临界节点，标识 project 的开始和结束。
     * 同个 project 下可能需要等待 {次后节点们} 统一结束直接才能进入结束节点。
     */
    private class CriticalTask internal constructor(name: String) : Task(name) {
        public override fun run(name: String) {
            //noting to do
        }
    }
}