import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { ReportService } from '../../../../core/services/report.service';
import { Report } from '../../../../core/models/report.model';
import { Appointment } from '../../../../core/models/appointment.model';
import { UserService } from '../../../../core/services/user.service';
import { UserProfile } from '../../../../core/models/user-profile.model';
import { MatCardModule } from '@angular/material/card';

interface ReportWithPatientName extends Report {
  patientName: string;
}

@Component({
  selector: 'app-report-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './report-list.component.html'
})
export class ReportListComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = ['id', 'patientName', 'type', 'status', 'reportData', 'actions'];
  dataSource = new MatTableDataSource<ReportWithPatientName>([]);
  loading = false;
  error?: string;

  appointments: Appointment[] = [];
  selectedAppointment?: Appointment;

  reportType: string = '';
  reportData: string = '';

  private userService = inject(UserService);

  constructor(
    private reportService: ReportService,
    private appointmentService: AppointmentService
  ) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (user: UserProfile) => {
        if (user.doctorId !== undefined) {
          this.loadAppointments(user.doctorId);
        }
      },
      error: () => (this.error = 'Failed to load user profile')
    });
  }

  loadAppointments(doctorId: number): void {
    this.appointmentService.getByDoctor(doctorId).subscribe({
      next: (appointments: Appointment[]) => {
        this.appointments = appointments;
        appointments.forEach((appointment) => this.loadReports(appointment));
      },
      error: () => (this.error = 'Failed to load appointments')
    });
  }

  loadReports(appointment: Appointment): void {
    this.loading = true;
    this.error = undefined;

    this.reportService.getReportsByAppointment(appointment.id!).subscribe({
      next: (reports: Report[]) => {
        const reportsWithPatient = reports.map((r) => ({
          ...r,
          patientName: appointment.patientName
        }));

        this.dataSource.data = [...this.dataSource.data, ...reportsWithPatient];

        if (!this.dataSource.paginator) this.dataSource.paginator = this.paginator;
        if (!this.dataSource.sort) this.dataSource.sort = this.sort;

        this.loading = false;
      },
      error: () => {
        this.error = 'No reports found.';
        this.loading = false;
      }
    });
  }

  selectAppointment(appointment: Appointment) {
    this.selectedAppointment = appointment;
    this.reportType = '';
    this.reportData = '';
    this.error = undefined;
  }

  createReportForSelectedAppointment(): void {
    if (!this.selectedAppointment || !this.reportType) {
      this.error = 'Please select an appointment and enter report type';
      return;
    }

    this.reportService.createReport(this.selectedAppointment.id!, this.reportType, this.reportData)
      .subscribe({
        next: (newReport: Report) => {
          const reportWithPatient: ReportWithPatientName = {
            ...newReport,
            patientName: this.selectedAppointment!.patientName
          };
          this.dataSource.data = [...this.dataSource.data, reportWithPatient];

          // Reset form
          this.selectedAppointment = undefined;
          this.reportType = '';
          this.reportData = '';
          this.error = undefined;
        },
        error: () => this.error = 'Failed to create report'
      });
  }

  markDelivered(reportId: number): void {
    this.reportService.markDelivered(reportId).subscribe({
      next: (updatedReport: Report) => {
        const index = this.dataSource.data.findIndex((r) => r.id === reportId);
        if (index !== -1) {
          this.dataSource.data[index].status = updatedReport.status;
        }
      }
    });
  }
}
