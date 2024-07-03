package com.GP.First.Step.DAO;

import java.util.Optional;

import com.GP.First.Step.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

// The generic parameters specify that this repository manages User entities and uses Integer as the ID type.
public interface UserRepository extends JpaRepository<User, Integer> {

    // Finds a User entity by its ID.
    //Returns an Optional<User> which can contain the found User or be empty if not found.
    Optional<User> findById(Long id);

    // Finds a User entity by its username.
    //Returns an Optional<User> which can contain the found User or be empty if not found.

    Optional<User> findByUserName(String userName);

    // Finds a User entity by its email.
    // Returns an Optional<User> which can contain the found User or be empty if not found.
    Optional<User> findByEmail(String email);

}