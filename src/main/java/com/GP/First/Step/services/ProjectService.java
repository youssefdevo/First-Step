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

// This service class manages project-related operations such as retrieving, creating, updating, deleting projects, and handling comments and likes on projects.
@Service
public class ProjectService {

    // Handles CRUD operations for Project entities.
    private final ProjectRepository projectRepository;

    // Handles CRUD operations for Comment entities.
    private final CommentRepository commentRepository;

    // Manages file operations on a cloud storage service.
    private final BlobService blobService;

    // Handles CSV file operations.
    private final CsvService csvService;
    private static final String BLOB_NAME = "updated_pitch_decks_dataset.csv";

    @Autowired
    public ProjectService(ProjectRepository projectRepository, CommentRepository commentRepository, BlobService blobService, CsvService csvService) {
        this.projectRepository = projectRepository;
        this.commentRepository = commentRepository;
        this.blobService = blobService;
        this.csvService = csvService;
    }

    // Fetches all projects from the database.
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Retrieves projects by the company name.
    public List<Project> getProjectByName(String name) {
        return projectRepository.findByCompanyName(name);
    }

    // Fetches a project by its ID.
    public Optional<Project> getProjectById(long id) {
        return projectRepository.findById(id);
    }

    // Retrieves projects associated with a specific user ID.
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

    // Creates a new project and updates the CSV file.
    public Project createProject(Project project, User user) {
        // Sets the user for the project.
        project.setUser(user);

        // Saves the project to the database using projectRepository.
        Project savedProject = projectRepository.save(project);

        // Updates the CSV file with the new project.
        updateCSV(savedProject);

        return savedProject;
    }

    // Updates the CSV file with a new project.
    public void updateCSV(Project project) {
        // Downloads the existing CSV file.
        String tempFilePath = "temp_projects.csv";
        blobService.downloadToFile(BLOB_NAME, tempFilePath);

        // Appends the new project to the CSV file.
        csvService.appendProjectToCSV(project, tempFilePath);

        // Uploads the updated CSV file back to the cloud storage.
        blobService.uploadFile(BLOB_NAME, tempFilePath);
        try {
            // Deletes the temporary CSV file.
            Files.deleteIfExists(Paths.get(tempFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Updates an existing project.
    public Project updateProject(User user, long id, Project updatedProject) {

        // Fetches the project by ID and id the ID does not exist it returns exception
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        // Checks if the current user is allowed to update the project.
        if (user.getId() != project.getUser().getId()) {
            throw new RuntimeException("You are not allowed to update this project");
        }

        // Updates the project's data.
        updateProjectData(project, updatedProject);

        // Saves the updated project to the database.
        Project savedProject = projectRepository.save(project);

        // Updates the CSV file with the updated project.
        updatedCVSAfterEdition(savedProject);
        return savedProject;
    }

    // Updates the project's fields with the values from the updated project if they are not null.
    private void updateProjectData(Project project, Project updatedProject) {

        // Checks each field of the updated project and sets the corresponding field of the original project if the updated field is not null.

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

    // Updates the CSV file after editing a project.
    public void updatedCVSAfterEdition(Project project) {
        // Downloads the existing CSV file.
        String tempFilePath = "temp_projects.csv";
        blobService.downloadToFile(BLOB_NAME, tempFilePath);

        // Updates the project in the CSV file.
        csvService.updateProjectToCSV(project, tempFilePath);

        // Uploads the updated CSV file back to the cloud storage.
        blobService.uploadFile(BLOB_NAME, tempFilePath);
        try {
            // Deletes the temporary CSV file.
            Files.deleteIfExists(Paths.get(tempFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean deleteProject(User user, long id) {
        // Fetches the project by ID.
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));

        // Checks if the current user is allowed to delete the project.
        if (user.getId() != project.getUser().getId())
            return false;

        // Deletes the project from the database.
        projectRepository.delete(project);

        // Updates the CSV file to reflect the deletion.
        updateCSVAfterDeletion(project);
        return true;
    }

    private void updateCSVAfterDeletion(Project project) {
        // Downloads the current CSV file from blob storage to a temporary file.
        String tempFilePath = "temp_projects.csv";
        blobService.downloadToFile(BLOB_NAME, tempFilePath);

        // Removes the project entry from the CSV file.
        csvService.deleteProjectFromCSV(project, tempFilePath);

        // Uploads the updated CSV back to blob storage.
        blobService.uploadFile(BLOB_NAME, tempFilePath);
        try {
            Files.deleteIfExists(Paths.get(tempFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Comment addComment(Comment comment, User user, long projectID) {
        // Sets the user details to the comment.
        comment.setUserName(user.getUserName());
        comment.setUser(user);

        // Fetches the project by ID to associate with the comment and returns exception if the project ID is not found.
        Project project = projectRepository.findById(projectID).orElseThrow(() -> new RuntimeException("Project not found"));

        // Sets the project to the comment.
        comment.setProject(project);

        // Saves the comment to the repository.
        return commentRepository.save(comment);
    }

    // Retrieves comments for a specific project by its ID.
    public List<Comment> getCommentsByProjectId(long id) {

        // Fetches and returns a list of comments associated with the given project ID from the repository.
        return commentRepository.findByProject_Id(id);
    }

    // Updates a user's comment.
    public Comment updateComment(User user, long id, Comment updatedComment) {

        // Fetches the comment by its ID and returns exception if the comment ID is not found.
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        // Checks if the current user is not authorized to update the comment.
        if(user.getId() != comment.getUser().getId())
            throw new RuntimeException("You are not allowed to update this Comment");

        //  Updates the comment content if provided.
        if (updatedComment.getContent() != null)
            comment.setContent(updatedComment.getContent());

        // Saves the updated comment to the repository.
        return commentRepository.save(comment);
    }

    public Boolean deleteComment(User user, long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        // Checks if the user is not authorized to delete the comment
        if(user.getId() != comment.getUser().getId())
            return false;

        // deletes it from the repository.
        commentRepository.delete(comment);
        // True if the comment was deleted, false otherwise.
        return true;
    }

    // Toggles a user's like on a project.
    // Adds a like if the user has not liked the project yet, otherwise removes the like.
    public Project likeProject(long projectID, User user) {
        // Get the ID of the project to be liked or unliked and throw exception if the project ID is not found.
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
