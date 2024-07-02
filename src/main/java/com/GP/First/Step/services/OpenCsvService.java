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


    // Constructor with dependency injection for UserRepository.
    @Autowired
    public OpenCsvService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // for upload sample projects.
    @Override
    public List<Project> readProjectsFromCSV(String filePath) {
        try {
            // List to hold the final Project objects.
            List<Project> projects = new ArrayList<>();

            // Opens the CSV file at the specified file path.
            // Using CsvToBeanBuilder to read CSV and map to ProjectT objects.
            List<ProjectT> projectsTemp = new CsvToBeanBuilder<ProjectT>(Files.newBufferedReader(Paths.get(filePath)))
                    .withType(ProjectT.class)  // Set the type of beans to ProjectT.
                    .build()                   // Build the CsvToBean object.
                    .parse();                  // Parse the CSV file into a list of ProjectT objects.

            // Convert each ProjectT object to a Project object and add to the projects list.
            projectsTemp.forEach(project -> {
                Project project1 = convertProjectTempToProject(project);
                projects.add(project1);
            });
            // Returns the list of Project objects.
            return projects;
        } catch (IOException e) {
            // Wrap the IOException in a RuntimeException and throw it.
            throw new RuntimeException("Error reading CSV file", e);
        }
    }

    //  Writes a list of ProjectT objects to a CSV file.
    public static void writeProjectsToCSV(String filePath, List<ProjectT> projects) {

        // Opens or creates the CSV file at the specified file path.
        //  Writes the list of ProjectT objects to the file.
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Create StatefulBeanToCsv instance and write the list of projects to CSV.
            StatefulBeanToCsv<ProjectT> beanToCsv = new StatefulBeanToCsvBuilder<ProjectT>(writer).build();
            beanToCsv.write(projects);
        } catch (Exception e) {
            e.printStackTrace();
            // Wrap any exception in a RuntimeException and throw it.
            throw new RuntimeException("Failed to write CSV file", e);
        }
    }

    @Override
    public void appendProjectToCSV(Project project, String filePath) {
        // Read existing projects from the CSV file.
        List<Project> projects = readProjectsFromCSV(filePath);
        List<ProjectT> tempProjects = new ArrayList<>();
        // Convert existing Project objects to ProjectT and add to tempProjects.
        for (Project value : projects) {
            tempProjects.add(convertProjectToProjectT(value));
        }
        // Convert the new Project object to ProjectT and add to tempProjects.
        tempProjects.add(convertProjectToProjectT(project));

        // Write the updated list of projects back to the CSV file.
        writeProjectsToCSV(filePath, tempProjects);
    }

    @Override
    public void updateProjectToCSV(Project updatedProject, String filePath) {

        // Read existing projects from the CSV file.
        List<Project> projects = readProjectsFromCSV(filePath);
        List<ProjectT> tempProjects = new ArrayList<>();
        // Finds and updates the specific project and convert all Project objects to ProjectT.
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getProjectID() == updatedProject.getProjectID()) {
                projects.set(i, updatedProject);
            }
            tempProjects.add(convertProjectToProjectT(projects.get(i)));
        }

        // Write the updated list of projects back to the CSV file.
        writeProjectsToCSV(filePath, tempProjects);
    }



    @Override
    public void deleteProjectFromCSV(Project project, String filePath) {
        // Read existing projects from the CSV file.
        List<Project> projects = readProjectsFromCSV(filePath);
        List<ProjectT> tempProjects = new ArrayList<>();

        // Remove the specific project and convert remaining Project objects to ProjectT.
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getProjectID() == project.getProjectID()) {
                continue;
            }
            tempProjects.add(convertProjectToProjectT(projects.get(i)));
        }

        // Write the updated list of projects back to the CSV file.
        writeProjectsToCSV(filePath, tempProjects);
    }

    private Project convertProjectTempToProject(ProjectT project) {
        // Creates a new Project object.
        Project project1 = new Project();
        Long userId = project.getUserId();

        // Find the User associated with the userId.
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Set all properties of Project object.
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
        // Creates a new ProjectT object.
        ProjectT project1 = new ProjectT();

        // Set all properties of ProjectT object.
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

