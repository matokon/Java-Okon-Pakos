package com.example.project.priority;

import com.example.project.entity.TaskType;

public class HighPriority implements PriorityLevel {

    @Override
    public TaskType getPriority() {
        return TaskType.HIGH_PRIORITY;
    }
}