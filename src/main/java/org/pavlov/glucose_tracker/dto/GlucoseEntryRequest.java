// java
        package org.pavlov.glucose_tracker.dto;

        import java.time.OffsetDateTime;

        import jakarta.validation.constraints.NotNull;
        import jakarta.validation.constraints.Positive;

        import com.fasterxml.jackson.annotation.JsonFormat;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public class GlucoseEntryRequest {
            @Positive
            private double value;

            @NotNull
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            private OffsetDateTime timestamp;

            private String description;
        }