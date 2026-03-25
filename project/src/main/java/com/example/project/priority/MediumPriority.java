package com.example.project.priority;

import com.example.project.entity.TaskType;

public class MediumPriority implements PriorityLevel {
    @Override
    public TaskType getPriority() {
        return TaskType.MEDIUM_PRIORITY;
    }
}