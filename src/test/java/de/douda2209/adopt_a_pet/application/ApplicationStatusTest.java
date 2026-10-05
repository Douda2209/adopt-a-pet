package de.douda2209.adopt_a_pet.application;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationStatusTest {

    @ParameterizedTest
    @CsvSource({"SUBMITTED, IN_REVIEW",
            "SUBMITTED, REJECTED",
            "SUBMITTED, WITHDRAWN",
            "IN_REVIEW, MEETING_SCHEDULED",
            "IN_REVIEW, REJECTED",
            "IN_REVIEW, WITHDRAWN",
            "MEETING_SCHEDULED, APPROVED",
            "MEETING_SCHEDULED, REJECTED",
            "MEETING_SCHEDULED, WITHDRAWN"
    })
    public void canTransitionTo_allowedTransition_returnsTrue(ApplicationStatus from, ApplicationStatus to) {

        boolean result = from.canTransitionTo(to);

        assertTrue(result, from + " -> " + to + " should be allowed");


    }
    @ParameterizedTest
    @CsvSource({"SUBMITTED, APPROVED",
            "APPROVED, SUBMITTED",
            "IN_REVIEW, APPROVED",
            "MEETING_SCHEDULED, SUBMITTED"
    })
    public void canTransitionTo_forbiddenTransition_returnsFalse(ApplicationStatus from, ApplicationStatus to){
        boolean result = from.canTransitionTo(to);
        assertFalse(result, from + " -> " + to + " should be forbidden");

    }
    @ParameterizedTest
    @EnumSource(ApplicationStatus.class)
    public void canTransitionTo_toItself_returnsFalse(ApplicationStatus from) {
        boolean result = from.canTransitionTo(from);
        assertFalse(result, from + " -> " + from + " should be forbidden");

    }

    @ParameterizedTest
    @EnumSource(names = {"APPROVED", "REJECTED", "WITHDRAWN"})
    public void isFinal_finalStatus_returnsTrue(ApplicationStatus from) {
        boolean result = from.isFinal();
        assertTrue(result, from + " -> should be FINAL");

    }

    @ParameterizedTest
    @EnumSource(names = {"APPROVED", "REJECTED", "WITHDRAWN"}, mode = EnumSource.Mode.EXCLUDE)
    public void isFinal_nonfinalStatus_returnsFalse(ApplicationStatus from) {
        boolean result = from.isFinal();
        assertFalse(result, from + " -> should be non FINAL");

    }

    @ParameterizedTest
    @EnumSource(ApplicationStatus.class)
    public void allowedTransitions_returnedSet_isUnmodifiable(ApplicationStatus status) {
        assertThrows(UnsupportedOperationException.class, () -> status.allowedTransitions().add(ApplicationStatus.SUBMITTED), status + " should return an unmodifiable set");

    }

    @ParameterizedTest
    @EnumSource(ApplicationStatus.class)
    public void canTransitionTo_null_throwsIllegalArgumentException(ApplicationStatus status){
       // assertThrows(IllegalArgumentException.class, () -> status.canTransitionTo(null));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> status.canTransitionTo(null));
        assertEquals("target must not be null", ex.getMessage());

    }
}

