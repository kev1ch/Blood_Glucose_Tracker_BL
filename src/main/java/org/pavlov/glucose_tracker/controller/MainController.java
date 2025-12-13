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

}