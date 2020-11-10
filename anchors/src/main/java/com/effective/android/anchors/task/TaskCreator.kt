package com.effective.android.anchors.task

interface TaskCreator {
    fun createTask(taskName: String): Task
}

private typealias CreateTask = (taskName: String) -> Task

class TaskCreatorBuilder {

    lateinit var createTask: CreateTask

    fun createTask(createTask: CreateTask) {
        this.createTask = createTask
    }
}