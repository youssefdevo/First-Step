package com.GP.First.Step.controllers;


import com.GP.First.Step.DTO.response.SuccessRes;
import com.GP.First.Step.entities.Comment;
import com.GP.First.Step.entities.Project;
import com.GP.First.Step.entities.User;
import com.GP.First.Step.services.ProjectService;
import com.GP.First.Step.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rest/project")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }


    @GetMapping("/search/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        List<Project> projects = projectService.getProjectByName(name);
        return ResponseEntity.ok(projects);
    }


    @GetMapping("/searchID/{id}")
    public ResponseEntity<?> getProjectByProjectID(@PathVariable long id) {
        Optional<Project> project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }


    // get user's projects by his ID.
    @GetMapping("/userID/{id}")
    public ResponseEntity<?> getProjectByUserID(@PathVariable long id) {
        List<Project> projects = projectService.getProjectsByUserId(id);
        return ResponseEntity.ok(projects);
    }

   /* @PostConstruct
    public void importProjectsFromCSV() {
        projectService.importProjectsFromCSV();
    }*/

    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<SuccessRes> createProject(@RequestBody Project project) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Project savedProject = projectService.createProject(project, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Project created successfully", savedProject));
    }

    @ResponseBody
    @PutMapping("/update/{id}")
    public ResponseEntity<SuccessRes> updateProject(@PathVariable long id, @RequestBody Project updatedProject) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectService.updateProject(user, id, updatedProject);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your project updated successfully", project));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessRes> deleteProject(@PathVariable Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        projectService.deleteProject(user, id);
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Project deleted successfully", null));
    }

    @ResponseBody
    @PostMapping("/addComment/{projectID}")
    public ResponseEntity<SuccessRes> addComment(@RequestBody Comment comment, @PathVariable long projectID) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Comment savedComment = projectService.addComment(comment, user, projectID);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Comment added successfully", savedComment));
    }

    @GetMapping("/getComments/{id}")
    public ResponseEntity<?> getProjectComments(@PathVariable long id) {
        List<Comment> comments = projectService.getCommentsByProjectId(id);
        return ResponseEntity.ok(comments);
    }

    @ResponseBody
    @PutMapping("/editComment/{id}")
    public ResponseEntity<SuccessRes> editComment(@PathVariable long id, @RequestBody Comment updatedComment) {
        Comment comment = projectService.updateComment(id, updatedComment);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Comment updated successfully", comment));
    }


    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<SuccessRes> deleteComment(@PathVariable Long id) {
        projectService.deleteComment(id);
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Comment deleted successfully", null));
    }

    @ResponseBody
    @PutMapping("/like/{projectID}")
    public ResponseEntity<SuccessRes> likeProject(@PathVariable long projectID) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Project project = projectService.likeProject(projectID, user);
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Like status updated successfully", project));
    }

}
