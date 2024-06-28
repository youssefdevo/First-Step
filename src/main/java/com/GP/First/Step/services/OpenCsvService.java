package com.GP.First.Step.services;

import com.GP.First.Step.entities.Project;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;

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


    @Override
    public void appendProjectToCSV(Project project, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
            String[] projectData = {
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
            writer.writeNext(projectData);
        } catch (IOException e) {
            throw new RuntimeException("Error appending project to CSV file", e);
        }
    }
    @Override
    public void deleteProjectFromCSV(long projectId, String filePath) {
        List<Project> projects = readProjectsFromCSV(filePath);
        List<Project> updatedProjects = projects.stream()
                .filter(project -> project.getProjectID() != projectId)
                .collect(Collectors.toList());
        writeProjectsToCSV(updatedProjects, filePath);
    }

    private void writeProjectsToCSV(List<Project> projects, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (Project project : projects) {
//                appendProjectToCSV(project,filePath);
                String[] projectData = {
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
                writer.writeNext(projectData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing projects to CSV file", e);
        }
    }

}

