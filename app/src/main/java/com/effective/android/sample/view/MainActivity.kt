package com.effective.android.sample.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.effective.android.sample.R
import com.effective.android.sample.data.TaskTest
import com.effective.android.sample.util.ProcessUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity#onCreate process Id is " + ProcessUtils.getProcessId())
        Log.d(TAG, "MainActivity#onCreate process Name is " + ProcessUtils.getProcessName())
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
        findViewById<View>(R.id.test_private_process).setOnClickListener { startActivity(Intent(this@MainActivity, PrivateProcessActivity::class.java)) }
    }

    private fun testPublicProcess() {
        findViewById<View>(R.id.test_public_process).setOnClickListener { startActivity(Intent(this@MainActivity, PublicProcessActivity::class.java)) }
    }

    private fun testUserChoose() {
        findViewById<View>(R.id.test_user_anchor).setOnClickListener {
            Log.d("MainActivity", "Demo1 - testUserChoose")
            TaskTest().startForTestLockableAnchorByDsl {
                val lockableAnchor = it
                CusDialog.Builder(this@MainActivity)
                        .title("任务(" + lockableAnchor.lockId + ")已进入等待状态，请求响应")
                        .left("终止任务") { lockableAnchor.smash() }
                        .right("继续执行") { lockableAnchor.unlock() }.build().show()
            }
            Log.d("MainActivity", "Demo1 - testUserChoose")
        }
    }

    private fun testRestartNewDependenciesLink() {
        findViewById<View>(R.id.test_restart).setOnClickListener {
            Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - startLinkOne")
            TaskTest().startForLinkOneByDsl(
                    Runnable {
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - endLinkOne")
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - startLinkTwo")
                        Handler(Looper.getMainLooper()).post {
                            TaskTest().startForLinkTwoByDsl()
                        }
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - endLinkTwo")
                    })
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}