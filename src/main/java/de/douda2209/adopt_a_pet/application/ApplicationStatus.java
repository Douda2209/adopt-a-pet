package de.douda2209.adopt_a_pet.application;

import java.util.Set;

public enum ApplicationStatus {

    SUBMITTED,
    IN_REVIEW,
    MEETING_SCHEDULED,
    APPROVED,
    REJECTED,
    WITHDRAWN;


    public Set<ApplicationStatus> allowedTransitions() {
        return switch (this) {
            case SUBMITTED -> Set.of(REJECTED, IN_REVIEW, WITHDRAWN);
            case IN_REVIEW -> Set.of(REJECTED, MEETING_SCHEDULED, WITHDRAWN);
            case MEETING_SCHEDULED -> Set.of(APPROVED, WITHDRAWN, REJECTED);
            case APPROVED, REJECTED, WITHDRAWN -> Set.of();

        };
    }

    public boolean canTransitionTo(ApplicationStatus target) {
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }
        return allowedTransitions().contains(target);
    }

    public boolean isFinal() {
        return allowedTransitions().isEmpty();
    }
}
