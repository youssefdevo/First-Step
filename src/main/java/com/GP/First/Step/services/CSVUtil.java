package com.GP.First.Step.services;

import com.GP.First.Step.DAO.ProjectRepository;
import com.GP.First.Step.entities.Project;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CSVUtil {
    private static final AtomicLong idCounter = new AtomicLong();

    // Read projects from CSV
    public static List<Project> readProjectsFromCSV(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            CsvToBean<Project> csvToBean = new CsvToBeanBuilder<Project>(reader)
                    .withType(Project.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<Project> projects = csvToBean.parse();

            // Set IDs programmatically if they are not already set
            projects.forEach(project -> {
                if (project.getProjectID() == 0) {
                    project.setProjectID(idCounter.incrementAndGet());
                    writeProjectsToCSV(filePath,projects);
                }
            });

            return projects;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    // Write projects to CSV
    public static void writeProjectsToCSV(String filePath, List<Project> projects) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            StatefulBeanToCsv<Project> beanToCsv = new StatefulBeanToCsvBuilder<Project>(writer).build();
            beanToCsv.write(projects);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write CSV file", e);
        }
    }
}
