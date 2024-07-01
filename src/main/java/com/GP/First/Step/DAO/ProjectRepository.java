package com.GP.First.Step.DAO;

import com.GP.First.Step.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Optional<Project> findById(long projectId);
    List<Project> findByUserId(long userId);
    List<Project> findByCompanyName(String companyName);
    List<Project> findAll();
    void delete(Project project);
}
