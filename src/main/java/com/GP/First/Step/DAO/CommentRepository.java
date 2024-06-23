package com.GP.First.Step.DAO;

import com.GP.First.Step.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Optional<Comment>findByCommentID(long commentID);
    List<Comment> findByProjectID(long projectID);
    void delete(Comment comment);
}
