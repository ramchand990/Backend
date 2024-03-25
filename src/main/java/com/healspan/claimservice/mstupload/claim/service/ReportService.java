package com.healspan.claimservice.mstupload.claim.service;

import com.healspan.claimservice.mstupload.claim.dto.report.ReportResponse;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface ReportService {

    public ReportResponse patientReport(String fileName, Long claimInfoId) throws JRException, FileNotFoundException;
}
