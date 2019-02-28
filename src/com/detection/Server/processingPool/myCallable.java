package com.detection.Server.processingPool;


public class myCallable implements  Runnable{

    private Task _task;//在任务中跑的task

    public myCallable(Task task)
    {
        _task = task;
    }

    @Override
    public void run() {
        //里面跑不同的runable
        _task.run();
    }

    public Task get_task() {
        return _task;
    }

    public void set_task(Task _task) {
        this._task = _task;
    }
}
