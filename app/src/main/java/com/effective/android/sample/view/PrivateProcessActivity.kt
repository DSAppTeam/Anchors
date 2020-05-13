package com.effective.android.sample.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.effective.android.sample.R
import com.effective.android.sample.util.ProcessUtils

class PrivateProcessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_process)
        Log.d(TAG, "PrivateProcessActivity#onCreate process Id is " + ProcessUtils.processId)
        Log.d(TAG, "PrivateProcessActivity#onCreate process Name is " + ProcessUtils.processName)
    }

    companion object {
        private val TAG: String = "PrivateProcessActivity"
    }
}