package com.effective.android.anchors;

class LockableTask extends Task {

    private LockableAnchor lockableAnchor;

    LockableTask(Task wait,LockableAnchor lockableAnchor) {
        super(wait.getId() + "_waiter",true);
        lockableAnchor.setTargetTaskId(wait.getId());
        this.lockableAnchor = lockableAnchor;
    }

    @Override
    protected void run(String name) {
        lockableAnchor.lock();
    }

    boolean successToUnlock(){
        return lockableAnchor.successToUnlock();
    }
}
