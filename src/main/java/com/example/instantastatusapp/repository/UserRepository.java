package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByProfileName(String profileName);
//    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
