package com.healspan.claimservice.mstupload.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class MasterRequest {
    private String mstName;
    private List<String> mstData;
}
