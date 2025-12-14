// java
package org.pavlov.glucose_tracker.jpa.service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
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

    public List<String> getRecommendedPunctureSpots() {
        List<GlucoseEntry> recentEntries = findAll().stream()
                .filter(e -> e.getPunctureSpot() != null && !e.getPunctureSpot().isBlank())
                .sorted(Comparator.comparing(GlucoseEntry::getTimestamp).reversed())
                .limit(20)
                .collect(Collectors.toList());

        Map<String, Integer> spotToIndex = new LinkedHashMap<>();
        for (int i = 0; i < recentEntries.size(); i++) {
            String spot = Optional.ofNullable(recentEntries.get(i).getPunctureSpot())
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .orElse("");
            spotToIndex.putIfAbsent(spot, i);
        }

        List<String> allSpots = new ArrayList<>();
        String[] hands = {"L", "R"};
        String[] sides = {"L", "C", "R"};
        for (String hand : hands) {
            for (int finger = 1; finger <= 5; finger++) {
                for (String side : sides) {
                    allSpots.add(hand + finger + side);
                }
            }
        }

        return allSpots.stream()
                .sorted((a, b) -> {
                    boolean aUsed = spotToIndex.containsKey(a);
                    boolean bUsed = spotToIndex.containsKey(b);
                    if (!aUsed && !bUsed) return 0;
                    if (!aUsed) return -1;
                    if (!bUsed) return 1;
                    return Integer.compare(spotToIndex.get(b), spotToIndex.get(a));
                })
                .collect(Collectors.toList());
    }
}