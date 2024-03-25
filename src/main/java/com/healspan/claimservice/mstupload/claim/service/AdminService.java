package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.model.MasterDetails;

import java.util.List;
import java.util.Map;

public interface AdminService
{
    Map<String, List> getMasterTableData();
    public  Map<String,Object> getMasterTableDataName (String tableName);

    public String deleteMasterDataById(String tableName, Long parseLong);

    boolean insertMstData(MasterDetails masterDetails);


}
