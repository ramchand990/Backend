package com.healspan.claimservice.mstupload.claim.initialize;

import com.healspan.claimservice.mstupload.claim.dao.business.MedicalInfo;
import com.healspan.claimservice.mstupload.claim.dao.business.PatientInfo;
import com.healspan.claimservice.mstupload.claim.dto.business.MedicalInfoDto;
import com.healspan.claimservice.mstupload.claim.dto.business.PatientInfoDto;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.healspan.claimservice.mstupload.claim.dao.master.SlaMst;
import com.healspan.claimservice.mstupload.claim.repo.SlaMstRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ClaimStage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
@Scope(value="singleton")
public class MasterStore {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    SlaMstRepo slaMstRepo;

    Map<ClaimStage,Long> stageWiseSlaMap = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(MasterStore.class);

    public void initialize(){
        initializeTypeMapForDao();
        initializeStageWiseSlaMap();
    }

    public void initializeTypeMapForDao(){
        System.out.println("TypeMap has been initialized from PatientInfoDao.....!!!!!");
        TypeMap<PatientInfoDto, PatientInfo> propertyMapperPatientInfo = modelMapper.createTypeMap(PatientInfoDto.class, PatientInfo.class);
        propertyMapperPatientInfo.addMappings(mapper -> mapper.skip(PatientInfo::setPatientAndOtherCostLink));

        System.out.println("TypeMap has been initialized from MedicalInfoDao.....!!!!!");
        TypeMap<MedicalInfoDto, MedicalInfo> propertyMapperMedicalInfo = modelMapper.createTypeMap(MedicalInfoDto.class, MedicalInfo.class);
        propertyMapperMedicalInfo.addMappings(mapper -> mapper.skip(MedicalInfo::setMedicalAndChronicIllnessLink));

    }

    public void initializeStageWiseSlaMap(){
        List<SlaMst> slaMstList = slaMstRepo.findAll();
        if(!slaMstList.isEmpty()){
            SlaMst slaMst = slaMstList.get(0);
            stageWiseSlaMap.put(ClaimStage.INITIAL_AUTHORIZATION,slaMst.getInitialAuthorizationSla());
            stageWiseSlaMap.put(ClaimStage.ENHANCEMENT,slaMst.getEnhancementSla());
            stageWiseSlaMap.put(ClaimStage.DISCHARGE,slaMst.getDischargeSla());
            stageWiseSlaMap.put(ClaimStage.FINAL_CLAIM,slaMst.getFinalClaimSla());
            logger.info("StageWiseSlaMap has been initialized from SlaMstRepo.....!!!!!");
        }else
            logger.info("No data fond in SlaMst.....!!!!!");
    }
}
