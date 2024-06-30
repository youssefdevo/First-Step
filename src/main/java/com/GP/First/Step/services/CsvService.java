package com.GP.First.Step.services;

import com.GP.First.Step.entities.Project;

import java.util.List;

public interface CsvService {
    List<Project> readProjectsFromCSV(String filePath);
    void appendProjectToCSV(Project project, String filePath);

    void deleteProjectFromCSV(Project project, String filePath);

    void updateProjectToCSV(Project project, String tempFilePath);
}
