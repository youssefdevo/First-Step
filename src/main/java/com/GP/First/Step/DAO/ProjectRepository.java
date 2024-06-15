package com.GP.First.Step.DAO;

import com.GP.First.Step.entities.Project;
import com.GP.First.Step.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByProjectID(long projectID);
    List<Project> findByName(String name);
    List<Project>findAll();
    void delete(Project project);

}
