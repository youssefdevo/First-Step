package com.GP.First.Step.services;

import com.GP.First.Step.DAO.UserRepository;
import com.GP.First.Step.entities.Project;
import com.GP.First.Step.entities.ProjectT;
import com.GP.First.Step.entities.User;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenCsvService implements CsvService {
    private final UserRepository userRepository;

    @Autowired
    public OpenCsvService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // for upload sample projects.
    @Override
    public List<Project> readProjectsFromCSV(String filePath) {
        try {
            List<Project> projects = new ArrayList<>();

            List<ProjectT> projectsTemp = new CsvToBeanBuilder<ProjectT>(Files.newBufferedReader(Paths.get(filePath)))
                    .withType(ProjectT.class)
                    .build()
                    .parse();

            projectsTemp.forEach(project -> {
                Project project1 = convertProjectTempToProject(project);
                projects.add(project1);
            });

            return projects;
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

    public static void writeProjectsToCSV(String filePath, List<ProjectT> projects) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            StatefulBeanToCsv<ProjectT> beanToCsv = new StatefulBeanToCsvBuilder<ProjectT>(writer).build();
            beanToCsv.write(projects);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write CSV file", e);
        }
    }

    @Override
    public void appendProjectToCSV(Project project, String filePath) {
        List<Project> projects = readProjectsFromCSV(filePath);
        List<ProjectT> tempProjects = new ArrayList<>();
        for (Project value : projects) {
            tempProjects.add(convertProjectToProjectT(value));
        }
        tempProjects.add(convertProjectToProjectT(project));

        writeProjectsToCSV(filePath, tempProjects);
    }

    @Override
    public void updateProjectToCSV(Project updatedProject, String filePath) {

        List<Project> projects = readProjectsFromCSV(filePath);
        List<ProjectT> tempProjects = new ArrayList<>();
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getProjectID() == updatedProject.getProjectID()) {
                projects.set(i, updatedProject);
            }
            tempProjects.add(convertProjectToProjectT(projects.get(i)));
        }

        writeProjectsToCSV(filePath, tempProjects);
    }



    @Override
    public void deleteProjectFromCSV(Project project, String filePath) {
        List<Project> projects = readProjectsFromCSV(filePath);
        List<ProjectT> tempProjects = new ArrayList<>();

        // removing the project
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getProjectID() == project.getProjectID()) {
                continue;
            }
            tempProjects.add(convertProjectToProjectT(projects.get(i)));
        }

        writeProjectsToCSV(filePath, tempProjects);
    }

    private Project convertProjectTempToProject(ProjectT project) {
        Project project1 = new Project();
        Long userId = project.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        project1.setProjectID(project.getId());
        project1.setUser(user);
        project1.setCompanyName(project.getCompanyName());
        project1.setSlogan(project.getSlogan());
        project1.setAmountRaised(project.getAmountRaised());
        project1.setYear(project.getYear());
        project1.setStage(project.getStage());
        project1.setBusinessModel(project.getBusinessModel());
        project1.setFullDescription(project.getFullDescription());
        project1.setImageURL(project.getImageURL());
        project1.setPdf_URL(project.getPdf_URL());
        project1.setInvestors(project.getInvestors());
        project1.setAbout(project.getAbout());
        project1.setIndustry(project.getIndustry());
        project1.setTags(project.getTags());
        project1.setCustomerModel(project.getCustomerModel());
        project1.setWebsite(project.getWebsite());
        project1.setLegalName(project.getLegalName());
        project1.setType(project.getType());

        return project1;
    }

    private ProjectT convertProjectToProjectT(Project project) {
        ProjectT project1 = new ProjectT();

        project1.setId(project.getProjectID());
        project1.setUserId(project.getUser().getId());
        project1.setCompanyName(project.getCompanyName());
        project1.setSlogan(project.getSlogan());
        project1.setAmountRaised(project.getAmountRaised());
        project1.setYear(project.getYear());
        project1.setStage(project.getStage());
        project1.setBusinessModel(project.getBusinessModel());
        project1.setFullDescription(project.getFullDescription());
        project1.setImageURL(project.getImageURL());
        project1.setPdf_URL(project.getPdf_URL());
        project1.setInvestors(project.getInvestors());
        project1.setAbout(project.getAbout());
        project1.setIndustry(project.getIndustry());
        project1.setTags(project.getTags());
        project1.setCustomerModel(project.getCustomerModel());
        project1.setWebsite(project.getWebsite());
        project1.setLegalName(project.getLegalName());
        project1.setType(project.getType());

        return project1;
    }
}

