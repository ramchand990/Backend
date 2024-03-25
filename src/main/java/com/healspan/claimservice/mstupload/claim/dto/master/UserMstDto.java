package com.healspan.claimservice.mstupload.claim.dto.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserMstDto {

    private Long id;
    private String jwt;
    private String userName;
    private String firstName;
    private String lastName;
    private Long hospitalMstId;
    private Long userRoleMstId;
}