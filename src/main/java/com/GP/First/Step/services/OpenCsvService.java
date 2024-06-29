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
            writer.writeNext(convertProjectToStringArray(project));
        } catch (IOException e) {
            throw new RuntimeException("Error appending project to CSV file", e);
        }
    }
    @Override
    public void updateProjectToCSV(Project updatedproject, String filePath) {
        List<Project>projects = readProjectsFromCSV(filePath);
        for(Project project:projects) {
            if(project.getProjectID()==updatedproject.getProjectID()) {
                try (CSVWriter writer = new CSVWriter(new FileWriter(filePath, true))) {
                    writer.writeNext(convertProjectToStringArray(updatedproject));
                } catch (IOException e) {
                    throw new RuntimeException("Error appending project to CSV file", e);
                }

            }
        }
    }
    @Override
    public void deleteProjectFromCSV(long projectId, String filePath) {
        List<Project> projects = readProjectsFromCSV(filePath);
        for(Project project:projects){
            if(project.getProjectID()!=projectId)
                appendProjectToCSV(project,filePath);
        }
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

