package com.healspan.claimservice.mstupload.claim.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ResponseStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ApplicationResponse {

    private ResponseStatus status;
    private String errorMessage;
    private LocalDateTime dateTime;

    public ApplicationResponse(ResponseStatus status, LocalDateTime dateTime) {
        this.status = status;
        this.dateTime = dateTime;
    }
}
