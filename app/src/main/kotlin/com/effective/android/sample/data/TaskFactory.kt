package com.effective.android.sample.data

import com.effective.android.anchors.task.project.Project.TaskFactory
import com.effective.android.anchors.task.Task
import com.effective.android.anchors.task.TaskCreator
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

class TASK_10 : TestTask(Datas.TASK_10, true) {
    override fun run(name: String) {
        doJob(1000)
    }
}

class TASK_11 : TestTask(Datas.TASK_11, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_12 : TestTask(Datas.TASK_12, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_13 : TestTask(Datas.TASK_13, false) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_20 : TestTask(Datas.TASK_20, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_21 : TestTask(Datas.TASK_21, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_22 : TestTask(Datas.TASK_22, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_23 : TestTask(Datas.TASK_23, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_30 : TestTask(Datas.TASK_30, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_31 : TestTask(Datas.TASK_31, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_32 : TestTask(Datas.TASK_32, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_33 : TestTask(Datas.TASK_33, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_40 : TestTask(Datas.TASK_40, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_41 : TestTask(Datas.TASK_41, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_42 : TestTask(Datas.TASK_42, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_43 : TestTask(Datas.TASK_43, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_50 : TestTask(Datas.TASK_50, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_51 : TestTask(Datas.TASK_51, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_52 : TestTask(Datas.TASK_52, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_53 : TestTask(Datas.TASK_53, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_60 : TestTask(Datas.TASK_60, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_61 : TestTask(Datas.TASK_61, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_62 : TestTask(Datas.TASK_62, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_63 : TestTask(Datas.TASK_63, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_70 : TestTask(Datas.TASK_70, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_71 : TestTask(Datas.TASK_71, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_72 : TestTask(Datas.TASK_72, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_73 : TestTask(Datas.TASK_73, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_80 : TestTask(Datas.TASK_80, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_81 : TestTask(Datas.TASK_81, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_82 : TestTask(Datas.TASK_82, true) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_83 : TestTask(Datas.TASK_83, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_90 : TestTask(Datas.TASK_90, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_91 : TestTask(Datas.TASK_91, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_92 : TestTask(Datas.TASK_92) {
    override fun run(name: String) {
        doIo(200)
    }
}

class TASK_93 : TestTask(Datas.TASK_93, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_100 : TestTask(Datas.TASK_100, true) {
    override fun run(name: String) {
        doJob(200)
    }

}

class TASK_101 : TestTask(Datas.TASK_101, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class TASK_102 : TestTask(Datas.TASK_102) {
    override fun run(name: String) {
        doIo(200)
    }

}

class TASK_103 : TestTask(Datas.TASK_103, true) {
    override fun run(name: String) {
        doJob(200)
    }

}

class CUTOUT_TASK_1 : TestTask(Datas.CUTOUT_TASK_1, true) {
    override fun run(name: String) {
        doJob(200)
    }

    override fun modifySons(behindTaskIds: Array<String>): Array<String> {
        if (behindTaskIds.size >= 3) {
            return arrayOf(behindTaskIds[0], behindTaskIds[2])
        }
        return behindTaskIds
    }

}

class UITHREAD_TASK_A : TestTask(Datas.UITHREAD_TASK_A) {
    override fun run(name: String) {
        doJob(200)
    }
}

class UITHREAD_TASK_B : TestTask(Datas.UITHREAD_TASK_B) {
    override fun run(name: String) {
        doJob(200)
    }
}

class UITHREAD_TASK_C : TestTask(Datas.UITHREAD_TASK_C) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_1 : TestTask(Datas.ASYNC_TASK_1, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_2 : TestTask(Datas.ASYNC_TASK_2, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_3 : TestTask(Datas.ASYNC_TASK_3, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_4 : TestTask(Datas.ASYNC_TASK_4, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

class ASYNC_TASK_5 : TestTask(Datas.ASYNC_TASK_5, true) {
    override fun run(name: String) {
        doJob(200)
    }
}

object TestTaskCreator : TaskCreator {
    override fun createTask(taskName: String): Task {
        when (taskName) {
            Datas.TASK_10 -> {
                val task_10 = TASK_10()
                task_10.priority = 10
                return task_10
            }
            Datas.TASK_11 -> {
                val TASK_11 = TASK_11()
                TASK_11.priority = 10
                return TASK_11
            }
            Datas.TASK_12 -> {
                val TASK_12 = TASK_12()
                TASK_12.priority = 10
                return TASK_12
            }
            Datas.TASK_13 -> {
                val TASK_13 = TASK_13()
                TASK_13.priority = 10
                return TASK_13
            }
            Datas.TASK_20 -> {
                return TASK_20()
            }
            Datas.TASK_21 -> {
                return TASK_21()
            }
            Datas.TASK_22 -> {
                return TASK_22()
            }
            Datas.TASK_23 -> {
                return TASK_23()
            }
            Datas.TASK_30 -> {
                return TASK_30()
            }
            Datas.TASK_31 -> {
                return TASK_31()
            }
            Datas.TASK_32 -> {
                return TASK_32()
            }
            Datas.TASK_33 -> {
                return TASK_33()
            }
            Datas.TASK_40 -> {
                return TASK_40()
            }
            Datas.TASK_41 -> {
                return TASK_41()
            }
            Datas.TASK_42 -> {
                return TASK_42()
            }
            Datas.TASK_43 -> {
                return TASK_43()
            }
            Datas.TASK_50 -> {
                return TASK_50()
            }
            Datas.TASK_51 -> {
                return TASK_51()
            }
            Datas.TASK_52 -> {
                return TASK_52()
            }
            Datas.TASK_53 -> {
                return TASK_53()
            }
            Datas.TASK_60 -> {
                return TASK_60()
            }
            Datas.TASK_61 -> {
                return TASK_61()
            }
            Datas.TASK_62 -> {
                return TASK_62()
            }
            Datas.TASK_63 -> {
                return TASK_63()
            }
            Datas.TASK_70 -> {
                return TASK_70()
            }
            Datas.TASK_71 -> {
                return TASK_71()
            }
            Datas.TASK_72 -> {
                return TASK_72()
            }
            Datas.TASK_73 -> {
                return TASK_73()
            }
            Datas.TASK_80 -> {
                return TASK_80()
            }
            Datas.TASK_81 -> {
                return TASK_81()
            }
            Datas.TASK_82 -> {
                return TASK_82()
            }
            Datas.TASK_83 -> {
                return TASK_83()
            }
            Datas.TASK_90 -> {
                return TASK_90()
            }
            Datas.TASK_91 -> {
                return TASK_91()
            }
            Datas.TASK_92 -> {
                return TASK_92()
            }
            Datas.TASK_93 -> {
                return TASK_93()
            }
            Datas.TASK_100 -> {
                return TASK_100()
            }
            Datas.TASK_101 -> {
                return TASK_101()
            }
            Datas.TASK_102 -> {
                return TASK_102()
            }
            Datas.TASK_103 -> {
                return TASK_103()
            }
            Datas.CUTOUT_TASK_1 -> {
                return CUTOUT_TASK_1()
            }
            Datas.UITHREAD_TASK_A -> {
                return UITHREAD_TASK_A()
            }
            Datas.UITHREAD_TASK_B -> {
                return UITHREAD_TASK_B()
            }
            Datas.UITHREAD_TASK_C -> {
                return UITHREAD_TASK_C()
            }
            Datas.ASYNC_TASK_1 -> {
                return ASYNC_TASK_1()
            }
            Datas.ASYNC_TASK_2 -> {
                return ASYNC_TASK_2()
            }
            Datas.ASYNC_TASK_3 -> {
                return ASYNC_TASK_3()
            }
            Datas.ASYNC_TASK_4 -> {
                return ASYNC_TASK_4()
            }
            Datas.ASYNC_TASK_5 -> {
                return ASYNC_TASK_5()
            }
        }
        return ASYNC_TASK_5()
    }
}

class TestTaskFactory : TaskFactory(TestTaskCreator)