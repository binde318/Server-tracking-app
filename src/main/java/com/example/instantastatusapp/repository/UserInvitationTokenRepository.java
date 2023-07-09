package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.UserInvitationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInvitationTokenRepository extends JpaRepository<UserInvitationToken, Long> {
    UserInvitationToken findByToken(String token);
    UserInvitationToken findUserInvitationTokenByEmail(String email);

}
