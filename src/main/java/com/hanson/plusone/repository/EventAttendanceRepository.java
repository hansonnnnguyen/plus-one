package com.hanson.plusone.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanson.plusone.model.EventAttendance;

public interface EventAttendanceRepository
        extends JpaRepository<EventAttendance, Long> {

    boolean existsByEvent_IdAndUser_Id(Long eventId, Long userId);

    long countByEvent_Id(Long eventId);

    Optional<EventAttendance> findByEvent_IdAndUser_Id(
            Long eventId,
            Long userId);

    List<EventAttendance> findByEvent_IdOrderByJoinedAtAsc(
            Long eventId);

    List<EventAttendance> findByUser_IdOrderByJoinedAtDesc(
            Long userId);
}