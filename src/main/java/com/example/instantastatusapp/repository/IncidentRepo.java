package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepo extends JpaRepository<Incident,Long> {
}
