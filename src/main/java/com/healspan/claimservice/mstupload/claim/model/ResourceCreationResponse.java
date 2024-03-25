package com.healspan.claimservice.mstupload.claim.model;

import com.healspan.claimservice.mstupload.claim.constants.Constant.ResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ResourceCreationResponse {
    private Long claimInfoId;
    private Long patientInfoId;
    private Long medicalInfoId;
    private Long insuranceInfoId;
    private Long claimStageLinkId;
    private Map<Long,Long> documentId;
    private ResponseStatus responseStatus;
}
