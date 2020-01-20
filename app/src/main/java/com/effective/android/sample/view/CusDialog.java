package com.effective.android.sample.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.effective.android.sample.R;

public class CusDialog extends Dialog{

    private CusDialog(@NonNull Context context) {
        super(context);
    }

    public static class Builder{

        Context context;
        CharSequence title;
        CharSequence left;
        View.OnClickListener leftAction;
        CharSequence right;
        View.OnClickListener rightAction;
        boolean action = false;

        public Builder(Context context){
            this.context = context;
        }

        public CusDialog.Builder title(@NonNull CharSequence charSequence){
            if(!TextUtils.isEmpty(charSequence)){
                this.title = charSequence;
            }
            return this;
        }

        public CusDialog.Builder left(@NonNull CharSequence charSequence){
            return left(charSequence,null);
        }

        public CusDialog.Builder left(@NonNull CharSequence charSequence, View.OnClickListener listener){
            if(!TextUtils.isEmpty(charSequence)){
                this.left = charSequence;
            }
            leftAction = listener;
            return this;
        }

        public CusDialog.Builder right(@NonNull CharSequence charSequence){
            return right(charSequence,null);
        }

        public CusDialog.Builder right(@NonNull CharSequence charSequence, View.OnClickListener listener){
            if(!TextUtils.isEmpty(charSequence)){
                this.right = charSequence;
            }
            rightAction = listener;
            return this;
        }

        public Dialog build(){
            final CusDialog cusDialog = new CusDialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);
            TextView title = view.findViewById(R.id.title);
            title.setText(this.title);
            TextView left = view.findViewById(R.id.left);
            left.setText(this.left);
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(leftAction != null){
                        action = true;
                        leftAction.onClick(v);
                    }
                    cusDialog.dismiss();
                }
            });
            TextView right = view.findViewById(R.id.right);
            right.setText(this.right);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rightAction != null){
                        action = true;
                        rightAction.onClick(v);
                    }
                    cusDialog.dismiss();
                }
            });
            cusDialog.setContentView(view);
            Window window = cusDialog.getWindow();
            if (window != null) {
                window.setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
            }
            cusDialog.setCanceledOnTouchOutside(false);
            cusDialog.setCancelable(false);
            return cusDialog;
        }
    }
}