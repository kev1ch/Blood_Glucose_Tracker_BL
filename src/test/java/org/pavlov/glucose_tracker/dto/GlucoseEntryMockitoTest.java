package org.pavlov.glucose_tracker.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlucoseEntryMockitoTest {

    @Test
    void spy_shouldInvokeSetters_andReflectValues() {
        GlucoseEntry spyEntry = spy(new GlucoseEntry());

        spyEntry.setId(10L);
        spyEntry.setValue(6.7);
        OffsetDateTime ts = OffsetDateTime.parse("2025-03-04T12:34:56+00:00");
        spyEntry.setTimestamp(ts);
        spyEntry.setDescription("after-lunch");
        spyEntry.setPunctureSpot("arm");

        verify(spyEntry).setId(10L);
        verify(spyEntry).setValue(6.7);
        verify(spyEntry).setTimestamp(ts);
        verify(spyEntry).setDescription("after-lunch");
        verify(spyEntry).setPunctureSpot("arm");

        assertEquals(10L, spyEntry.getId());
        assertEquals(6.7, spyEntry.getValue(), 0.0);
        assertEquals(ts, spyEntry.getTimestamp());
        assertEquals("after-lunch", spyEntry.getDescription());
        assertEquals("arm", spyEntry.getPunctureSpot());
    }

    @Test
    void equalsAndHashCode_forIdenticalContent() {
        OffsetDateTime ts = OffsetDateTime.parse("2025-03-04T12:34:56+00:00");
        GlucoseEntry a = new GlucoseEntry(1L, 5.5, ts, "note", "finger");
        GlucoseEntry b = new GlucoseEntry(1L, 5.5, ts, "note", "finger");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void json_roundTrip_preservesOffsetDateTime() throws Exception {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .findAndRegisterModules();

        OffsetDateTime ts = OffsetDateTime.parse("2025-03-04T10:34:56Z");
        GlucoseEntry original = new GlucoseEntry(2L, 7.2, ts, "pre-exercise", "finger");

        String json = mapper.writeValueAsString(original);
        GlucoseEntry read = mapper.readValue(json, GlucoseEntry.class);

        assertEquals(original.getId(), read.getId());
        assertEquals(original.getValue(), read.getValue(), 0.0);
        assertEquals(original.getTimestamp(), read.getTimestamp());
        assertEquals(original.getDescription(), read.getDescription());
        assertEquals(original.getPunctureSpot(), read.getPunctureSpot());
    }
}