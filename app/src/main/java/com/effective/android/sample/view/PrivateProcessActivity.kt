package com.effective.android.sample.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.effective.android.sample.R;
import com.effective.android.sample.util.ProcessUtils;

public class PrivateProcessActivity extends AppCompatActivity {

    private static final String TAG = "PrivateProcessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_process);

        Log.d(TAG, "PrivateProcessActivity#onCreate process Id is " + ProcessUtils.getProcessId());
        Log.d(TAG, "PrivateProcessActivity#onCreate process Name is " + ProcessUtils.getProcessName());
    }
}
