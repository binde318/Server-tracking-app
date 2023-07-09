package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepo extends JpaRepository<ForgotPasswordToken,Long> {
    ForgotPasswordToken findForgotPasswordTokenByToken(String token);
}
