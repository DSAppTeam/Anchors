package com.effective.android.sample.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.effective.android.anchors.LockableAnchor;
import com.effective.android.anchors.Task;
import com.effective.android.anchors.TaskListener;
import com.effective.android.sample.R;
import com.effective.android.sample.data.TaskTest;
import com.effective.android.sample.util.ProcessUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LockableAnchor lockableAnchor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity#onCreate process Id is " + ProcessUtils.getProcessId());
        Log.d(TAG, "MainActivity#onCreate process Name is " + ProcessUtils.getProcessName());

        //SampleApplication#onCreate 已经有常规初始化的demo实例了


        //留意跳转时Log.d 输出的新进程信息，同时application#onCreate会被再次调用
        //一般针对多进程，在常规初始化流程中针对进程对应的特定场景构建特定的初始化链
        //在异步进程被拉起的时候，执行特定进程的初始化链即可
        //具体代码可参考 SampleApplication#initDependenciesCompatMutilProcess() 方法
        testPrivateProcess();
        testPublicProcess();

        //测试用户选择
        testUserChoose();

        //测试重启新链接
        testRestartNewDependenciesLink();
    }

    private void testPrivateProcess() {
        findViewById(R.id.test_private_process).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PrivateProcessActivity.class));
            }
        });
    }


    private void testPublicProcess() {
        findViewById(R.id.test_public_process).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PublicProcessActivity.class));
            }
        });
    }


    private void testUserChoose() {
        findViewById(R.id.test_user_anchor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Demo1 - testUserChoose");
                lockableAnchor = new TaskTest().startForTestLockableAnchor();
                lockableAnchor.setLockListener(new LockableAnchor.LockListener() {
                    @Override
                    public void lockUp() {
                        //做一些自己的业务判断
                        //.....
                        new CusDialog.Builder(MainActivity.this)
                                .title("任务(" + lockableAnchor.getLockId() + ")已进入等待状态，请求响应")
                                .left("终止任务", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        lockableAnchor.smash();
                                    }
                                })
                                .right("继续执行", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        lockableAnchor.unlock();
                                    }
                                }).build().show();
                    }
                });
                Log.d("MainActivity", "Demo1 - testUserChoose");
            }
        });
    }


    private void testRestartNewDependenciesLink() {
        findViewById(R.id.test_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - startLinkOne");
                new TaskTest().startForLinkOne(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - endLinkOne");
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - startLinkTwo");
                        new TaskTest().startForLinkTwo();
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - endLinkTwo");
                    }
                });

            }
        });
    }

}
