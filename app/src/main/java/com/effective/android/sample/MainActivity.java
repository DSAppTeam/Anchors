package com.effective.android.sample;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.effective.android.anchors.LockableAnchor;
public class MainActivity extends AppCompatActivity {

    private LockableAnchor lockableAnchor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test_user_anchor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity","Demo2 - start");
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
                Log.d("MainActivity","Demo2 - end");
            }
        });
    }


    static class CusDialog extends Dialog{

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

            public Builder title(@NonNull CharSequence charSequence){
                if(!TextUtils.isEmpty(charSequence)){
                    this.title = charSequence;
                }
                return this;
            }

            public Builder left(@NonNull CharSequence charSequence){
                return left(charSequence,null);
            }

            public Builder left(@NonNull CharSequence charSequence, View.OnClickListener listener){
                if(!TextUtils.isEmpty(charSequence)){
                    this.left = charSequence;
                }
                leftAction = listener;
                return this;
            }

            public Builder right(@NonNull CharSequence charSequence){
                return right(charSequence,null);
            }

            public Builder right(@NonNull CharSequence charSequence, View.OnClickListener listener){
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


}
