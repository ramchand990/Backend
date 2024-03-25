package com.healspan.claimservice.mstupload.claim.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class HeaderRequestParameter {

    @Value("${rpa.service.claim.username}")
    private String userName;

    @Value("${rpa.service.claim.password}")
    private String password;

    @Value("${rpa.service.token.unit.id}")
    private String tokenUnitId;

    @Value("${rpa.service.apaarebsjgei.unit.id}")
    private String apaarRpaUnitId;

    public HttpHeaders getTokenGenerationHeaders(){
        HttpHeaders headers = new HttpHeaders();
        /*headers.add("X-UIPATH-OrganizationUnitId","Document Understanding/apaarinfosystemspvtltd");*/
        headers.add("X-UIPATH-OrganizationUnitId",tokenUnitId);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED.toString());
        return headers;
    }

    public MultiValueMap<String, String> getTokenGenerationRequestValueMap(){
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("grant_type","client_credentials");
        requestBody.add("client_id",userName);
        requestBody.add("client_secret",password);
        requestBody.add("scope","OR.Jobs OR.Queues OR.Tasks OR.Folders OR.Machines OR.Robots OR.Execution");
        return requestBody;
    }

    public MultiValueMap<String, String> getRpaRequestValueMap(String token){
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("Authorization","Bearer "+token);
        requestBody.add("Content-Type",MediaType.APPLICATION_JSON.toString());
        requestBody.add("X-UIPATH-OrganizationUnitId",apaarRpaUnitId);
        return requestBody;
    }
}
