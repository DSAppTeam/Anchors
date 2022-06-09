package com.effective.android.sample.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.effective.android.anchors.task.lock.LockableAnchor;
import com.effective.android.sample.R;
import com.effective.android.sample.data.JDatas;
import com.effective.android.sample.util.ProcessUtils;

import java.util.List;

public class JMainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        testAsyncTask();
    }

    private void testAsyncTask() {
        this.findViewById(R.id.test_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Demo3 - testAsyncAnchors");
                new JDatas().startAllAsyncTask();
                Log.d("MainActivity", "Demo3 - testAsyncAnchors --end");
            }
        });
    }

    private void testPrivateProcess() {
        this.findViewById(R.id.test_private_process).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JMainActivity.this, PrivateProcessActivity.class));
            }
        });
    }

    private void testPublicProcess() {
        this.findViewById(R.id.test_public_process).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JMainActivity.this, PublicProcessActivity.class));
            }
        });
    }

    private void testUserChoose() {
        this.findViewById(R.id.test_user_anchor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Demo1 - testUserChoose");
                final List<LockableAnchor> lockableAnchors = new JDatas().startForTestLockableAnchor();
                for(final LockableAnchor lockableAnchor : lockableAnchors){
                    lockableAnchor.setLockListener(new LockableAnchor.LockListener() {
                        @Override
                        public void lockUp() {
                            new CusDialog.Builder(JMainActivity.this)
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
                }
            }
        });
    }

    private void testRestartNewDependenciesLink() {
        this.findViewById(R.id.test_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - startLinkOne");
                new JDatas().startForLinkOneByDsl(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - endLinkOne");
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - startLinkTwo");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                new JDatas().startForLinkTwoByDsl();
                            }
                        });
                        Log.d("MainActivity", "Demo2 - testRestartNewDependenciesLink - endLinkTwo");
                    }
                });
            }
        });
    }
}