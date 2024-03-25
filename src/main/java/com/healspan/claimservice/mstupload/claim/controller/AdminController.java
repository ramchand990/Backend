package com.healspan.claimservice.mstupload.claim.controller;

import com.healspan.claimservice.mstupload.claim.jdbcrepo.InsertDetails;
import com.healspan.claimservice.mstupload.claim.jdbcrepo.UpdateDetails;
import com.healspan.claimservice.mstupload.claim.model.MasterDetails;
import com.healspan.claimservice.mstupload.claim.model.MasterResponse;
import com.healspan.claimservice.mstupload.claim.service.AdminServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/healspan/claim")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @Autowired
    private InsertDetails insertDetails;

    @Autowired
    private UpdateDetails updateDetails;

    @GetMapping("/admin/masters")
    public ResponseEntity<Map<String, List>> getMasterData() throws Exception {
        logger.info("AdminController::getMasterData --Start");
        return new ResponseEntity<Map<String, List>>(adminServiceImpl.getMasterTableData(), HttpStatus.OK);

    }

    //Get Master Data by table name
    @GetMapping("/admin/masters/name/{tableName}")
    public ResponseEntity<Map<String, Object>> getMasterDataName(@PathVariable String tableName) throws Exception {
        logger.info("AdminController::getMasterDataName masterTable:{}", tableName);
        Map<String, Object> mstObj = adminServiceImpl.getMasterTableDataName(tableName);
        if (mstObj != null) {
            logger.info("Successfully retrieved for tableName:{}", tableName);
            return new ResponseEntity<Map<String, Object>>(mstObj, HttpStatus.OK);
        } else {
            logger.info("Data not retrieved for tableName:{}", tableName);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //Delete MasterData by Table id
    @GetMapping("/admin/masters/name/{tableName}/{tableId}")
    public ResponseEntity<String> deleteMasterDataById(@PathVariable String tableName, @PathVariable String tableId) {
        try {
            logger.info("AdminController::deleteMasterDataById tableName:{} and tableId:{} ", tableName, tableId);
            return new ResponseEntity<>(this.adminServiceImpl.deleteMasterDataById(tableName, Long.parseLong(tableId)), HttpStatus.OK);
        } catch (Exception e) {
            logger.info("Exception Details: \n {}", e.getMessage());
            return new ResponseEntity<>("Data Not Present", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   /* @PostMapping("/admin/masters/insert")
    public ResponseEntity<String>createByTableName(@RequestBody MasterDetails masterDetails){
        if(adminServiceImpl.insertMstData(masterDetails)){
            return new ResponseEntity<>("Status Update SUCCESS", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Status Update FAILED", HttpStatus.NOT_MODIFIED);
        }
    }
    @PutMapping("/admin/masters/update")
    public ResponseEntity<String>updateByTableName(@RequestBody MasterDetails masterDetails){
        if(adminServiceImpl.updateMasterData(masterDetails)){
            return new ResponseEntity<>("Status Update SUCCESS", HttpStatus.OK);
        }  else {
            return new ResponseEntity<>("Status Update FAILED", HttpStatus.NOT_MODIFIED);
        }
    }*/
    @PostMapping("/admin/masters/insert")
    public ResponseEntity<MasterResponse>createByTableName(@RequestBody MasterDetails masterDetails){
        logger.info("AdminController::createByTableName masterDetails: {}",masterDetails);
        MasterResponse response = new MasterResponse();
        if("user_mst".equalsIgnoreCase(masterDetails.getTableName())){
            response =insertDetails.insertMasterDetailsForUserMst(masterDetails);
        }else if ("hospital_mst".equalsIgnoreCase(masterDetails.getTableName())){
            response =insertDetails.insertMasterDetailsForHospitalMst(masterDetails);
        }else {
            response =insertDetails.insertMasterDetails(masterDetails);
        }
        return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
    }

    @PutMapping("/admin/masters/update")
    public ResponseEntity<MasterResponse>updateByTableName(@RequestBody MasterDetails masterDetails){
        logger.info("AdminController::updateByTableName masterDetails: {}",masterDetails);
        MasterResponse response = new MasterResponse();
        if("user_mst".equalsIgnoreCase(masterDetails.getTableName())){
            response =updateDetails.updateMasterDetailsForUserMst(masterDetails);
        }else if("hospital_mst".equalsIgnoreCase(masterDetails.getTableName())){
            response =updateDetails.updateMasterDetailsForHospitalMst(masterDetails);
        }else {
            response =updateDetails.updateMasterDetails(masterDetails);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}