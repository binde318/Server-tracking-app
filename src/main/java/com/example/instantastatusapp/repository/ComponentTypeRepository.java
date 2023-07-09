package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentTypeRepository extends JpaRepository<ComponentType,Long> {
    Optional<ComponentType> findByName(String type);

}
