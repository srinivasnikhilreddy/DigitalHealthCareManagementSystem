package com.iss.services;

import com.hcms.common.events.*;
import com.hcms.common.enums.ReportType;
import com.hcms.common.enums.ReportStatus;
import com.iss.models.Report;
import com.iss.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportListenerService
{
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ReportService reportService;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "PaymentCompleted", groupId = "ReportGroup", containerFactory = "kafkaListenerContainerFactory")
    public void handlePaymentCompleted(PaymentCompletedEvent event)
    {
        System.out.println("Received PaymentCompletedEvent: " + event);
        try{
            // Delegate creation to ReportService
            reportService.createReport(event.getAppointmentId(), ReportType.PRESCRIPTION, "Take a medicine once in a day");

            System.out.println("Payment-triggered report created for appointment " + event.getAppointmentId());
        }catch(Exception e){
            System.out.println("Failed to create report for payment: " + e.getMessage());
            e.printStackTrace();
        }

        /*System.out.println("ReportService received PaymentCompletedEvent: " + event);
        try{
            Report report = Report.builder()
                    .appointmentId(event.getAppointmentId())
                    .type(ReportType.PRESCRIPTION) // can dynamically decide based on AI/logic
                    .status(ReportStatus.GENERATED)
                    .build();
            reportRepository.save(report);

            ReportGeneratedEvent reportEvent = ReportGeneratedEvent.builder()
                    .reportId(report.getId())
                    .appointmentId(report.getAppointmentId())
                    .type(report.getType())
                    .build();

            kafkaTemplate.send("ReportGenerated", reportEvent);
            System.out.println("Published ReportGeneratedEvent: " + reportEvent);

        }catch(Exception e){
            ReportFailedEvent failed = ReportFailedEvent.builder()
                    .appointmentId(event.getAppointmentId())
                    .reason("AI analysis failed")
                    .build();
            kafkaTemplate.send("ReportFailed", failed);
            System.out.println("Published ReportFailedEvent: " + failed);
        }*/
    }
}
