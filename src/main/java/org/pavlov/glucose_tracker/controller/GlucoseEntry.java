// java
    package org.pavlov.glucose_tracker.controller;

    import java.time.OffsetDateTime;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class GlucoseEntry {
        private long id;
        private double value;
        private OffsetDateTime timestamp;
        private String description;
    }