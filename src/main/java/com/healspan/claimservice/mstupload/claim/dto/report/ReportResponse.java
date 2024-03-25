package com.healspan.claimservice.mstupload.claim.dto.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReportResponse {
    String fileName;
    byte[] reportData;
}
