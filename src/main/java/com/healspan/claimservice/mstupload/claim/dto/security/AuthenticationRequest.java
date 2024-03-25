package com.healspan.claimservice.mstupload.claim.dto.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest implements Serializable {

    private String username;
    private String password;
}
