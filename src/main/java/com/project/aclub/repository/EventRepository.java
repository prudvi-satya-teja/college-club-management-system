package com.project.aclub.repository;

import com.project.aclub.entity.Club;
import com.project.aclub.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByClub(Club club, Pageable pageable);
}
