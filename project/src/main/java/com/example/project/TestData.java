package com.example.project;

import com.example.project.priority.HighPriority;
import com.example.project.priority.LowPriority;
import com.example.project.priority.MediumPriority;
import com.example.project.priority.PriorityLevel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestData implements CommandLineRunner {

    @Override
    public void run(String... args) {
        PriorityLevel low = new LowPriority();
        PriorityLevel medium = new MediumPriority();
        PriorityLevel high = new HighPriority();

        System.out.println("Low: " + low.getPriority());
        System.out.println("Medium: " + medium.getPriority());
        System.out.println("High: " + high.getPriority());
    }
}