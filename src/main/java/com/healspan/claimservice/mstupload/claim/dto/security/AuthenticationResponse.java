package com.healspan.claimservice.mstupload.claim.dto.security;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class AuthenticationResponse implements Serializable {
    private final String jwt;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final Long userTypeId;
    private final Long userId;

    public AuthenticationResponse(String jwt, String userName, String firstName, String lastName, Long userTypeId, Long userId) {
        this.jwt = jwt;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userTypeId = userTypeId;
        this.userId = userId;
    }
}
