package com.effective.android.sample.view

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.effective.android.sample.R

class CusDialog private constructor(context: Context) : Dialog(context) {
    class Builder(var context: Context) {
        var title: CharSequence? = null
        var left: CharSequence? = null
        var leftAction: View.OnClickListener? = null
        var right: CharSequence? = null
        var rightAction: View.OnClickListener? = null
        var action = false
        fun title(charSequence: CharSequence): Builder {
            if (charSequence.isNotEmpty()) {
                title = charSequence
            }
            return this
        }

        @JvmOverloads
        fun left(charSequence: CharSequence, listener: View.OnClickListener? = null): Builder {
            if (charSequence.isNotEmpty()) {
                left = charSequence
            }
            leftAction = listener
            return this
        }

        @JvmOverloads
        fun right(charSequence: CharSequence, listener: View.OnClickListener? = null): Builder {
            if (charSequence.isNotEmpty()) {
                right = charSequence
            }
            rightAction = listener
            return this
        }

        fun build(): Dialog {
            val cusDialog = CusDialog(context)
            val view = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_layout, null)
            val title = view.findViewById<TextView>(R.id.title)
            title.text = this.title
            val left = view.findViewById<TextView>(R.id.left)
            left.text = this.left
            left.setOnClickListener { v ->
                if (leftAction != null) {
                    action = true
                    leftAction!!.onClick(v)
                }
                cusDialog.dismiss()
            }
            val right = view.findViewById<TextView>(R.id.right)
            right.text = this.right
            right.setOnClickListener { v ->
                if (rightAction != null) {
                    action = true
                    rightAction!!.onClick(v)
                }
                cusDialog.dismiss()
            }
            cusDialog.setContentView(view)
            val window = cusDialog.window
            if (window != null) {
                window.setGravity(Gravity.CENTER)
                val lp = window.attributes
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                window.attributes = lp
            }
            cusDialog.setCanceledOnTouchOutside(false)
            cusDialog.setCancelable(false)
            return cusDialog
        }

    }
}