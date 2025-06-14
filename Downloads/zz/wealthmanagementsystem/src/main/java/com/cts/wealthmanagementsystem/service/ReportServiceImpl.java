
package com.cts.wealthmanagementsystem.service;
 
import com.cts.wealthmanagementsystem.entity.PortfolioItem;
import com.cts.wealthmanagementsystem.entity.Report;
import com.cts.wealthmanagementsystem.repository.ReportRepository;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
 
@Service
public class ReportServiceImpl implements ReportService {
 
    @Autowired
    private ReportRepository reportRepository;
 
    @Override
    public Report generateReport(List<PortfolioItem> portfolioItems) {
        double totalCurrentValue = portfolioItems.stream().mapToDouble(PortfolioItem::getCurrentValue).sum();
        double totalProfitLoss = portfolioItems.stream().mapToDouble(PortfolioItem::getProfitLoss).sum();
 
        Report report = new Report(LocalDate.now(), totalCurrentValue, totalProfitLoss, portfolioItems);
        return reportRepository.save(report);
    }
 
    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
 
   
    @Override
    public ByteArrayOutputStream generatePdfReport(Report report) {
      
        byte[] pdfBytes = exportReportToPdfBytes(report);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(pdfBytes);
        } catch (IOException e) {
            System.err.println("Error converting PDF bytes to ByteArrayOutputStream: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to process PDF output.", e);
        }
        return out;
    }
 
   
    @Override
    public byte[] exportReportToPdfBytes(Report report) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
 
 
com.lowagie.text.Font nseFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Font.BOLD,Color.BLUE);
Paragraph nseTitle = new Paragraph("NSE", nseFont);
nseTitle.setAlignment(Paragraph.ALIGN_LEFT);
document.add(nseTitle);
 
 
 
LocalDate today = LocalDate.now();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
String formattedDate = today.format(formatter);
 
document.add(new Paragraph(formattedDate, FontFactory.getFont(FontFactory.HELVETICA, 10)));
 
            document.add(new Paragraph("Dear Investor,", FontFactory.getFont(FontFactory.HELVETICA, 10)));
           
            document.add(Chunk.NEWLINE);
 
           
            document.add(new Paragraph("With reference to NSE circulars NSE/INSP/46704 and NSE/INSP/55039, and NCL circular NCL/CMPL/49348, please find below your fund and securities balance as on " + report.getReportDate() + ".", FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(Chunk.NEWLINE);
 
        
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(100);
            summaryTable.setSpacingBefore(10f);
            summaryTable.setSpacingAfter(10f);
            
            summaryTable.addCell("Total Current Value:");
            summaryTable.addCell(String.format("₹%.2f", report.getTotalCurrentValue()));
            summaryTable.addCell("Total Profit/Loss:");
            summaryTable.addCell(String.format("₹%.2f", report.getTotalProfitLoss()));
            document.add(summaryTable);
 
          
            document.add(new Paragraph("ISIN-wise Securities Details:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            document.add(Chunk.NEWLINE);
 
            if (report.getPortfolioItems() != null && !report.getPortfolioItems().isEmpty()) {
            	   for (PortfolioItem item : report.getPortfolioItems()) {
                       document.add(new Paragraph(item.getName() + " - Buy: $" + item.getBuyValue() +
                               ", Current: $" + item.getCurrentValue() +
                               ", P/L: $" + item.getProfitLoss() +
                               ", Allocation: " + item.getAllocationPercentage() + "%"));
                   }
            } else {
                document.add(new Paragraph("No securities data available."));
            }
 
           
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Disclaimer", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            com.lowagie.text.Font  normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
 
            document.add(new Paragraph("Please note that a negative number in the file represents a debit balance or balance payable by you.", normalFont));
            document.add(new Paragraph("Also, the funds and securities balances provided are those maintained with your broker and do not include balances in your personal bank account or demat account.", normalFont));
            document.add(new Paragraph("You are advised to compare the funds, securities, and commodities balances received from the Exchange on a weekly basis with the balances in the weekly statement of accounts sent by the stockbroker for the respective date.", normalFont));
            document.add(new Paragraph("In case you find any discrepancy in the balances, we advise you to take up the matter immediately with your broker. You may contact customer service at helpdesk@sbicapsec.com or 022-69515555.", normalFont));
            document.add(new Paragraph("In case of non-resolution of discrepancy by the broker, you may intimate the same to ignse@nse.co.in or 18002660050 or register an online complaint at https://smartodr.in/login.", normalFont));
            document.add(new Paragraph("The data is being provided to the clients on an 'as is' and 'what-is' basis as provided by the trading member. The Exchange shall not be liable for any delays, errors, omissions, commissions, or inaccuracies in rendering the data.", normalFont));
            document.add(new Paragraph("In no event shall the Exchange be liable for any damages, including without limitation direct or indirect, special, incidental, or consequential damages, losses, or expenses arising in connection with the data provided by the Exchange through this facility.", normalFont));
            document.add(new Paragraph("You are requested to note that in the event of a trading member being declared as defaulter or expelled, all transactions executed on the Exchange trading platform within 90 calendar days prior to the effective date of disablement of the trading member, or after the latest monthly/quarterly settlement date (whichever is later), shall be considered eligible for compensation from IPFT, subject to the Rules, Byelaws, Regulations, guidelines, etc., of the Exchange and SEBI circulars, and subject to the appropriate norms laid down by the relevant Committee.", normalFont));
            document.add(new Paragraph("For details, you may refer to the policy hosted on the website: https://static.nseindia.com//s3fs-public/inline-files/Policy_for_evaluation_of_claims_0.pdf", normalFont));
 
 
        
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Investor Do's & Don'ts", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            com.lowagie.text.Font  normalFont1 = FontFactory.getFont(FontFactory.HELVETICA, 10);
 
            document.add(new Paragraph("Please note that a negative number in the file represents a debit balance or balance payable by you.", normalFont1));
            document.add(new Paragraph("Also, the funds and securities balances provided are those maintained with your broker and do not include balances in your personal bank account or demat account.", normalFont1));
            document.add(new Paragraph("You are advised to compare the funds, securities, and commodities balances received from the Exchange on a weekly basis with the balances in the weekly statement of accounts sent by the stockbroker for the respective date.", normalFont1));
            document.add(new Paragraph("In case you find any discrepancy in the balances, please take up the matter immediately with your broker. You may contact customer service at helpdesk@sbicapsec.com or 022-69515555.", normalFont1));
            document.add(new Paragraph("If the issue is not resolved by the broker, you may escalate it to ignse@nse.co.in or 18002660050, or register an online complaint at https://smartodr.in/login.", normalFont1));
            document.add(new Paragraph("The data is provided to clients on an 'as is' and 'what-is' basis as received from the trading member. The Exchange shall not be liable for any delays, errors, omissions, or inaccuracies in the data.", normalFont1));
            document.add(new Paragraph("In no event shall the Exchange be liable for any damages, including direct or indirect, special, incidental, or consequential losses arising from the use of this data.", normalFont1));
            document.add(new Paragraph("In the event of a trading member being declared a defaulter or expelled, all transactions executed on the Exchange platform within 90 calendar days prior to the effective date of disablement, or after the latest monthly/quarterly settlement date (whichever is later), shall be considered eligible for compensation from IPFT, subject to applicable rules and regulations.", normalFont1));
            document.add(new Paragraph("For more details, refer to the policy at: https://static.nseindia.com//s3fs-public/inline-files/Policy_for_evaluation_of_claims_0.pdf", normalFont));
 
 
 
 
            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Failed to generate investor-style PDF report.", e);
        }
    }

	
}
