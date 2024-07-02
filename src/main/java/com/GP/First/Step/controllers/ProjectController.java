package com.GP.First.Step.controllers;


import com.GP.First.Step.DTO.response.ErrorRes;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/project")
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }


    // Retrieves all projects.
    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        // Calls the getAllProjects method from ProjectService.
        List<Project> projects = projectService.getAllProjects();

        // Returns the list of projects in the response.
        return ResponseEntity.ok(projects);
    }


    // Searches projects by name.
    @GetMapping("/search/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        // Fetches projects by name using the getProjectByName method from ProjectService.
        List<Project> projects = projectService.getProjectByName(name);

        // Returns the list of matching projects in the response.
        return ResponseEntity.ok(projects);
    }

    // Retrieves a project by its ID.
    @GetMapping("/searchID/{id}")
    public ResponseEntity<?> getProjectByProjectID(@PathVariable long id) {
        // Fetches the project by ID using the getProjectById method from ProjectService.
        Optional<Project> project = projectService.getProjectById(id);

        // Returns the project in the response if found.
        return ResponseEntity.ok(project);
    }


    // get user's projects by his ID.
    @GetMapping("/userID/{id}")
    public ResponseEntity<?> getProjectByUserID(@PathVariable long id) {
        // Fetches projects by user ID using the getProjectsByUserId method from ProjectService.
        // Returns the list of projects in the response.
        List<Project> projects = projectService.getProjectsByUserId(id);
        return ResponseEntity.ok(projects);
    }

   /* @PostConstruct
    public void importProjectsFromCSV() {
        projectService.importProjectsFromCSV();
    }*/

    // creates a new project.
    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<SuccessRes> createProject(@RequestBody Project project) {
        // Gets the current authenticated user by his email and returns exception if the user is not found.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Calls the createProject method from ProjectService with the project details and user.
        Project savedProject = projectService.createProject(project, user);

        // Returns a success response with the created project.
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Project created successfully", savedProject));
    }


    // Updates an existing project.
    @ResponseBody
    @PutMapping("/update/{id}")
    // Gets the current authenticated user by his email and returns exception if the user is not found.
    public ResponseEntity<SuccessRes> updateProject(@PathVariable long id, @RequestBody Project updatedProject) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Calls the updateProject method from ProjectService with the project ID, updated project details, and user.
        Project project = projectService.updateProject(user, id, updatedProject);
        // Returns a success response with the updated project.
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your project updated successfully", project));
    }


    // Deletes a project.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        // Gets the current authenticated user by his email and returns exception if the user is not found.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Checks if the user is allowed to delete the project.
        if (projectService.deleteProject(user, id))
            // If not allowed, returns a forbidden response.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorRes(HttpStatus.FORBIDDEN, "You are not allowed to Delete this project"));

        // If allowed, deletes the project and returns a success response.
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Project deleted successfully", null));
    }

    // Adds a comment to a project.
    @ResponseBody
    @PostMapping("/addComment/{projectID}")
    public ResponseEntity<SuccessRes> addComment(@RequestBody Comment comment, @PathVariable long projectID) {
        // Gets the current authenticated user by his email and returns exception if the user is not found.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Calls the addComment method from ProjectService with the comment details, user, and project ID.
        Comment savedComment = projectService.addComment(comment, user, projectID);

        // Returns a success response with the added comment.
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Comment added successfully", savedComment));
    }

    // Retrieves comments for a project.
    @GetMapping("/getComments/{id}")
    public ResponseEntity<?> getProjectComments(@PathVariable long id) {
        // Fetches comments by project ID using the getCommentsByProjectId method from ProjectService.
        List<Comment> comments = projectService.getCommentsByProjectId(id);

        // Returns the list of comments in the response.
        return ResponseEntity.ok(comments);
    }

    // Edits a comment from a project.
    @ResponseBody
    @PutMapping("/editComment/{id}")
    public ResponseEntity<SuccessRes> editComment(@PathVariable long id, @RequestBody Comment updatedComment) {
        // Gets the current authenticated user by his email and returns exception if the user is not found.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Calls the updateComment method from ProjectService with the comment ID, updated comment details, and user.
        Comment comment = projectService.updateComment(user, id, updatedComment);

        // Returns a success response with the updated comment.
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Comment updated successfully", comment));
    }


    // Deletes a comment from a project.
    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {

        // Gets the current authenticated user by his email and returns exception if user email not found.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Checks if the user is not allowed to delete the comment.
        // Calls the deleteComment method from ProjectService with the comment ID and user.
        if (projectService.deleteComment(user, id))

            // If not allowed, returns a forbidden response.
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorRes(HttpStatus.FORBIDDEN, "You are not allowed to Delete this Comment"));

        // If allowed, deletes the comment and returns a success response.
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Comment deleted successfully", null));
    }

    // Likes or unlikes a project.
    @ResponseBody
    @PutMapping("/like/{projectID}")
    public ResponseEntity<SuccessRes> likeProject(@PathVariable long projectID)
    {
        // Gets the current authenticated user by his email and returns exception if user email not found.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Calls the likeProject method from ProjectService with the project ID and user.
        Project project = projectService.likeProject(projectID, user);

        // Returns a success response with the updated like status of the project.
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Like status updated successfully", project));
    }

}
