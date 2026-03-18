package com.project.aclub.repository;

import com.project.aclub.entity.Club;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    boolean existsByClubCodeOrClubName(String clubCode, String clubName);

    boolean existsByClubIdNotAndClubCodeOrClubIdNotAndClubName(Long id, @NotBlank String clubCode, Long id1, @NotBlank String clubName);
}
