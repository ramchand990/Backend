package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClaimSlaDto {
    private String stage;
    private Double consumedSla;

    public ClaimSlaDto(String stage, Double consumedSla) {
        this.stage = stage;
        this.consumedSla = consumedSla;
    }
}
