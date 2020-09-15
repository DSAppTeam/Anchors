package com.effective.android.sample.util

import android.os.Process
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

object ProcessUtils {

    @JvmStatic
    val processId: Int
        get() = Process.myPid()

    @JvmStatic
    val processName: String?
        get() = try {
            val file = File("/proc/" + Process.myPid() + "/" + "cmdline")
            val mBufferedReader = BufferedReader(FileReader(file))
            val processName = mBufferedReader.readLine()
                    .trim { it <= ' ' }
            mBufferedReader.close()
            processName
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}