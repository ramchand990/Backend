package com.healspan.claimservice.mstupload.claim.dto.business;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.ByteArrayOutputStream;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DocumentDownloadDto {

    private ByteArrayOutputStream byteArrayOutputStream;
    private String fileName;

}
