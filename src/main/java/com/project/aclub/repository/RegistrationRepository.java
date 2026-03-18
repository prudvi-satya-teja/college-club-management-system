package com.project.aclub.repository;

import com.project.aclub.entity.Event;
import com.project.aclub.entity.Registration;
import com.project.aclub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByUserAndEvent(User user, Event event);

    @Query(value = "SELECT * " +
            "FROM registrations " +
            "WHERE event_id = :eventId " +
            "AND feedback IS NOT NULL " +
            "AND rating != 0", nativeQuery = true)
    Page<Registration> findFeedbacksByEvent(Long eventId, Pageable pageable);

    boolean existsByUserAndEventAndRegistrationIdNot(User user, Event event, Long id);

    Page<Registration> findByEvent(Event event, Pageable pageable);

    Page<Registration> findByUser(User user, Pageable pageable);
}
