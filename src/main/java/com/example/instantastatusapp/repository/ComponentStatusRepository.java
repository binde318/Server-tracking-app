package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.Component;
import com.example.instantastatusapp.entity.ComponentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentStatusRepository extends JpaRepository<ComponentStatus,Long> {
    Optional<ComponentStatus> findByName(String status);

}
