package com.GP.First.Step.DAO;

import com.GP.First.Step.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// The generic parameters specify that this repository manages Project entities and uses Integer as the ID type.
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    // Finds a Project entity by its ID.
    // Returns an Optional<Project> which can contain the found Project or be empty if not found.
    Optional<Project> findById(long projectId);

    //Finds all Project entities associated with a specific user ID.
    //Returns a List<Project> containing all matching projects.
    List<Project> findByUserId(long userId);

    // Finds all Project entities associated with a specific company name.
    // Returns a List<Project> containing all matching projects.
    List<Project> findByCompanyName(String companyName);

    // Finds all Project entities.
    // Returns a List<Project> containing all projects.
    List<Project> findAll();

    // Deletes a given Project
    void delete(Project project);
}
