import { Component, OnInit, inject } from '@angular/core';
import { Router, NavigationEnd, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { DoctorService } from '../../core/services/doctor.service';
import { UserProfile } from '../../core/models/user-profile.model';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-patient-dashboard',
  imports: [CommonModule, RouterModule],
  templateUrl: './patient-dashboard.component.html',
  styleUrls: ['./patient-dashboard.component.css']
})
export class PatientDashboardComponent implements OnInit
{
  showTitle = false;
  loading = false;
  error?: string;
  doctorService = inject(DoctorService);
  private userService = inject(UserService);

  doctors: UserProfile[] = [];

  constructor(private router: Router) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        this.showTitle = event.urlAfterRedirects === '/patient-dashboard';
      });
  }
  profilePictureUrl: string = "";
  username: string = "";
  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
        next: (data: UserProfile) => {
            this.profilePictureUrl = data.profilePictureUrl!;
            localStorage.setItem('patientId', data.patientId!.toString());
            this.username = data.username;
        },
        error: () => ('Failed to load user profile')
    });
    this.fetchDoctors();
  }

  fetchDoctors(): void {
    this.loading = true;
    this.doctorService.getAllDoctors().subscribe({
      next: (data) => {
        this.doctors = data;
        this.loading = false;
      },
      error: (err) => {
        console.error(err);
        this.error = 'Failed to load doctors';
        this.loading = false;
      }
    });
  }
}
