package com.hanson.plusone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.hanson.plusone.dto.CreateProfileRequest;
import com.hanson.plusone.dto.ProfileResponse;
import com.hanson.plusone.model.User;
import com.hanson.plusone.model.UserProfile;
import com.hanson.plusone.repository.UserProfileRepository;
import com.hanson.plusone.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profiles")
public class UserProfileController {

    private final UserProfileRepository profileRepository;
    private final UserRepository userRepository;

    public UserProfileController(
            UserProfileRepository profileRepository,
            UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse createProfile(
            @Valid @RequestBody CreateProfileRequest request,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "You must be logged in"
            );
        }

        if (profileRepository.existsByUser_Id(userId)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "You already have a profile"
            );
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User account no longer exists"
            ));

        UserProfile profile = new UserProfile(
            user,
            request.displayName().trim(),
            request.bio() == null ? null : request.bio().trim(),
            request.city() == null ? null : request.city().trim(),
            request.connectionGoal() == null ? null : request.connectionGoal().trim()
        );

        UserProfile savedProfile = profileRepository.save(profile);

        return new ProfileResponse(
            savedProfile.getId(),
            user.getId(),
            savedProfile.getDisplayName(),
            savedProfile.getBio(),
            savedProfile.getCity(),
            savedProfile.getConnectionGoal(),
            savedProfile.getCreatedAt()
        );
    }

    @GetMapping("/me")
    public ProfileResponse getMyProfile(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "You must be logged in"
            );
        }

        UserProfile profile = profileRepository.findByUser_Id(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Profile not found"
            ));

        return new ProfileResponse(
            profile.getId(),
            userId,
            profile.getDisplayName(),
            profile.getBio(),
            profile.getCity(),
            profile.getConnectionGoal(),
            profile.getCreatedAt()
        );
    }

    @PutMapping("/me")
        public ProfileResponse updateMyProfile(
            @Valid @RequestBody CreateProfileRequest request,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "You must be logged in"
            );
        }

        UserProfile profile = profileRepository.findByUser_Id(userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Profile not found"
            ));

        profile.update(
            request.displayName().trim(),
            request.bio() == null ? null : request.bio().trim(),
            request.city() == null ? null : request.city().trim(),
            request.connectionGoal() == null ? null : request.connectionGoal().trim()
        );

        UserProfile savedProfile = profileRepository.save(profile);

        return new ProfileResponse(
            savedProfile.getId(),
            userId,
            savedProfile.getDisplayName(),
            savedProfile.getBio(),
            savedProfile.getCity(),
            savedProfile.getConnectionGoal(),
            savedProfile.getCreatedAt()
        );
    }
}