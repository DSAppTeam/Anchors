package com.effective.android.sample.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.effective.android.sample.R;
import com.effective.android.sample.util.ProcessUtils;

public class PublicProcessActivity  extends AppCompatActivity {

    private static final String TAG = "PublicProcessActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_process);

        Log.d(TAG, "PublicProcessActivity#onCreate process Id is " + ProcessUtils.getProcessId());
        Log.d(TAG, "PublicProcessActivity#onCreate process Name is " + ProcessUtils.getProcessName());
    }
}