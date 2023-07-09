package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.Component;
import com.example.instantastatusapp.entity.ComponentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository extends JpaRepository<Component,Long> {
    Component findByName(String s);

    Component findComponentByIpAddress(String ip);
    Component findComponentByPortNumber(Integer port);

    Component findComponentByUrl(String url);
    Long countComponentByType(String operation);
    Long countAllByTypeEquals(String type);
    Long countComponentByTypeEquals(String type);
    Long countComponentByStatus(String status);
    Long countAllByType(String type);

}
