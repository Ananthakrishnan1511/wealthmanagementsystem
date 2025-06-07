package com.cts.wealthmanagementsystem.controller;

import com.cts.wealthmanagementsystem.entity.PortfolioItem;
import com.cts.wealthmanagementsystem.entity.Report;
import com.cts.wealthmanagementsystem.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes("portfolioItems")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @ModelAttribute("portfolioItems")
    public List<PortfolioItem> portfolioItems() {
        return List.of();
    }

    @GetMapping("/report")
    public String reportPage(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        return "report";
    }

    @PostMapping("/generateReport")
    public String generateReport(@ModelAttribute("portfolioItems") List<PortfolioItem> portfolioItems) {
        reportService.generateReport(portfolioItems);
        return "redirect:/report";
    }

    @GetMapping("/downloadReport/{id}")
    public void downloadReport(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Report report = reportService.getAllReports().stream()
                .filter(r -> r.getReportId().equals(id))
                .findFirst()
                .orElseThrow();

        byte[] pdf = reportService.exportReportToPdfBytes(report);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report_" + id + ".pdf");
        response.getOutputStream().write(pdf);
    }
}
