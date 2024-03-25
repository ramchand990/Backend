package com.healspan.claimservice.mstupload.claim.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemData {

    private String DeferDate = "2021-03-11T14:19:56.4407392Z";
    private String DueDate = "2021-03-11T15:19:56.4407392Z";
    private String Priority = "Normal";
    private String Name = "Postman";
    private String Reference = "Petstore";
    private SpecificContent SpecificContent;
}
