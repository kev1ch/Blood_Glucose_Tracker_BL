// java
package org.pavlov.glucose_tracker.jpa.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.pavlov.glucose_tracker.dto.GlucoseEntry;
import org.pavlov.glucose_tracker.dto.GlucoseEntryRequest;
import org.pavlov.glucose_tracker.jpa.entity.GlucoseEntryT;
import org.pavlov.glucose_tracker.jpa.repository.GlucoseEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GlucoseEntryService {

    private final GlucoseEntryRepository repository;

    public GlucoseEntryService(GlucoseEntryRepository repository) {
        this.repository = repository;
    }

    public GlucoseEntry save(GlucoseEntryRequest request) {
        OffsetDateTime odt = request.getTimestamp();
        Instant instant = (odt == null) ? null : odt.toInstant();

        GlucoseEntryT entity = new GlucoseEntryT(
            request.getValue(),
            instant,
            request.getDescription(),
            request.getPunctureSpot()
        );

        GlucoseEntryT saved = repository.save(entity);
        return toDto(saved);
    }

    public GlucoseEntry save(GlucoseEntryT entity) {
        var record = repository.save(entity);
        return toDto(record);
    }

    public List<GlucoseEntry> findAll() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private GlucoseEntry toDto(GlucoseEntryT e) {
        OffsetDateTime ts = (e.getTimestamp() == null)
            ? null
            : OffsetDateTime.ofInstant(e.getTimestamp(), ZoneOffset.UTC);

        return new GlucoseEntry(e.getId(), e.getValue(), ts, e.getDescription(), e.getPunctureSpot());
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<GlucoseEntryT> findById(Long id) {
        return repository.findById(id);
    }
}