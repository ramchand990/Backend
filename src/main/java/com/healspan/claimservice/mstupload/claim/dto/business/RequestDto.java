package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RequestDto {
    private Long claimStageLinkId;
    private Long claimStageId;
    private Long statusId;
    private Long userId;
}