package com.GP.First.Step.services;

import com.GP.First.Step.entities.Project;

import java.util.List;

// Defines methods for CSV file operations.
public interface CsvService {

    //  Declares methods for reading, appending, updating, and deleting projects in CSV files.
    //  Provides an abstraction for different CSV service implementations.

    List<Project> readProjectsFromCSV(String filePath);
    void appendProjectToCSV(Project project, String filePath);

    void deleteProjectFromCSV(Project project, String filePath);

    void updateProjectToCSV(Project project, String tempFilePath);
}
