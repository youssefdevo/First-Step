package com.GP.First.Step.controllers;

import com.GP.First.Step.DAO.ProjectRepository;
import com.GP.First.Step.DTO.response.ErrorRes;
import com.GP.First.Step.DTO.response.SuccessRes;
import com.GP.First.Step.entities.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rest/auth/project")
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/search/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        List<Project> projects = projectRepository.findByName(name);
        return ResponseEntity.ok(projects);
    }
    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<SuccessRes> createProject(@RequestBody Project project) {
        Project savedProject = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Project created successfully", savedProject));
    }
    @ResponseBody
    @PutMapping("/update/{id}")
    public ResponseEntity<SuccessRes> updateProject(@PathVariable long id,@RequestBody Project updatedProject) {
        Project project=projectRepository.findByProjectID(id).orElseThrow(() -> new RuntimeException("Project not found"));

        if (updatedProject.getName() != null)
            project.setName(updatedProject.getName());
        if (updatedProject.getDescription() != null)
            project.setDescription(updatedProject.getDescription());
        if (updatedProject.getAbout() != null)
            project.setAbout(updatedProject.getAbout());
        if (updatedProject.getSlogan() != null)

            project.setSlogan(updatedProject.getSlogan());
        if (updatedProject.getIndustry()!= null)
            project.setIndustry(updatedProject.getIndustry());
        if (updatedProject.getBusiness_model() != null)
            project.setBusiness_model(updatedProject.getBusiness_model());
        if (updatedProject.getCustomer_model() != null)
            project.setCustomer_model(updatedProject.getCustomer_model());

        if (updatedProject.getType() != null)
            project.setType(updatedProject.getType());
        if (updatedProject.getWebsite() != null)
            project.setWebsite(updatedProject.getWebsite());
        if (updatedProject.getLegal_name() != null)
            project.setLegal_name(updatedProject.getLegal_name());

        if (updatedProject.getRaised_funds() != null)
            project.setRaised_funds(updatedProject.getRaised_funds());
        if (updatedProject.getFounding_year() != null)
            project.setFounding_year(updatedProject.getFounding_year());
        if (updatedProject.getStage() != null)
            project.setStage(updatedProject.getStage());

        projectRepository.save(project);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your project updated successfully", project));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessRes> deleteProject(@PathVariable Long id) {
        Project project=projectRepository.findByProjectID(id).orElseThrow(() -> new RuntimeException("Project not found"));
        projectRepository.delete(project);
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Project deleted successfully", null));
    }


}
