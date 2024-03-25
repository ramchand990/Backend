package com.healspan.claimservice.mstupload.claim.dto.business;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReviewerResponseDto {

    private List<Long> documentId;
    private Long userMstId;
    private String status;
    private String reviewerNote;
    private List<String> documentName;
}
