package com.project.aclub.repository;

import com.project.aclub.Enum.Role;
import com.project.aclub.entity.Club;
import com.project.aclub.entity.ClubMembership;
import com.project.aclub.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubMembershipRepository extends JpaRepository<ClubMembership, Long> {
    boolean existsByUserAndClub(User user, Club club);

    Page<ClubMembership> findClubMembershipByClub(Club club, Pageable pageable);

    boolean existsByUserAndClubAndRole(User user, Club club, @NotNull(message = "role should not be empty") Role role);

    Optional<ClubMembership> findByUserAndClub(User user, Club club);
}
