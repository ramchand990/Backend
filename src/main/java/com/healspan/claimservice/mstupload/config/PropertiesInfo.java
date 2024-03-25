package com.healspan.claimservice.mstupload.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@ToString
public class PropertiesInfo {
    //DataSource Connection

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String databaseSchema;

    //jpa Config
    @Value("${spring.jpa.show-sql}")
    private String jpaShowSql;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String jpaDdlAuto;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String jpaDialet;

    @Value("${spring.jpa.properties.hibernate.format_sql}")
    private String jpaFormatSql;

    @Value("${spring.main.allow-circular-references}")
    private String jpaReference;


    @Value("${eureka.client.serviceUrl.defaultZone}")
    private String eureka;

    @Value("${AWSAccessKeyId}")
    private String awsId;

    @Value("${AWSSecretKey}")
    private String awsKey;

    @Value("${s3bucketname}")
    private String s3bucketname;

    //RPA Service Communication

    @Value("${rpa.service.token.url}")
    private String rpaUrl;

    @Value("${rpa.service.claim.consumer.url}")
    private String rpaConsumer;

    @Value("${rpa.service.claim.username}")
    private String rpaUsername;

    @Value("${rpa.service.claim.password}")
    private String rpaPassword;

    //jwt configuration
    @Value("${jwt.session.timeout.in.seconds}")
    private String seconds;



}

