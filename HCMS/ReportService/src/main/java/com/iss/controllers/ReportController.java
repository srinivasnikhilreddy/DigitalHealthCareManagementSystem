package com.iss.controllers;

import com.iss.models.Report;
import com.iss.services.ReportService;
import com.hcms.common.enums.ReportType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // DTO for safe response
    record ReportDTO(Long id, Long appointmentId, ReportType type, String status, String reportData) {
        static ReportDTO fromEntity(Report report) {
            return new ReportDTO(
                    report.getId(),
                    report.getAppointment().getId(),
                    report.getType(),
                    report.getStatus().name(),
                    report.getReportData()
            );
        }
    }

    // POST http://localhost:9086/reports/createReport/4?type=PRESCRIPTION&reportData=Take medicine twice a day
    @PostMapping("/createReport/{appointmentId}")
    public ResponseEntity<ReportDTO> createReport(@PathVariable Long appointmentId,
                                                  @RequestParam ReportType type,
                                                  @RequestParam(required = false) String reportData) {
        Report report = reportService.createReport(appointmentId, type, reportData);
        return ResponseEntity.ok(ReportDTO.fromEntity(report));
    }

    // GET http://localhost:9086/reports/4
    @GetMapping("/{appointmentId}")
    public ResponseEntity<List<ReportDTO>> getReports(@PathVariable Long appointmentId) {
        List<Report> reports = reportService.getReportsByAppointment(appointmentId);
        if (reports.isEmpty()) return ResponseEntity.noContent().build();

        List<ReportDTO> dtos = reports.stream()
                .map(ReportDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // POST http://localhost:9086/reports/deliver/2
    @PostMapping("/deliver/{reportId}")
    public ResponseEntity<ReportDTO> markDelivered(@PathVariable Long reportId) {
        Report report = reportService.markDelivered(reportId);
        return ResponseEntity.ok(ReportDTO.fromEntity(report));
    }
}
