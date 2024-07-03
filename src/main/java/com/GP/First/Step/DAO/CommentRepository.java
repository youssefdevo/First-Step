package com.GP.First.Step.DAO;

import com.GP.First.Step.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;  // Spring Data JPA interface providing CRUD operations and more.

import java.util.List;
import java.util.Optional;

// The generic parameters specify that this repository manages Comment entities and uses Integer as the ID type.
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    // Finds a Comment entity by its ID.
    // Returns an Optional<Comment> which can contain the found Comment or be empty if not found.
    Optional<Comment> findById(long id);

    // Finds all Comment entities associated with a specific project ID.
    // Returns a List<Comment> containing all matching comments.
    List<Comment> findByProject_Id(long projectID);

    // Deletes a given Comment.
    void delete(Comment comment);
}
