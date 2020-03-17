package com.effective.android.anchors;

import android.os.Handler;
import android.util.Log;

public class LockableAnchor {

    private LockListener lockListener;
    private String targetId;
    private Object mLockObject = new Object();
    private Handler handler;
    private boolean unlock = false;

    LockableAnchor(Handler handler){
        this.handler = handler;
    }

    /**
     * 外部监听上锁时机，在监听到上锁之后处理业务逻辑，并完成解锁或者破坏锁行为。
     * @param lockListener
     */
    public void setLockListener(LockListener lockListener){
        this.lockListener = lockListener;
    }

    public String getLockId(){
        return targetId;
    }

    void setTargetTaskId(String id){
        this.targetId = id;
    }

    boolean successToUnlock(){
        return unlock;
    }

    /**
     * 解锁，任务链后续任务继续执行
     */
    public void unlock(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Logger.d(Constants.LOCK_TAG,Thread.currentThread().getName() + "- unlock( " + targetId + " )");
                Logger.d(Constants.LOCK_TAG,"Continue the task chain...");
                unlockInner();
            }
        });
    }

    synchronized void unlockInner(){
        synchronized (mLockObject) {
            unlock = true;
            mLockObject.notify();
        }
    }

    synchronized void smashInner(){
        synchronized (mLockObject) {
            unlock = false;
            mLockObject.notify();
        }
    }

    /**
     * 破坏锁，任务链后续任务无法继续执行
     */
    public void smash(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Logger.d(Constants.LOCK_TAG,Thread.currentThread().getName() + "- smash( " + targetId + " )");
                Logger.d(Constants.LOCK_TAG,"Terminate task chain !");
                smashInner();
            }
        });
    }

    /**
     * 上锁
     */
    void lock(){
        Logger.d(Constants.LOCK_TAG,Thread.currentThread().getName() + "- lock( " + targetId + " )");
        try {
            synchronized (mLockObject) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(lockListener != null){
                            lockListener.lockUp();
                        }
                    }
                });
                mLockObject.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface LockListener{
        void lockUp();
    }
}
