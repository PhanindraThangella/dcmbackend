package com.dcm.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcm.backend.entity.DayPurse;

public interface DayPurseRepository extends JpaRepository<DayPurse,Long> {
	List<DayPurse> findTop10ByCreatedAtBetween(LocalDateTime start,LocalDateTime end);
	Optional<DayPurse> findTopByCreatedAtBetweenOrderByIdDesc(LocalDateTime start,LocalDateTime end);
	Optional<DayPurse> findTopByOrderByIdDesc();
}
