package com.project.aclub.repository;

import com.project.aclub.Enum.Role;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.Participation;
import com.project.aclub.entity.User;
import jakarta.mail.Part;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByUserAndClub(User user, Club club);

    Page<Participation> findParticipationByClub(Club club, Pageable pageable);

    boolean existsByUserAndClubAndRole(User user, Club club, @NotNull(message = "role should not be empty") Role role);
}
