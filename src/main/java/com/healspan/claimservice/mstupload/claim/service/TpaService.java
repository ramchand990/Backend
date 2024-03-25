package com.healspan.claimservice.mstupload.claim.service;
import com.healspan.claimservice.mstupload.claim.dto.business.TpaResponseDto;
import com.healspan.claimservice.mstupload.claim.exception.ApplicationResponse;

public interface TpaService {
    ApplicationResponse receiveTpaResponse(TpaResponseDto dto);
}
