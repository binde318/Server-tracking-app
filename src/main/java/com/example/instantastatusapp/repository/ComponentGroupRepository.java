package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.ComponentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentGroupRepository extends JpaRepository<ComponentGroup,Long>{
    Optional<ComponentGroup> findComponentGroupByName(String group);
    ComponentGroup findByName(String group);
}
