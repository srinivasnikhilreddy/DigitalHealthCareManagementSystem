import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import { ReportService } from '../../../../core/services/report.service';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { Report } from '../../../../core/models/report.model';
import { Appointment } from '../../../../core/models/appointment.model';

@Component({
  selector: 'app-report-detail',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatSelectModule, FormsModule],
  templateUrl: './report-detail.component.html',
  styleUrls: ['./report-detail.component.css']
})
export class ReportDetailComponent implements OnInit {
  reports: Report[] = [];
  appointments: Appointment[] = [];
  selectedAppointmentId: number | null = null;
  loading = true;
  error: string | null = null;

  private reportService = inject(ReportService);
  private appointmentService = inject(AppointmentService);

  displayedColumns: string[] = ['id', 'appointmentId', 'type', 'status', 'reportData'];

  ngOnInit(): void {
    this.loadAppointments();
  }

  loadAppointments() {
    const patientId = Number(localStorage.getItem('patientId'));
    if (!patientId) {
      this.error = 'Invalid patient ID';
      this.loading = false;
      return;
    }

    this.appointmentService.getByPatient(patientId).subscribe({
      next: (data) => {
        this.appointments = data;
        this.loading = false;
        // Select first appointment by default
        if (this.appointments.length > 0) {
          this.selectedAppointmentId = this.appointments[0].id!;
          this.loadReports();
        }
      },
      error: () => {
        this.error = 'Failed to load appointments';
        this.loading = false;
      }
    });
  }

  loadReports()
  {
    if(!this.selectedAppointmentId) return;

    this.loading = true;
    this.reportService.getReportsByAppointment(this.selectedAppointmentId).subscribe({
      next: (data) => {
        this.reports = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load reports';
        this.loading = false;
      }
    });
  }

  onAppointmentChange() {
    this.loadReports();
  }
}
