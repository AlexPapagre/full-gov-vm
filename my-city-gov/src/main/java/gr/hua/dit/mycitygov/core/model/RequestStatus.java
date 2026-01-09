package gr.hua.dit.mycitygov.core.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestStatus {
    SUBMITTED("Submitted"),
    RECEIVED("Received"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    REJECTED("Rejected");

    private final String label;
}
