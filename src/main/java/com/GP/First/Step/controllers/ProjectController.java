package com.GP.First.Step.controllers;

import com.GP.First.Step.DAO.CommentRepository;
import com.GP.First.Step.DAO.ProjectRepository;
import com.GP.First.Step.DAO.UserRepository;
import com.GP.First.Step.DTO.response.ErrorRes;
import com.GP.First.Step.DTO.response.SuccessRes;
import com.GP.First.Step.entities.Comment;
import com.GP.First.Step.entities.Project;
import com.GP.First.Step.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.GP.First.Step.services.CSVUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rest/project")
public class ProjectController {
    private final String csvFilePath = "C:\\FCAI\\Graduation Project/pitch_decks_dataset.csv"; // Set the path to your CSV file
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ProjectController(ProjectRepository projectRepository,UserRepository userRepository,CommentRepository commentRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.commentRepository=commentRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> getProjectByName(@PathVariable String name) {
        List<Project> projects = projectRepository.findByCompanyName(name);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/searchID/{id}")
    public ResponseEntity<?> getProjectByProjectID(@PathVariable long id) {
        Optional<Project> projects = projectRepository.findByProjectID(id);
        return ResponseEntity.ok(projects);
    }

    // get user projects by his ID.
    @GetMapping("/userID/{id}")
    public ResponseEntity<?> getProjectByUserID(@PathVariable long id) {
        List<Project> projects = projectRepository.findByUserId(id);
        return ResponseEntity.ok(projects);
    }

    @ResponseBody
    @PostMapping("/upload")
    public ResponseEntity<SuccessRes> createProject(@RequestBody Project project) {

        // get current user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        // save user id in his project.
        project.setUserId(user.getId());
        project.setNumberOfLikes(0);

        Project savedProject = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Project created successfully", savedProject));
    }

    @ResponseBody
    @PutMapping("/update/{id}")
    public ResponseEntity<SuccessRes> updateProject(@PathVariable long id, @RequestBody Project updatedProject) {
        Project project = projectRepository.findByProjectID(id).orElseThrow(() -> new RuntimeException("Project not found"));

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
        if (updatedProject.getPdf_URL() != null)
            project.setPdf_URL(updatedProject.getPdf_URL());
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

        projectRepository.save(project);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your project updated successfully", project));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<SuccessRes> deleteProject(@PathVariable Long id) {
        Project project = projectRepository.findByProjectID(id).orElseThrow(() -> new RuntimeException("Project not found"));
        projectRepository.delete(project);
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Project deleted successfully", null));
    }

    @ResponseBody
    @PostMapping("/addComment/{projectID}")
    public ResponseEntity<SuccessRes> addComment(@RequestBody Comment comment,@PathVariable long projectID) {
        // get current user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        //save comment details
        comment.setUserName(user.getUserName());
        comment.setUserID(user.getId());
        comment.setProjectID(projectID);

        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Comment added successfully", comment));
    }

    @GetMapping("/getComments/{id}")
    public ResponseEntity<?> getProjectComments(@PathVariable long id) {
        List<Comment> comments = commentRepository.findByProjectID(id);
        return ResponseEntity.ok(comments);
    }
    @ResponseBody
    @PutMapping("/editComment/{id}")
    public ResponseEntity<SuccessRes> editComment(@PathVariable long id, @RequestBody Comment updatedComment) {
        Comment comment =commentRepository.findByCommentID(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        if (updatedComment.getContent() != null)
            comment.setContent(updatedComment.getContent());

        commentRepository.save(comment);
        return ResponseEntity.ok().body(new SuccessRes(HttpStatus.OK, "Your comment updated successfully", comment));
    }

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<SuccessRes> deleteComment(@PathVariable Long id) {
        Comment comment = commentRepository.findByCommentID(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
        return ResponseEntity.ok(new SuccessRes(HttpStatus.OK, "Comment deleted successfully", null));
    }

    @ResponseBody
    @PostMapping("/like/{projectID}")
    public ResponseEntity<SuccessRes> like(@PathVariable long projectID) {
        // get current user
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Project project=projectRepository.findByProjectID(projectID).orElseThrow(() -> new RuntimeException("Project not found"));

        //<< check if already user liked this project >>

        if(project.getLikes()==null) {
            project.setLikes(new ArrayList<>());
        }
        //remove like
        if(project.getLikes().contains(user.getUserName())) {
            project.removeLike(user.getUserName());
            project.setNumberOfLikes(project.getNumberOfLikes()-1);
            projectRepository.save(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Like removed successfully", project));

        }
        //add like
        else {
            project.addLike(user.getUserName());
            project.setNumberOfLikes(project.getNumberOfLikes()+1);
            projectRepository.save(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessRes(HttpStatus.CREATED, "Like added successfully", project));
        }
    }


    //to transform data from exel to DB
    @GetMapping("/transfer")
    public ResponseEntity<?> transfer() {
        File file = new File(csvFilePath);
        if (!file.exists() || !file.canRead()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorRes(HttpStatus.FORBIDDEN, "File not found or not readable"));
        }

        List<Project> projects = CSVUtil.readProjectsFromCSV(csvFilePath);
        projectRepository.saveAll(projects);
        return ResponseEntity.ok(projects);
    }
}
