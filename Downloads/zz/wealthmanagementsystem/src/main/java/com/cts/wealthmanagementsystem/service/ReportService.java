package com.cts.wealthmanagementsystem.service;

import com.cts.wealthmanagementsystem.entity.Report;
import com.cts.wealthmanagementsystem.entity.PortfolioItem;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface ReportService {
    Report generateReport(List<PortfolioItem> portfolioItems);
    List<Report> getAllReports();
    ByteArrayOutputStream generatePdfReport(Report report);
	byte[] exportReportToPdfBytes(Report report);
}
