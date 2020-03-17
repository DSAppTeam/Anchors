package com.effective.android.sample.data

import com.effective.android.anchors.Project.TaskFactory
import com.effective.android.anchors.Task
import com.effective.android.anchors.TaskCreator
import java.util.*

abstract class TestTask(
    id: String,
    isAsyncTask: Boolean = false //是否是异步存在
) : Task(id, isAsyncTask) {

    fun doIo(millis: Long) {
        try {
            Thread.sleep(millis)
        } catch (e: Exception) {
        }
    }

    fun doJob(millis: Long) {
        val nowTime = System.currentTimeMillis()
        while (System.currentTimeMillis() < nowTime + millis) {
            //程序阻塞指定时间
            val min = 10
            val max = 99
            val random = Random()
            val num = random.nextInt(max) % (max - min + 1) + min
        }
    }
}

class TASK_10 : TestTask(TaskTest.TASK_10, true) {
    override fun run(name: String) {
        doJob(1000)
    }
}

class TASK_11 : TestTask(TaskTest.TASK_11, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_12 : TestTask(TaskTest.TASK_12, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_13 : TestTask(TaskTest.TASK_13, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_20 : TestTask(TaskTest.TASK_20, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_21 : TestTask(TaskTest.TASK_21, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_22 : TestTask(TaskTest.TASK_22, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_23 : TestTask(TaskTest.TASK_23, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_30 : TestTask(TaskTest.TASK_30, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_31 : TestTask(TaskTest.TASK_31, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_32 : TestTask(TaskTest.TASK_32, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_33 : TestTask(TaskTest.TASK_33, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_40 : TestTask(TaskTest.TASK_40, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_41 : TestTask(TaskTest.TASK_41, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_42 : TestTask(TaskTest.TASK_42, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_43 : TestTask(TaskTest.TASK_43, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_50 : TestTask(TaskTest.TASK_50, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_51 : TestTask(TaskTest.TASK_51, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_52 : TestTask(TaskTest.TASK_52, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_53 : TestTask(TaskTest.TASK_53, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_60 : TestTask(TaskTest.TASK_60, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_61 : TestTask(TaskTest.TASK_61, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_62 : TestTask(TaskTest.TASK_62, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_63 : TestTask(TaskTest.TASK_63, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_70 : TestTask(TaskTest.TASK_70, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_71 : TestTask(TaskTest.TASK_71, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_72 : TestTask(TaskTest.TASK_72, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_73 : TestTask(TaskTest.TASK_73, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_80 : TestTask(TaskTest.TASK_80, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_81 : TestTask(TaskTest.TASK_81, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_82 : TestTask(TaskTest.TASK_82, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_83 : TestTask(TaskTest.TASK_83, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_90 : TestTask(TaskTest.TASK_90, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_91 : TestTask(TaskTest.TASK_91, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_92 : TestTask(TaskTest.TASK_92) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_93 : TestTask(TaskTest.TASK_93, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class UITHREAD_TASK_A : TestTask(TaskTest.UITHREAD_TASK_A) {
    override fun run(name: String) {
        doJob(200)
    }
}

class UITHREAD_TASK_B : TestTask(TaskTest.UITHREAD_TASK_B) {
    override fun run(name: String) {
        doJob(200)
    }
}

class UITHREAD_TASK_C : TestTask(TaskTest.UITHREAD_TASK_C) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_1 : TestTask(TaskTest.ASYNC_TASK_1, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_2 : TestTask(TaskTest.ASYNC_TASK_2, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_3 : TestTask(TaskTest.ASYNC_TASK_3, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_4 : TestTask(TaskTest.ASYNC_TASK_4, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_5 : TestTask(TaskTest.ASYNC_TASK_5, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

object TestTaskCreator : TaskCreator {
    override fun createTask(taskName: String): Task? {
        when (taskName) {
            TaskTest.TASK_10 -> {
                val task_10 = TASK_10()
                task_10.priority = 10
                return task_10
            }
            TaskTest.TASK_11 -> {
                val TASK_11 = TASK_11()
                TASK_11.priority = 10
                return TASK_11
            }
            TaskTest.TASK_12 -> {
                val TASK_12 = TASK_12()
                TASK_12.priority = 10
                return TASK_12
            }
            TaskTest.TASK_13 -> {
                val TASK_13 = TASK_13()
                TASK_13.priority = 10
                return TASK_13
            }
            TaskTest.TASK_20 -> {
                return TASK_20()
            }
            TaskTest.TASK_21 -> {
                return TASK_21()
            }
            TaskTest.TASK_22 -> {
                return TASK_22()
            }
            TaskTest.TASK_23 -> {
                return TASK_23()
            }
            TaskTest.TASK_30 -> {
                return TASK_30()
            }
            TaskTest.TASK_31 -> {
                return TASK_31()
            }
            TaskTest.TASK_32 -> {
                return TASK_32()
            }
            TaskTest.TASK_33 -> {
                return TASK_33()
            }
            TaskTest.TASK_40 -> {
                return TASK_40()
            }
            TaskTest.TASK_41 -> {
                return TASK_41()
            }
            TaskTest.TASK_42 -> {
                return TASK_42()
            }
            TaskTest.TASK_43 -> {
                return TASK_43()
            }
            TaskTest.TASK_50 -> {
                return TASK_50()
            }
            TaskTest.TASK_51 -> {
                return TASK_51()
            }
            TaskTest.TASK_52 -> {
                return TASK_52()
            }
            TaskTest.TASK_53 -> {
                return TASK_53()
            }
            TaskTest.TASK_60 -> {
                return TASK_60()
            }
            TaskTest.TASK_61 -> {
                return TASK_61()
            }
            TaskTest.TASK_62 -> {
                return TASK_62()
            }
            TaskTest.TASK_63 -> {
                return TASK_63()
            }
            TaskTest.TASK_70 -> {
                return TASK_70()
            }
            TaskTest.TASK_71 -> {
                return TASK_71()
            }
            TaskTest.TASK_72 -> {
                return TASK_72()
            }
            TaskTest.TASK_73 -> {
                return TASK_73()
            }
            TaskTest.TASK_80 -> {
                return TASK_80()
            }
            TaskTest.TASK_81 -> {
                return TASK_81()
            }
            TaskTest.TASK_82 -> {
                return TASK_82()
            }
            TaskTest.TASK_83 -> {
                return TASK_83()
            }
            TaskTest.TASK_90 -> {
                return TASK_90()
            }
            TaskTest.TASK_91 -> {
                return TASK_91()
            }
            TaskTest.TASK_92 -> {
                return TASK_92()
            }
            TaskTest.TASK_93 -> {
                return TASK_93()
            }
            TaskTest.UITHREAD_TASK_A -> {
                return UITHREAD_TASK_A()
            }
            TaskTest.UITHREAD_TASK_B -> {
                return UITHREAD_TASK_B()
            }
            TaskTest.UITHREAD_TASK_C -> {
                return UITHREAD_TASK_C()
            }
            TaskTest.ASYNC_TASK_1 -> {
                return ASYNC_TASK_1()
            }
            TaskTest.ASYNC_TASK_2 -> {
                return ASYNC_TASK_2()
            }
            TaskTest.ASYNC_TASK_3 -> {
                return ASYNC_TASK_3()
            }
            TaskTest.ASYNC_TASK_4 -> {
                return ASYNC_TASK_4()
            }
            TaskTest.ASYNC_TASK_5 -> {
                return ASYNC_TASK_5()
            }
        }
        return null
    }
}

class TestTaskFactory : TaskFactory(TestTaskCreator)