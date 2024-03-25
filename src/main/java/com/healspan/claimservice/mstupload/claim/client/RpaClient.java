package com.healspan.claimservice.mstupload.claim.client;

import com.google.gson.Gson;
import com.healspan.claimservice.mstupload.claim.model.HeaderRequestParameter;
import com.healspan.claimservice.mstupload.claim.model.RpaRequiredDetailDto;
import com.healspan.claimservice.mstupload.claim.model.TokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class RpaClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HeaderRequestParameter parameter;

    @Autowired
    private Gson gson;

    @Value("${rpa.service.token.url}")
    String tokenGenerationUrl;

    @Value("${rpa.service.claim.consumer.url}")
    String rpaClaimDetailsConsumerUrl;

    public TokenResponseDto getToken(){
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(parameter.getTokenGenerationRequestValueMap(),parameter.getTokenGenerationHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(tokenGenerationUrl, HttpMethod.POST,httpEntity, String.class);
        TokenResponseDto dto = gson.fromJson(responseEntity.getBody(),TokenResponseDto.class);
        System.out.println("getToken-1 :: "+dto.getAccess_token());
        System.out.println("getToken-2 :: "+responseEntity.getStatusCode());
        return dto;
    }

    public void pushClaimDataToRpa(RpaRequiredDetailDto dto, String token){
        String requestBody = gson.toJson(dto);
        System.out.println("pushClaimDataToRpa-1-Req Body :: "+requestBody);
        MultiValueMap<String, String> headerValueMap = parameter.getRpaRequestValueMap(token);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody,headerValueMap);
        ResponseEntity<String> responseEntity = restTemplate.exchange(rpaClaimDetailsConsumerUrl, HttpMethod.POST,httpEntity, String.class);
        System.out.println("pushClaimDataToRpa-2 :: "+responseEntity.getBody());
        System.out.println("pushClaimDataToRpa-3 :: "+responseEntity.getStatusCode());
    }
}
