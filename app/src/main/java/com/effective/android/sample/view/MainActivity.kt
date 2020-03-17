package com.effective.android.sample.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.effective.android.anchors.LockableAnchor
import com.effective.android.anchors.LockableAnchor.LockListener
import com.effective.android.sample.R
import com.effective.android.sample.data.TaskTest
import com.effective.android.sample.util.ProcessUtils

class MainActivity() : AppCompatActivity() {
    private var lockableAnchor: LockableAnchor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity#onCreate process Id is " + ProcessUtils.processId)
        Log.d(TAG, "MainActivity#onCreate process Name is " + ProcessUtils.processName)

        //SampleApplication#onCreate 已经有常规初始化的demo实例了


        //留意跳转时Log.d 输出的新进程信息，同时application#onCreate会被再次调用
        //一般针对多进程，在常规初始化流程中针对进程对应的特定场景构建特定的初始化链
        //在异步进程被拉起的时候，执行特定进程的初始化链即可
        //具体代码可参考 SampleApplication#initDependenciesCompatMutilProcess() 方法
        testPrivateProcess()
        testPublicProcess()

        //测试用户选择
        testUserChoose()

        //测试重启新链接
        testRestartNewDependenciesLink()
    }

    private fun testPrivateProcess() {
        findViewById<View>(R.id.test_private_process).setOnClickListener {
            startActivity(Intent(this@MainActivity, PrivateProcessActivity::class.java))
        }
    }

    private fun testPublicProcess() {
        findViewById<View>(R.id.test_public_process).setOnClickListener {
            startActivity(Intent(this@MainActivity, PublicProcessActivity::class.java))
        }
    }

    private fun testUserChoose() {
        findViewById<View>(R.id.test_user_anchor).setOnClickListener {
            Log.d("MainActivity", "Demo1 - testUserChoose")
            val anchor = TaskTest().startForTestLockableAnchor() ?: return@setOnClickListener
            lockableAnchor = anchor
            anchor.setLockListener(object : LockListener {
                override fun lockUp() {
                    //做一些自己的业务判断
                    //.....
                    CusDialog.Builder(this@MainActivity)
                            .title("任务(" + anchor.lockId + ")已进入等待状态，请求响应")
                            .left("终止任务", View.OnClickListener { anchor.smash() })
                            .right("继续执行", View.OnClickListener { anchor.unlock() })
                            .build()
                            .show()
                }
            })
            Log.d("MainActivity", "Demo1 - testUserChoose")
        }
    }

    private fun testRestartNewDependenciesLink() {
        findViewById<View>(R.id.test_restart).setOnClickListener {
            Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - startLinkOne")
            TaskTest().startForLinkOne(Runnable {
                Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - endLinkOne")
                Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - startLinkTwo")
                TaskTest().startForLinkTwo()
                Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - endLinkTwo")
            })
        }
    }

    companion object {
        private val TAG = "MainActivity"
    }
}