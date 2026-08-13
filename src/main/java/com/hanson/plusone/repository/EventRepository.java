package com.hanson.plusone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hanson.plusone.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByStartsAtAsc();

    List<Event> findByHost_IdOrderByStartsAtAsc(Long hostUserId);

}