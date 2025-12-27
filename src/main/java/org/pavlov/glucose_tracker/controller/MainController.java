package org.pavlov.glucose_tracker.controller;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.pavlov.glucose_tracker.dto.GlucoseEntry;
import org.pavlov.glucose_tracker.dto.GlucoseEntryRequest;
import org.pavlov.glucose_tracker.jpa.service.GlucoseEntryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/entries")
public class MainController {

    private final GlucoseEntryService service;

    public MainController(GlucoseEntryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GlucoseEntry> createEntry(@Valid @RequestBody GlucoseEntryRequest request) {
        GlucoseEntry entry = service.save(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entry.getId())
                .toUri();

        return ResponseEntity.created(location).body(entry);
    }

    @GetMapping
    public ResponseEntity<List<GlucoseEntry>> getAllEntries(
            @RequestParam Optional<String> sortBy,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {

        List<GlucoseEntry> entries = new ArrayList<>(service.findAll());

        // normalize pagination params
        if (page < 1) {
            page = 1;
        }
        page = page - 1; // zero-based page index
        if (size <= 0) {
            size = 5;
        }

        if (sortBy.isPresent()) {
            String key = sortBy.get().toLowerCase(Locale.ROOT).trim();
            switch (key) {
                case "timestamp_asc":
                case "timestamp":
                    entries.sort(Comparator.comparing(GlucoseEntry::getTimestamp));
                    break;
                case "timestamp_desc":
                    entries.sort(Comparator.comparing(GlucoseEntry::getTimestamp).reversed());
                    break;
                case "value_asc":
                case "value":
                    entries.sort(Comparator.comparing(GlucoseEntry::getValue));
                    break;
                case "value_desc":
                    entries.sort(Comparator.comparing(GlucoseEntry::getValue).reversed());
                    break;
                default:
                    break;
            }
        }

        // apply pagination and reuse total
        int total = entries.size();
        long firstElementIndex = (long) page * (long) size;
        if (firstElementIndex >= total) {
            return ResponseEntity.ok()
                    .header("X-Total-Count", String.valueOf(total))
                    .body(Collections.emptyList());
        }
        int fromIndex = (int) firstElementIndex;
        int toIndex = Math.min(fromIndex + size, total);
        List<GlucoseEntry> pageList = entries.subList(fromIndex, toIndex);

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(total))
                .body(pageList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlucoseEntry> updateEntry(@PathVariable Long id, @Valid @RequestBody GlucoseEntryRequest request) {
        return service.findById(id)
                .map(existing -> {
                    // copy mutable fields from request to the existing entity
                    existing.setDescription(request.getDescription());
                    existing.setTimestamp(request.getTimestamp().toInstant());
                    existing.setValue(request.getValue());
                    existing.setPunctureSpot(request.getPunctureSpot());

                    // persist the updated entity (ensure your service exposes save(GlucoseEntry) or update(id, request))
                    GlucoseEntry updated = service.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // REST function in backend that will return a list of recommended spots for next puncture
    @GetMapping("/recommended-spots")
    public List<String> getRecommendedPunctureSpots() {
        return service.getRecommendedPunctureSpots();
    }

}