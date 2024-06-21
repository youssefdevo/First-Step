package com.GP.First.Step.controllers;

import com.GP.First.Step.DTO.response.SuccessRes;
import com.GP.First.Step.entities.Project;
import com.GP.First.Step.services.CSVUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rest/project")
public class ProjectController {

    private final String csvFilePath = "C:\\FCAI\\Graduation Project/pitch_decks_dataset.csv"; // Set the path to your CSV file

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = CSVUtil.readProjectsFromCSV(csvFilePath);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        List<Project> projects = CSVUtil.readProjectsFromCSV(csvFilePath);
        List<Project> matchedProjects = projects.stream()
                .filter(project -> project.getCompanyName().equalsIgnoreCase(name))
                .toList();
        return ResponseEntity.ok(matchedProjects);
    }

    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<SuccessRes> createProject(@RequestBody Project project) {
        List<Project> projects = CSVUtil.readProjectsFromCSV(csvFilePath);
        project.setProjectID(projects.size() + 1); // Set ID for the new project
        projects.add(project);
        CSVUtil.writeProjectsToCSV(csvFilePath, projects);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Project created successfully", project));
    }

    @ResponseBody
    @PutMapping("/update/{id}")
    public ResponseEntity<SuccessRes> updateProject(@PathVariable long id, @RequestBody Project updatedProject) {
        List<Project> projects = CSVUtil.readProjectsFromCSV(csvFilePath);
        Project project = projects.stream()
                .filter(p -> p.getProjectID() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (updatedProject.getCompanyName() != null)
            project.setCompanyName(updatedProject.getCompanyName());
        if (updatedProject.getSlogan() != null)
            project.setSlogan(updatedProject.getSlogan());
        if (updatedProject.getAmountRaised() != null)
            project.setAmountRaised(updatedProject.getAmountRaised());
        if (updatedProject.getYear() != null)
            project.setYear(updatedProject.getYear());
        if (updatedProject.getBusinessModel() != null)
            project.setBusinessModel(updatedProject.getBusinessModel());
        if (updatedProject.getFullDescription() != null)
            project.setFullDescription(updatedProject.getFullDescription());
        if (updatedProject.getImageURL() != null)
            project.setImageURL(updatedProject.getImageURL());
        if (updatedProject.getPDF_URL() != null)
            project.setPDF_URL(updatedProject.getPDF_URL());
        if (updatedProject.getInvestors() != null)
            project.setInvestors(updatedProject.getInvestors());
        if (updatedProject.getAbout() != null)
            project.setAbout(updatedProject.getAbout());
        if (updatedProject.getIndustry() != null)
            project.setIndustry(updatedProject.getIndustry());
        if (updatedProject.getTags() != null)
            project.setTags(updatedProject.getTags());
        if (updatedProject.getCustomerModel() != null)
            project.setCustomerModel(updatedProject.getCustomerModel());
        if (updatedProject.getWebsite() != null)
            project.setWebsite(updatedProject.getWebsite());
        if (updatedProject.getLegalName() != null)
            project.setLegalName(updatedProject.getLegalName());
        if (updatedProject.getType() != null)
            project.setType(updatedProject.getType());

        CSVUtil.writeProjectsToCSV(csvFilePath, projects);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your project updated successfully", project));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessRes> deleteProject(@PathVariable Long id) {
        List<Project> projects = CSVUtil.readProjectsFromCSV(csvFilePath);
        Project project = projects.stream()
                .filter(p -> p.getProjectID() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"));

        projects.remove(project);
        CSVUtil.writeProjectsToCSV(csvFilePath, projects);
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Project deleted successfully", null));
    }
}
