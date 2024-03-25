package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.constants.Constant.AssignmentFlowType;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ClaimStatus;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ResponseStatus;
import com.healspan.claimservice.mstupload.claim.dao.business.ClaimInfo;
import com.healspan.claimservice.mstupload.claim.dao.business.TpaUpdate;
import com.healspan.claimservice.mstupload.claim.dao.master.StatusMst;
import com.healspan.claimservice.mstupload.claim.dto.business.AssignmentDetail;
import com.healspan.claimservice.mstupload.claim.dto.business.BaseRequestDto;
import com.healspan.claimservice.mstupload.claim.dto.business.TpaResponseDto;
import com.healspan.claimservice.mstupload.claim.exception.ApplicationResponse;
import com.healspan.claimservice.mstupload.claim.exception.InvalidStatusException;
import com.healspan.claimservice.mstupload.claim.repo.ClaimInfoRepo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.healspan.claimservice.mstupload.claim.repo.TpaUpdateRepo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TpaServiceImpl implements TpaService{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TpaUpdateRepo tpaUpdateRepo;
    @Autowired
    private ClaimInfoRepo claimInfoRepo;
    @Autowired
    private UpdateClaimAssignmentService updateClaimAssignmentService;

    private Logger logger = LoggerFactory.getLogger(TpaServiceImpl.class);

    @Override
    public ApplicationResponse receiveTpaResponse(TpaResponseDto dto) {
        try {
            logger.info("TpaServiceImpl::receiveTpaResponse::START");
            BaseRequestDto baseRequestDto = modelMapper.map(dto,BaseRequestDto.class);
            setFlowType(dto,baseRequestDto);
            AssignmentDetail detail = updateClaimAssignmentService.prepareAssignmentDetail(baseRequestDto);
            checkIfAlreadyApprovedOrRejectedByTpa(detail.getCurrentStatus());
            insertDetailsInTpaUpdate(detail, dto);
            updateClaimAssignmentService.setNewUserAndStatus(detail);
            updateClaimAssignmentService.insertCommunicationDetailsInCaseAssignment(detail);
            logger.info("TpaServiceImpl::receiveTpaResponse::END");
        }catch (Exception ex){
            logger.error("TpaServiceImpl::receiveTpaResponse::Exception::{}", ExceptionUtils.getStackTrace(ex));
            return new ApplicationResponse(ResponseStatus.FAILED,LocalDateTime.now());
        }
        return new ApplicationResponse(ResponseStatus.SUCCESS,LocalDateTime.now());
    }

    private void setFlowType(TpaResponseDto tpaDto,BaseRequestDto baseRequestDto){
        if(ClaimStatus.APPROVED.equals(tpaDto.getStatus()))
            baseRequestDto.setFlowType(AssignmentFlowType.TPA_USER_APPROVED_CLAIM);
        else if(ClaimStatus.REJECTED.equals(tpaDto.getStatus()))
            baseRequestDto.setFlowType(AssignmentFlowType.TPA_USER_REJECTED_CLAIM);
        else if(ClaimStatus.QUERIED.equals(tpaDto.getStatus()))
            baseRequestDto.setFlowType(AssignmentFlowType.TPA_USER_QUERIED_AGAINST_CLAIM);

    }

    private void checkIfAlreadyApprovedOrRejectedByTpa(StatusMst status){
        String currentStatus = status.getName().replaceAll("\\s+", "_");
        if(ClaimStatus.APPROVED.name().equalsIgnoreCase(currentStatus) || ClaimStatus.TPA_QUERY.name().equalsIgnoreCase(currentStatus) || ClaimStatus.REJECTED.name().equalsIgnoreCase(currentStatus))
            throw new InvalidStatusException("Claim is already in APPROVED or TPA_QUERY status...!!!");
        else
            logger.info("TpaServiceImpl::checkIfAlreadyApprovedOrRejectedByTpa::Claim in not in APPROVED or TPA_QUERY status....!!!!");
    }

    private void insertDetailsInTpaUpdate(AssignmentDetail detail,TpaResponseDto dto){
        TpaUpdate tpaUpdate = new TpaUpdate();
        tpaUpdate.setTpaClaimNo(dto.getClaimNumber());
        tpaUpdate.setApprovedAmount(dto.getApprovedAmount());
        tpaUpdate.setStatus(dto.getStatus().name());
        tpaUpdate.setRemarks(dto.getTransferComment());
        tpaUpdate.setClaimInfo(detail.getClaimInfo());
        tpaUpdate.setClaimStage(detail.getClaimStage());
        tpaUpdate.setClaimStageLink(detail.getClaimStageLink());
        tpaUpdate.setRecordInsertionDate(LocalDateTime.now());
        tpaUpdateRepo.save(tpaUpdate);
        updateClaimTpaNumber(detail.getClaimInfo(),dto.getClaimNumber());
    }

    private void updateClaimTpaNumber(ClaimInfo claimInfo,String tpaNumber){
        claimInfo.setTpaClaimNumber(tpaNumber);
        claimInfoRepo.save(claimInfo);
    }
}
