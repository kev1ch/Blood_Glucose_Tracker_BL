package org.pavlov.glucose_tracker.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "glucose_entries")
@Data
public class GlucoseEntryT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "glucose_value", nullable = false)
    private double value;

    @Column(name = "recorded_at")
    private Instant timestamp;

    @Column(name = "description")
    private String description;

    @Column(name = "puncture_spot")
    private String punctureSpot;

    protected GlucoseEntryT() { }

    public GlucoseEntryT(double value, Instant timestamp, String description, String punctureSpot) {
        this.value = value;
        this.timestamp = timestamp;
        this.description = description;
        this.punctureSpot = punctureSpot;
    }

}