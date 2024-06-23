package com.GP.First.Step.services;

import com.GP.First.Step.DAO.CommentRepository;
import com.GP.First.Step.DAO.ProjectRepository;
import com.GP.First.Step.DAO.UserRepository;
import com.GP.First.Step.entities.Comment;
import com.GP.First.Step.entities.Project;
import com.GP.First.Step.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, CommentRepository commentRepository) {
        this.projectRepository = projectRepository;
        this.commentRepository = commentRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectByName(String name) {
        return projectRepository.findByCompanyName(name);
    }

    public Optional<Project> getProjectById(long id) {
        return projectRepository.findByProjectID(id);
    }

    public List<Project> getProjectsByUserId(long userId) {
        return projectRepository.findByUserId(userId);
    }

    public Project createProject(Project project, User user) {
        project.setUserId(user.getId());
        return projectRepository.save(project);
    }

    public Project updateProject(long id, Project updatedProject) {
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

        return projectRepository.save(project);
    }

    public void deleteProject(long id) {
        Project project = projectRepository.findByProjectID(id).orElseThrow(() -> new RuntimeException("Project not found"));
        projectRepository.delete(project);
    }

    public Comment addComment(Comment comment, User user, long projectID) {
        comment.setUserName(user.getUserName());
        comment.setUserID(user.getId());
        comment.setProjectID(projectID);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByProjectId(long id) {
        return commentRepository.findByProjectID(id);
    }

    public Comment updateComment(long id, Comment updatedComment) {
        Comment comment = commentRepository.findByCommentID(id).orElseThrow(() -> new RuntimeException("Comment not found"));

        if (updatedComment.getContent() != null)
            comment.setContent(updatedComment.getContent());

        return commentRepository.save(comment);
    }

    public void deleteComment(long id) {
        Comment comment = commentRepository.findByCommentID(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }

    public Project likeProject(long projectID, User user) {
        Project project = projectRepository.findByProjectID(projectID).orElseThrow(() -> new RuntimeException("Project not found"));

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

    public List<Project> transferProjectsFromCSV(String csvFilePath) {
        return CSVUtil.readProjectsFromCSV(csvFilePath);
    }
}
