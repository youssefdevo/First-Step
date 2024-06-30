package com.GP.First.Step.services;

import com.GP.First.Step.entities.Project;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class OpenCsvService implements CsvService {
    @Override
    public List<Project> readProjectsFromCSV(String filePath) {
        try {
            return new CsvToBeanBuilder<Project>(Files.newBufferedReader(Paths.get(filePath)))
                    .withType(Project.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

    public static void writeProjectsToCSV(String filePath, List<Project> projects) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            StatefulBeanToCsv<Project> beanToCsv = new StatefulBeanToCsvBuilder<Project>(writer).build();
            beanToCsv.write(projects);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write CSV file", e);
        }
    }
    @Override
    public void appendProjectToCSV(Project project, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            writer.writeNext(convertProjectToStringArray(project));
        } catch (IOException e) {
            throw new RuntimeException("Error appending project to CSV file", e);
        }
    }
    @Override
    public void updateProjectToCSV(Project updatedProject, String filePath) {
        List<Project> projects = readProjectsFromCSV(filePath);
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getProjectID() == updatedProject.getProjectID()) {
                projects.set(i, updatedProject);
                break;
            }
        }
        writeProjectsToCSV(filePath, projects);
    }
    @Override
    public void deleteProjectFromCSV(Project project, String filePath) {
        List<Project> projects = readProjectsFromCSV(filePath);
        List<Project> updatedProjects = projects.stream()
                .filter(p -> p.getProjectID() != project.getProjectID())
                .collect(Collectors.toList());
        writeProjectsToCSV(filePath, updatedProjects);
    }

    private String[] convertProjectToStringArray(Project project) {
        return new String[] {
                project.getCompanyName(),
                project.getSlogan(),
                project.getAmountRaised().toString(),
                String.valueOf(project.getYear()),
                project.getStage(),
                project.getBusinessModel(),
                project.getImageURL(),
                project.getFullDescription(),
                project.getPdf_URL(),
                project.getInvestors(),
                project.getAbout(),
                project.getIndustry(),
                project.getTags(),
                project.getCustomerModel(),
                project.getWebsite(),
                project.getLegalName(),
                project.getType(),
                String.valueOf(project.getUserId()),
                String.valueOf(project.getProjectID())
        };
    }
}

