package com.project.aclub.repository;

import com.project.aclub.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = "Select * from users " +
            "where (:name is NULL OR name like  CONCAT( '%', :name, '%'))  " +
            "AND (:rollNumber is NULL OR roll_number like CONCAT( '%', :rollNumber, '%')) " +
            "AND (:phoneNumber is NULL OR phone_number like CONCAT( '%', :phoneNumber, '%')) " +
            "AND (:email is NULL OR email like CONCAT( '%', :email, '%'))",
            nativeQuery = true)
    List<User> searchUsers(@Param("name") String name, @Param("rollNumber") String rollNumber,
                           @Param("phoneNumber") String phoneNumber, @Param("email") String email, Pageable pageable);
}

