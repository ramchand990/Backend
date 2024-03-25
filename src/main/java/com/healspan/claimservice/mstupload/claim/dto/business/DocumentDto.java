package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DocumentDto {

    private Long id;
    private Long documentsMstId;
    private String mandatoryDocumentName;
    private String documentName;
    private String documentPath;
    private boolean status;
}
