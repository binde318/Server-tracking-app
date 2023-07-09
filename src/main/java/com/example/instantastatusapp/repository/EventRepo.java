package com.example.instantastatusapp.repository;

import com.example.instantastatusapp.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event,Long> {
//    List<Event> findTop10ByOrderByTimeDesc();


    Page<Event> findAllByOrderByCreatedTimeDesc(PageRequest pageable);
//    List<Event> findAllByUrl(String url);
//    List<Event> findAllByTimestampBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Event> findAllByUrlAndCreatedTimeBetween(String url, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Event> findAllByUrlAndPortNumberAndCreatedTimeBetween(String ur,Integer portNumber,LocalDateTime startDateTime,
                                                         LocalDateTime endDateTime);
    Long countEventByTypeEqualsIgnoreCase(String type);
    List<Event> findAllByUrlAndTypeEquals(String ur,String type);
//    List<Event> findAllByUrl(String url);
    Long countAllByType(String type);

}
