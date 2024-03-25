package com.healspan.claimservice.mstupload.config;

import com.healspan.claimservice.mstupload.config.PropertiesConfig;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PropertyConfig {

    private Logger logger = LoggerFactory.getLogger(PropertyConfig.class);

    @Autowired
    private PropertiesInfo propertiesInfo;

    private Map<String,String> propertyMap = new HashMap<>();

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public void getPropertyDetails(){
        propertyMap.put("spring.datasource.url",propertiesInfo.getUrl());
        propertyMap.put("spring.datasource.username",propertiesInfo.getUsername());
        propertyMap.put("spring.datasource.password",propertiesInfo.getPassword());
        propertyMap.put("spring.datasource.driver-class-name",propertiesInfo.getDriverClassName());
        propertyMap.put("spring.jpa.properties.hibernate.default_schema",propertiesInfo.getDatabaseSchema());
        propertyMap.put("spring.jpa.show-sql",propertiesInfo.getJpaShowSql());
        propertyMap.put("spring.jpa.hibernate.ddl-auto",propertiesInfo.getJpaDdlAuto());
        propertyMap.put("spring.jpa.properties.hibernate.dialect",propertiesInfo.getJpaDialet());
        propertyMap.put("spring.jpa.properties.hibernate.format_sql",propertiesInfo.getJpaFormatSql());
        propertyMap.put("spring.main.allow-circular-references",propertiesInfo.getJpaReference());
        propertyMap.put("eureka.client.serviceUrl.defaultZone",propertiesInfo.getEureka());
        propertyMap.put("AWSAccessKeyId",propertiesInfo.getAwsId());
        propertyMap.put("AWSSecretKey",propertiesInfo.getAwsKey());
        propertyMap.put("s3bucketname",propertiesInfo.getS3bucketname());
        propertyMap.put("rpa.service.token.url",propertiesInfo.getRpaUrl());
        propertyMap.put("rpa.service.claim.consumer.url",propertiesInfo.getRpaConsumer());
        propertyMap.put("rpa.service.claim.username",propertiesInfo.getRpaUsername());
        propertyMap.put("rpa.service.claim.password",propertiesInfo.getRpaPassword());
        propertyMap.put("jwt.session.timeout.in.seconds",propertiesInfo.getSeconds());
        logger.info("Property Details::{}",getPropertyMap());
    }
}
