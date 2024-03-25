package com.healspan.claimservice.mstupload.claim.jdbcrepo;

import com.healspan.claimservice.mstupload.claim.constants.HealspanQueries;
import com.healspan.claimservice.mstupload.claim.model.ReviewerClaimsData;
import com.healspan.claimservice.mstupload.config.PropertiesConfig;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.text.MessageFormat;

@Component
public class ClaimRepo {

    @Autowired
    PropertiesConfig propertiesConfig;

    private Logger logger = LoggerFactory.getLogger(ClaimRepo.class);

    public List<ReviewerClaimsData> deriveClaimsForReviewerDashboard(long loggerInUserId) {
        logger.info("ClaimRepo::deriveClaimsForReviewerDashboard::DateTime::{}::START" ,LocalDateTime.now());
        List<ReviewerClaimsData> dtoList = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             Statement stmt = con.createStatement()) {
            String query2 = MessageFormat.format(HealspanQueries.RECEIVER_DASHBOARD_QUERY,loggerInUserId);
            ResultSet resultSet = stmt.executeQuery(query2);
            while (resultSet.next()) {
                ReviewerClaimsData dto = new ReviewerClaimsData();
                dto.setClaimID(resultSet.getLong("claim_info_id"));
                dto.setClaimStageLinkId(resultSet.getLong("claim_stage_link_id"));
                dto.setName(resultSet.getString("full_name"));
                dto.setAilment(resultSet.getString("ailment"));
                dto.setStage(resultSet.getString("claim_stage"));
                dto.setStatus(resultSet.getString("claim_status"));
                dto.setPTat("-");
                dto.setApprovedAmount(Double.parseDouble(resultSet.getString("approved_amount")));
                dto.setHospital(resultSet.getString("hospital_name"));
                dto.setSlaPercent(resultSet.getLong("sla_percent"));
                dto.setCreatedDate(resultSet.getDate(5));
                dtoList.add(dto);
            }
        }catch (SQLException e){
            logger.error("ClaimRepo::deriveClaimsForReviewerDashboard::Exception::{}" , ExceptionUtils.getStackTrace(e));
        }
        logger.info("ClaimRepo::deriveClaimsForReviewerDashboard::DateTime::{}::END" , LocalDateTime.now());
        return dtoList;
    }

    public String getHospitalUserDashboardDataFromDb(long loggerInUserId) {
        logger.info("ClaimRepo::getHospitalUserDashboardDataFromDb::START::{}",LocalDateTime.now());
        String jsonObject = "";
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             Statement stmt = con.createStatement()) {
            String query2 = MessageFormat.format(HealspanQueries.HOSPITAL_DASHBOARD_QUERY,loggerInUserId);
            ResultSet resultSet = stmt.executeQuery(query2);
            while (resultSet.next()) {
                jsonObject =resultSet.getString(1);
            }
        }catch (SQLException e){
            logger.error("ClaimRepo::getHospitalUserDashboardDataFromDb::Exception::{}",ExceptionUtils.getStackTrace(e));
        }
        logger.info("ClaimRepo::getHospitalUserDashboardDataFromDb::END::{}",LocalDateTime.now());
        return jsonObject;
    }

    public String getHealspanUserDashboardDataFromDb(long loggerInUserId) {
        logger.debug("ClaimRepo::getHealspanUserDashboardDataFromDb requestedLoggedInUserId: {}",loggerInUserId);
        String jsonObject = "";
        try (Connection con = DriverManager.getConnection(propertiesConfig.getUrl(), propertiesConfig.getUsername(), propertiesConfig.getPassword());
             Statement stmt = con.createStatement()) {
            String query2 = MessageFormat.format(HealspanQueries.HEALSPAN_DASHBOARD_QUERY,loggerInUserId);
            ResultSet resultSet = stmt.executeQuery(query2);
            while (resultSet.next()) {
                jsonObject =resultSet.getString(1);
            }
        }catch (SQLException e){
            logger.error("ClaimRepo::getHealspanUserDashboardDataFromDb::Exception::{}",ExceptionUtils.getStackTrace(e));
        }
        logger.info("ClaimRepo::getHealspanUserDashboardDataFromDb responseData: {}",jsonObject);
        return jsonObject;
    }
}
