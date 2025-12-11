package org.pavlov.glucose_tracker.controller;

import java.net.URI;
import java.util.List;

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
    public List<GlucoseEntry> getAllEntries() {
        return service.findAll();
    }
}