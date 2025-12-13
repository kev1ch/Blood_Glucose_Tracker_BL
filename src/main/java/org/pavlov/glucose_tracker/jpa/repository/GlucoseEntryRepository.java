package org.pavlov.glucose_tracker.jpa.repository;

import org.pavlov.glucose_tracker.jpa.entity.GlucoseEntryT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlucoseEntryRepository extends JpaRepository<GlucoseEntryT, Long> {
}