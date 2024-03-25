package com.healspan.claimservice.mstupload.claim.jdbcrepo;

import com.healspan.claimservice.mstupload.claim.constants.Constant;
import com.healspan.claimservice.mstupload.claim.model.MasterDetails;
import com.healspan.claimservice.mstupload.claim.model.MasterResponse;
import com.healspan.claimservice.mstupload.claim.model.TableRequestParameters;
import com.healspan.claimservice.mstupload.config.PropertiesConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;

@Component
public class UpdateDetails {

    @Autowired
    private PropertiesConfig propertiesConfig;

    private Logger logger = LoggerFactory.getLogger(UpdateDetails.class);

    String message = "update healspan.claim_assignment set completed_date_time=current_timestamp where claim_info_id = :claimInfoId and claim_stage_mst_id = :claimStageMstId and completed_date_time IS NULL";

    public MasterResponse updateMasterDetails(MasterDetails masterDetails) {
        logger.info("InsertDetails::updateMasterDetails::DateTime::{}::START", LocalDateTime.now());
        TableRequestParameters parameters = masterDetails.getRequestParameters();
        MasterResponse masterResponse = new MasterResponse();
        masterResponse.setTableName(masterDetails.getTableName());
        String updateQuery = "update healspan." + masterDetails.getTableName() + " set name = '" + parameters.getName() + "' where id = " + parameters.getId() + ";";
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.executeUpdate();
            masterResponse.setStatus(Constant.ResponseStatus.SUCCESS.name());
        } catch (SQLException e) {
            logger.error("InsertDetails::insertChronic_illness_Details::Exception::{}" + ExceptionUtils.getStackTrace(e));
            masterResponse.setStatus(Constant.ResponseStatus.FAILED.name());
            e.printStackTrace();
        }
        logger.info("InsertDetails::updateMasterDetails::DateTime::{}::END", LocalDateTime.now());
        return masterResponse;
    }

    public MasterResponse updateMasterDetailsForHospitalMst(MasterDetails masterDetails) {
        logger.info("InsertDetails::updateMasterDetailsForHospitalMst::DateTime::{}::START", LocalDateTime.now());
        TableRequestParameters parameters = masterDetails.getRequestParameters();
        MasterResponse masterResponse = new MasterResponse();
        masterResponse.setTableName(masterDetails.getTableName());
        String updateQuery = "update healspan." + masterDetails.getTableName() + " set hospital_code = '" + parameters.getHospitalCode() + "' ,name = '" + parameters.getName() + "' where id = " + parameters.getId() + ";";
        System.out.println("UpdateQuery::"+updateQuery);
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.executeUpdate();
            masterResponse.setStatus(Constant.ResponseStatus.SUCCESS.name());
        } catch (SQLException e) {
            logger.error("InsertDetails::updateMasterDetailsForHospitalMst::Exception::{}" + ExceptionUtils.getStackTrace(e));
            masterResponse.setStatus(Constant.ResponseStatus.FAILED.name());
            e.printStackTrace();
        }
        logger.info("InsertDetails::updateMasterDetailsForHospitalMst::DateTime::{}::END", LocalDateTime.now());
        return masterResponse;
    }

    public MasterResponse updateMasterDetailsForUserMst(MasterDetails masterDetails) {
        logger.info("InsertDetails::updateMasterDetailsForUserMst::DateTime::{}::START", LocalDateTime.now());
        TableRequestParameters parameters = masterDetails.getRequestParameters();
        MasterResponse masterResponse = new MasterResponse();
        masterResponse.setTableName(masterDetails.getTableName());
        String updateQuery = "update healspan." + masterDetails.getTableName()
                + " set username = '" + parameters.getUserName()
                + "' , first_name = '" + parameters.getFirstName()
                + "' , email = '" + parameters.getEmail()
                + "' , is_active = '" + parameters.getIsActive()
                + "' , mobile_no = '" + parameters.getMobileNo()
                + "' , last_name = '" + parameters.getLastName()
                + "' , password = '" + parameters.getPassword()
                + "' , hospital_mst_id = " + parameters.getHospitalMstId()
                + " , user_role_mst_id = " + parameters.getUserRoleMstId()
                + " where id = " + parameters.getId() + ";";
        System.out.println("UpdateQuery::"+updateQuery);
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             PreparedStatement pstmt = con.prepareStatement(updateQuery)) {
            pstmt.executeUpdate();
            masterResponse.setStatus(Constant.ResponseStatus.SUCCESS.name());
        } catch (SQLException e) {
            logger.error("InsertDetails::updateMasterDetailsForUserMst::Exception::{}" + ExceptionUtils.getStackTrace(e));
            masterResponse.setStatus(Constant.ResponseStatus.FAILED.name());
            e.printStackTrace();
        }
        logger.info("InsertDetails::updateMasterDetailsForUserMst::DateTime::{}::END", LocalDateTime.now());
        return masterResponse;
    }
}