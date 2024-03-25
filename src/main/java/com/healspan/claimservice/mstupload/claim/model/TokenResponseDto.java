package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenResponseDto {

    private String access_token;
    private Long expires_in;
    private String scope;
    private String token_type;
}
