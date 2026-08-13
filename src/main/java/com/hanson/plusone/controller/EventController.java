package com.hanson.plusone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.hanson.plusone.dto.CreateEventRequest;
import com.hanson.plusone.dto.EventResponse;
import com.hanson.plusone.model.Event;
import com.hanson.plusone.model.User;
import com.hanson.plusone.model.UserProfile;
import com.hanson.plusone.repository.EventRepository;
import com.hanson.plusone.repository.UserProfileRepository;
import com.hanson.plusone.repository.UserRepository;
import com.hanson.plusone.model.EventAttendance;
import com.hanson.plusone.repository.EventAttendanceRepository;
import com.hanson.plusone.dto.AttendeeResponse;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository profileRepository;
    private final EventAttendanceRepository attendanceRepository;

    public EventController(
            EventRepository eventRepository,
            UserRepository userRepository,
            UserProfileRepository profileRepository,
            EventAttendanceRepository attendanceRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(   // creates event
            @Valid @RequestBody CreateEventRequest request,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User account no longer exists"));

        UserProfile profile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Create a profile before hosting an event"));

        Event event = new Event(
                user,
                request.title().trim(),
                request.description().trim(),
                request.location().trim(),
                request.category().trim(),
                request.startsAt(),
                request.maxAttendees());

        Event savedEvent = eventRepository.save(event);

        long attendeeCount = attendanceRepository.countByEvent_Id(event.getId());
        long spotsRemaining = Math.max(0L, event.getMaxAttendees() - attendeeCount);

        return new EventResponse(
                savedEvent.getId(),
                user.getId(),
                profile.getDisplayName(),
                savedEvent.getTitle(),
                savedEvent.getDescription(),
                savedEvent.getLocation(),
                savedEvent.getCategory(),
                savedEvent.getStartsAt(),
                savedEvent.getMaxAttendees(),
                savedEvent.getCreatedAt(),
                attendeeCount,
                spotsRemaining
            );
    }

    @GetMapping
    public List<EventResponse> getAllEvents() { // list events

        return eventRepository.findAllByOrderByStartsAtAsc()
                .stream()
                .map(event -> {
                    UserProfile hostProfile = profileRepository
                            .findByUser_Id(event.getHost().getId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "Event host profile not found"));
                    long attendeeCount = attendanceRepository.countByEvent_Id(event.getId());
                    long spotsRemaining = Math.max(0L, event.getMaxAttendees() - attendeeCount);

                    return new EventResponse(
                            event.getId(),
                            event.getHost().getId(),
                            hostProfile.getDisplayName(),
                            event.getTitle(),
                            event.getDescription(),
                            event.getLocation(),
                            event.getCategory(),
                            event.getStartsAt(),
                            event.getMaxAttendees(),
                            event.getCreatedAt(),
                            attendeeCount,
                            spotsRemaining
                        );
                })
                .toList();
    }

    @GetMapping("/{eventId}")
    public EventResponse getEventById(@PathVariable Long eventId) { // event by id

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event not found"));

        UserProfile hostProfile = profileRepository
                .findByUser_Id(event.getHost().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Event host profile not found"));
        long attendeeCount = attendanceRepository.countByEvent_Id(event.getId());
        long spotsRemaining = Math.max(0L, event.getMaxAttendees() - attendeeCount);

        return new EventResponse(
            event.getId(),
            event.getHost().getId(),
            hostProfile.getDisplayName(),
            event.getTitle(),
            event.getDescription(),
            event.getLocation(),
            event.getCategory(),
            event.getStartsAt(),
            event.getMaxAttendees(),
            event.getCreatedAt(),
            attendeeCount,
            spotsRemaining
        );
    }

    @PutMapping("/{eventId}")
    public EventResponse updateEvent(   // updates event
            @PathVariable Long eventId,
            @Valid @RequestBody CreateEventRequest request,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event not found"));

        if (!event.getHost().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only update your own events");
        }

        event.update(
                request.title().trim(),
                request.description().trim(),
                request.location().trim(),
                request.category().trim(),
                request.startsAt(),
                request.maxAttendees());

        Event savedEvent = eventRepository.save(event);

        UserProfile hostProfile = profileRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Event host profile not found"));
        long attendeeCount = attendanceRepository.countByEvent_Id(savedEvent.getId());
        long spotsRemaining = Math.max(0L, savedEvent.getMaxAttendees() - attendeeCount);
        return new EventResponse(
                savedEvent.getId(),
                userId,
                hostProfile.getDisplayName(),
                savedEvent.getTitle(),
                savedEvent.getDescription(),
                savedEvent.getLocation(),
                savedEvent.getCategory(),
                savedEvent.getStartsAt(),
                savedEvent.getMaxAttendees(),
                savedEvent.getCreatedAt(),
                attendeeCount,
                spotsRemaining
            );
    }

    @DeleteMapping("/{eventId}")    // deletes event
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(
            @PathVariable Long eventId,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event not found"));

        if (!event.getHost().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only delete your own events");
        }

        eventRepository.delete(event);
    }

    @PostMapping("/{eventId}/join")
    @ResponseStatus(HttpStatus.CREATED)
    public void joinEvent(                  // join event
            @PathVariable Long eventId,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Event not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User account no longer exists"));

        if (event.getHost().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You are already the host of this event");
        }

        if (attendanceRepository
                .existsByEvent_IdAndUser_Id(eventId, userId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You have already joined this event");
        }

        long attendeeCount =
                attendanceRepository.countByEvent_Id(eventId);

        if (attendeeCount >= event.getMaxAttendees()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This event is full");
        }

        EventAttendance attendance =
                new EventAttendance(event, user);

        attendanceRepository.save(attendance);
    }

    @GetMapping("/{eventId}/attendees")
    public List<AttendeeResponse> getEventAttendees(
            @PathVariable Long eventId) {

        if (!eventRepository.existsById(eventId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Event not found");
        }

        return attendanceRepository
                .findByEvent_IdOrderByJoinedAtAsc(eventId)
                .stream()
                .map(attendance -> {
                    Long attendeeUserId = attendance.getUser().getId();

                    UserProfile profile = profileRepository
                            .findByUser_Id(attendeeUserId)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "Attendee profile not found"));

                    return new AttendeeResponse(
                            attendeeUserId,
                            profile.getDisplayName(),
                            attendance.getJoinedAt());
                })
                .toList();
    }

    @DeleteMapping("/{eventId}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveEvent(
        @PathVariable Long eventId,
        HttpSession session
    ) {
        Long userId = (Long) session.getAttribute("userId");

        if(userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "You must be logged in."
            );
        }

        if (!eventRepository.existsById(eventId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Event not found."
            );
        }

        EventAttendance attendance = attendanceRepository
            .findByEvent_IdAndUser_Id(eventId, userId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "You have not joined this event."
            ));
        attendanceRepository.delete(attendance);
    }

    @GetMapping("/joined")
    public List<EventResponse> getMyJoinedEvents(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in");
        }

        return attendanceRepository
                .findByUser_IdOrderByJoinedAtDesc(userId)
                .stream()
                .map(attendance -> {
                    Event event = attendance.getEvent();

                    UserProfile hostProfile = profileRepository
                            .findByUser_Id(event.getHost().getId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.INTERNAL_SERVER_ERROR,
                                    "Event host profile not found"));
                    long attendeeCount = attendanceRepository.countByEvent_Id(event.getId());
                    long spotsRemaining = Math.max(0L, event.getMaxAttendees() - attendeeCount);
                    return new EventResponse(
                            event.getId(),
                            event.getHost().getId(),
                            hostProfile.getDisplayName(),
                            event.getTitle(),
                            event.getDescription(),
                            event.getLocation(),
                            event.getCategory(),
                            event.getStartsAt(),
                            event.getMaxAttendees(),
                            event.getCreatedAt(),
                            attendeeCount,
                            spotsRemaining
                            );
                })
                .toList();
    }

    @GetMapping("/hosted")
    public List<EventResponse> getMyHostedEvents(HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in");
        }

        UserProfile hostProfile = profileRepository
                .findByUser_Id(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profile not found"));

        return eventRepository
                .findByHost_IdOrderByStartsAtAsc(userId)
                .stream()
                .map(event -> {
                    long attendeeCount = attendanceRepository.countByEvent_Id(event.getId());
                    long spotsRemaining = Math.max(0L, event.getMaxAttendees() - attendeeCount);
                    return new EventResponse(
                            event.getId(),
                            userId,
                            hostProfile.getDisplayName(),
                            event.getTitle(),
                            event.getDescription(),
                            event.getLocation(),
                            event.getCategory(),
                            event.getStartsAt(),
                            event.getMaxAttendees(),
                            event.getCreatedAt(),
                            attendeeCount,
                            spotsRemaining
                    );
                })
                .toList();
    }

}