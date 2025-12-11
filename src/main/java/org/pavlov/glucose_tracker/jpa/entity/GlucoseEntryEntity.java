package org.pavlov.glucose_tracker.jpa.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "glucose_entries")
public class GlucoseEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "glucose_value", nullable = false)
    private double value;

    @Column(name = "recorded_at")
    private Instant timestamp;

    @Column(name = "description")
    private String description;

    protected GlucoseEntryEntity() { }

    public GlucoseEntryEntity(double value, Instant timestamp, String description) {
        this.value = value;
        this.timestamp = timestamp;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}