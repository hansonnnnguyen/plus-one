package com.hanson.plusone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanson.plusone.model.UserProfile;

public interface UserProfileRepository
        extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);
}