package com.healspan.claimservice.mstupload.claim.dto.business;

import com.healspan.claimservice.mstupload.claim.dto.master.ChronicIllnessMstDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MedicalAndChronicIllnessLinkDto {
    private Long id;
    private ChronicIllnessMstDto chronicIllnessMst;
}