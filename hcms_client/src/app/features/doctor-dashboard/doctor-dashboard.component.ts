import { Component, OnInit, inject } from '@angular/core';
import { Router, NavigationEnd, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { AppointmentService } from '../../core/services/appointment.service';
import { PatientService } from '../../core/services/patient.service';
import { ReportService } from '../../core/services/report.service';
import { ReportStatus } from '../../core/models/report.model';
import { UserProfile } from '../../core/models/user-profile.model';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-doctor-dashboard',
  imports: [CommonModule, RouterModule],
  templateUrl: './doctor-dashboard.component.html',
  styleUrls: ['./doctor-dashboard.component.css']
})
export class DoctorDashboardComponent implements OnInit {
  showTitle = false;

  // Dashboard stats
  totalPatients: number = 0;
  todaysAppointments: number = 0;
  reportsPending: number = 0;
  notifications: number = 0; // placeholder

  // Replace with logged-in doctorId
  private doctorId: number = 1;
  private userService = inject(UserService);

  constructor(
    private router: Router,
    private appointmentService: AppointmentService,
    private patientService: PatientService,
    private reportService: ReportService
  ) {
    // Show title only on exact dashboard route
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.showTitle = event.urlAfterRedirects === '/doctor-dashboard';
      });
  }

  profilePictureUrl: string = "";
  username: string = "";
  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
        next: (data: UserProfile) => {
            this.profilePictureUrl = data.profilePictureUrl!;
            this.username = data.username;
        },
        error: () => ('Failed to load user profile')
    });
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    // 1. Get total patients
    this.patientService.getPatients().subscribe({
      next: (patients) => this.totalPatients = patients.length,
      error: (err) => console.error('Error fetching patients', err)
    });

    // 2. Get doctor’s appointments
    this.appointmentService.getByDoctor(this.doctorId).subscribe({
      next: (appointments) => {
        const today = new Date().toDateString();
        this.todaysAppointments = appointments.filter(a =>
          new Date(a.appointmentDate).toDateString() === today
        ).length;
      },
      error: (err) => console.error('Error fetching appointments', err)
    });

    // 3. Get pending reports
    // (depends on how your backend marks status, assuming "PENDING")
    this.reportService.getReportsByAppointment(this.doctorId).subscribe({
      next: (reports) => {
        this.reportsPending = reports.filter(r => r.status == <ReportStatus>'PENDING').length;
      },
      error: (err) => console.error('Error fetching reports', err)
    });

    // 4. Notifications (TODO: hook notification API later)
    this.notifications = 3;
  }
}
