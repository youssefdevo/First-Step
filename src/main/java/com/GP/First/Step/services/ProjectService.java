package com.GP.First.Step.services;

import com.GP.First.Step.DAO.CommentRepository;
import com.GP.First.Step.DAO.ProjectRepository;
import com.GP.First.Step.entities.Comment;
import com.GP.First.Step.entities.Project;
import com.GP.First.Step.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;
    private final BlobService blobService;
    private final CsvService csvService;
    private static final String BLOB_NAME = "updated_pitch_decks_dataset.csv";

    @Autowired
    public ProjectService(ProjectRepository projectRepository, CommentRepository commentRepository, BlobService blobService, CsvService csvService) {
        this.projectRepository = projectRepository;
        this.commentRepository = commentRepository;
        this.blobService = blobService;
        this.csvService = csvService;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectByName(String name) {
        return projectRepository.findByCompanyName(name);
    }

    public Optional<Project> getProjectById(long id) {
        return projectRepository.findById(id);
    }

    public List<Project> getProjectsByUserId(long userId) {
        return projectRepository.findByUserId(userId);
    }

    public void importProjectsFromCSV() {
        String tempFilePath = "temp_projects.csv";
        blobService.downloadToFile(BLOB_NAME, tempFilePath);
        List<Project> projects = csvService.readProjectsFromCSV(tempFilePath);
        projectRepository.saveAll(projects);
        try {
            Files.deleteIfExists(Paths.get(tempFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Project createProject(Project project, User user) {
        project.setUser(user);
        Project savedProject = projectRepository.save(project);
        updateCSV(savedProject);
        return savedProject;
    }

    public void updateCSV(Project project) {
        String tempFilePath = "temp_projects.csv";
        blobService.downloadToFile(BLOB_NAME, tempFilePath);
        csvService.appendProjectToCSV(project, tempFilePath);
        blobService.uploadFile(BLOB_NAME, tempFilePath);
        try {
            Files.deleteIfExists(Paths.get(tempFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Project updateProject(User user, long id, Project updatedProject) {

        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        if (user.getId() != project.getUser().getId()) {
            throw new RuntimeException("You are not allowed to update this project");
        }

        updateProjectData(project, updatedProject);

        Project savedProject = projectRepository.save(project);

        updatedCVSAfterEdition(savedProject);
        return savedProject;
    }

    private void updateProjectData(Project project, Project updatedProject) {
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
    }

    public void updatedCVSAfterEdition(Project project) {
        String tempFilePath = "temp_projects.csv";
        blobService.downloadToFile(BLOB_NAME, tempFilePath);
        csvService.updateProjectToCSV(project, tempFilePath);
        blobService.uploadFile(BLOB_NAME, tempFilePath);
        try {
            Files.deleteIfExists(Paths.get(tempFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean deleteProject(User user, long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        if (user.getId() != project.getUser().getId())
            return false;


        projectRepository.delete(project);
        updateCSVAfterDeletion(project);
        return true;
    }

    private void updateCSVAfterDeletion(Project project) {
        String tempFilePath = "temp_projects.csv";
        blobService.downloadToFile(BLOB_NAME, tempFilePath);
        csvService.deleteProjectFromCSV(project, tempFilePath);
        blobService.uploadFile(BLOB_NAME, tempFilePath);
        try {
            Files.deleteIfExists(Paths.get(tempFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Comment addComment(Comment comment, User user, long projectID) {
        comment.setUserName(user.getUserName());
        comment.setUser(user);
        Project project = projectRepository.findById(projectID).orElseThrow(() -> new RuntimeException("Project not found"));
        comment.setProject(project);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByProjectId(long id) {
        return commentRepository.findByProject_Id(id);
    }

    public Comment updateComment(User user, long id, Comment updatedComment) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        if(user.getId() != comment.getUser().getId())
            throw new RuntimeException("You are not allowed to update this Comment");

        if (updatedComment.getContent() != null)
            comment.setContent(updatedComment.getContent());

        return commentRepository.save(comment);
    }

    public Boolean deleteComment(User user, long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        if(user.getId() != comment.getUser().getId())
            return false;

        commentRepository.delete(comment);

        return true;
    }

    public Project likeProject(long projectID, User user) {
        Project project = projectRepository.findById(projectID).orElseThrow(() -> new RuntimeException("Project not found"));

        if (project.getLikes() == null) {
            project.setLikes(new ArrayList<>());
        }
        //<< check if already user liked this project >>
        if (project.getLikes().contains(user.getId())) {
            project.removeLike(user.getId());
        }
        // add like otherwise
        else {
            project.addLike(user.getId());
        }
        return projectRepository.save(project);
    }


}
