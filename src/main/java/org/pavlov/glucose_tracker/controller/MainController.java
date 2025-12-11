package org.pavlov.glucose_tracker.controller;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.validation.Valid;

import org.pavlov.glucose_tracker.dto.GlucoseEntry;
import org.pavlov.glucose_tracker.dto.GlucoseEntryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/entries")
public class MainController {

    private final List<GlucoseEntry> store = new CopyOnWriteArrayList<>();
    private final AtomicLong idGen = new AtomicLong();

    @PostMapping
    public ResponseEntity<GlucoseEntry> createEntry(@Valid @RequestBody GlucoseEntryRequest request) {
        GlucoseEntry entry = new GlucoseEntry(
            idGen.incrementAndGet(),
            request.getValue(),
            request.getTimestamp(),
            request.getDescription()
        );
        store.add(entry);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(entry.getId())
            .toUri();

        return ResponseEntity.created(location).body(entry);
    }

    @GetMapping
    public List<GlucoseEntry> getAllEntries() {
        return store;
    }
}