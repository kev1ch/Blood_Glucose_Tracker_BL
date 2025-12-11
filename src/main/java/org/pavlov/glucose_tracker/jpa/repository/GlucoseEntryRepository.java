package org.pavlov.glucose_tracker.jpa.repository;

import org.pavlov.glucose_tracker.jpa.entity.GlucoseEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlucoseEntryRepository extends JpaRepository<GlucoseEntryEntity, Long> {
}