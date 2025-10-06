package com.iss.services;

import com.hcms.common.enums.ReportStatus;
import com.hcms.common.enums.ReportType;
import com.hcms.common.events.ReportGeneratedEvent;
import com.iss.models.Appointment;
import com.iss.models.Report;
import com.iss.repositories.AppointmentRepository;
import com.iss.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService
{
    private final ReportRepository reportRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final AppointmentRepository appointmentRepository;

    public Report createReport(Long appointmentId, ReportType type, String reportData)
    {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Report report = Report.builder()
                .appointment(appointment)
                .type(type)
                .status(ReportStatus.GENERATED)
                .reportData(reportData)
                .build();
        report = reportRepository.save(report);

        // Publish event so AI can process
        ReportGeneratedEvent event = ReportGeneratedEvent.builder()
                .reportId(report.getId())
                .appointmentId(appointment.getId())
                .type(report.getType())
                .reportData(report.getReportData())
                .build();

        kafkaTemplate.send("ReportGenerated", event);
        System.out.println("Published ReportGeneratedEvent: " + event);

        return report;
    }

    public List<Report> getReportsByAppointment(Long appointmentId)
    {
        return reportRepository.findByAppointmentId(appointmentId);
    }

    public Report markDelivered(Long reportId)
    {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));
        report.setStatus(ReportStatus.DELIVERED);
        return reportRepository.save(report);
    }
}
