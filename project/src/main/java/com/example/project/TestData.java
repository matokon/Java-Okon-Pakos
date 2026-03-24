package com.example.project;

import com.example.project.entity.Project;
import com.example.project.repository.ProjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestData {

    @Bean
    CommandLineRunner run(ProjectRepository repository) {
        return args -> {
            Project project = new Project("Moj projekt", "Opis projektu");
            Project saved = repository.save(project);

            System.out.println("Zapisano projekt");
            System.out.println("ID: " + saved.getId());
            System.out.println("Liczba rekordow: " + repository.count());
        };
    }
}