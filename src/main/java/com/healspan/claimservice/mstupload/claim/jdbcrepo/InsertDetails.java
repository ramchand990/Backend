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
public class InsertDetails {

    @Autowired
    PropertiesConfig propertiesConfig;

    private Logger logger = LoggerFactory.getLogger(InsertDetails.class);

    public MasterResponse insertMasterDetails(MasterDetails masterDetails) {
        logger.info("InsertDetails::insertMasterDetails::DateTime::{}::START", LocalDateTime.now());
        TableRequestParameters parameters = masterDetails.getRequestParameters();
        MasterResponse masterResponse = new MasterResponse();
        masterResponse.setTableName(masterDetails.getTableName());
        String insertQuery = "insert into healspan." + masterDetails.getTableName() + "(id,name) values(nextval('healspan." + masterDetails.getTableName() + "_id_seq'),?);";
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, parameters.getName());
            pstmt.executeUpdate();
            masterResponse.setStatus(Constant.ResponseStatus.SUCCESS.name());
        } catch (SQLException e) {
            logger.error("InsertDetails::insertMasterDetails::Exception::{}" + ExceptionUtils.getStackTrace(e));
            masterResponse.setStatus(Constant.ResponseStatus.FAILED.name());
            e.printStackTrace();
        }
        logger.info("InsertDetails::insertMasterDetails::DateTime::{}::END", LocalDateTime.now());
        return masterResponse;
    }

    public MasterResponse insertMasterDetailsForHospitalMst(MasterDetails masterDetails) {
        logger.info("InsertDetails::insertMasterDetailsForHospitalMst::DateTime::{}::START", LocalDateTime.now());
        TableRequestParameters parameters = masterDetails.getRequestParameters();
        MasterResponse masterResponse = new MasterResponse();
        masterResponse.setTableName(masterDetails.getTableName());
        String insertQuery = "insert into healspan." + masterDetails.getTableName() + "(id,hospital_code,name) values(nextval('healspan." + masterDetails.getTableName() + "_id_seq'),?,?);";
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, parameters.getHospitalCode());
            pstmt.setString(2, parameters.getName());
            pstmt.executeUpdate();
            masterResponse.setStatus(Constant.ResponseStatus.SUCCESS.name());
        } catch (SQLException e) {
            logger.error("InsertDetails::insertMasterDetailsForHospitalMst::Exception::{}" + ExceptionUtils.getStackTrace(e));
            masterResponse.setStatus(Constant.ResponseStatus.FAILED.name());
            e.printStackTrace();
        }
        logger.info("InsertDetails::insertMasterDetailsForHospitalMst::DateTime::{}::END", LocalDateTime.now());
        return masterResponse;
    }

    public MasterResponse insertMasterDetailsForUserMst(MasterDetails masterDetails) {
        logger.info("InsertDetails::insertMasterDetailsForUserMst::DateTime::{}::START", LocalDateTime.now());
        TableRequestParameters parameters = masterDetails.getRequestParameters();
        MasterResponse masterResponse = new MasterResponse();
        masterResponse.setTableName(masterDetails.getTableName());
        String insertQuery = "insert into healspan." + masterDetails.getTableName() + "(id,username,email,first_name,is_active,mobile_no,last_name,password,hospital_mst_id,user_role_mst_id)" +
                " values(nextval('healspan." + masterDetails.getTableName() + "_id_seq'),?,?,?,?,?,?,?,?,?);";
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setString(1, parameters.getUserName());
            pstmt.setString(2, parameters.getEmail());
            pstmt.setString(3, parameters.getFirstName());
            pstmt.setBoolean(4, parameters.getIsActive());
            pstmt.setString(5, parameters.getMobileNo());
            pstmt.setString(6, parameters.getLastName());
            pstmt.setString(7, parameters.getPassword());
            pstmt.setLong(8, parameters.getHospitalMstId());
            pstmt.setLong(9, parameters.getUserRoleMstId());
            pstmt.executeUpdate();
            masterResponse.setStatus(Constant.ResponseStatus.SUCCESS.name());
        } catch (SQLException e) {
            logger.error("InsertDetails::insertMasterDetailsForUserMst::Exception::{}" + ExceptionUtils.getStackTrace(e));
            masterResponse.setStatus(Constant.ResponseStatus.FAILED.name());
            e.printStackTrace();
        }
        logger.info("InsertDetails::insertMasterDetailsForUserMst::DateTime::{}::END", LocalDateTime.now());
        return masterResponse;
    }


}