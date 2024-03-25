package com.healspan.claimservice.mstupload.claim.controller;

import com.healspan.claimservice.mstupload.claim.dto.business.TpaResponseDto;
import com.healspan.claimservice.mstupload.claim.exception.ApplicationResponse;
import com.healspan.claimservice.mstupload.claim.service.TpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/tpa/claim")
public class TpaController {

    @Autowired
    TpaService service;

    private Logger logger = LoggerFactory.getLogger(TpaController.class);

    @PostMapping("/tparesponse")
    public ResponseEntity<ApplicationResponse> receiveTpaResponse(@RequestBody TpaResponseDto dto){
        logger.info("TpaController::receiveTpaResponse::TpaResponseDto::{}",dto);
        return new ResponseEntity<>(service.receiveTpaResponse(dto), HttpStatus.OK);
    }
}
