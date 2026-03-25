package com.example.project.priority;

import com.example.project.entity.TaskType;

public class LowPriority implements PriorityLevel {
    @Override
    public TaskType getPriority() {
        return TaskType.LOW_PRIORITY;
    }
}