import { Component, OnInit, inject } from '@angular/core';
import { NotificationService } from '../../../../core/services/notification.service';
import { Notification } from '../../../../core/models/notification.model';
import { UserProfile } from '../../../../core/models/user-profile.model';
import { UserService } from '../../../../core/services/user.service';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { Appointment } from '../../../../core/models/appointment.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notification-list',
  imports: [CommonModule],
  templateUrl: './notification-list.component.html',
  styleUrls: ['./notification-list.component.css']
})
export class NotificationListComponent implements OnInit {

  notifications: Notification[] = [];
  userId: number = 1; // replace with logged-in user ID
  error?: string;

  constructor(private notificationService: NotificationService, private appointmentService: AppointmentService) {}
  private userService = inject(UserService);

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: (data: UserProfile) => {
        if(data.doctorId != undefined){
          this.loadDoctorAppointments(data.doctorId);
        }
      },
      error: () => (this.error = 'Failed to load user profile')
    });
  }

  loadDoctorAppointments(doctorId: number): void {
    this.appointmentService.getByDoctor(doctorId).subscribe({
      next: (appointments: Appointment[]) => {
        if (appointments.length > 0) {
          if (appointments[0].id !== undefined) {
            this.userId = appointments[0].id;
          }
          console.log('First appointment patientId:', this.userId);
          this.loadNotifications();
        } else {
          this.error = 'No appointments found.';
        }
      },
      error: () => {
        this.error = 'Error fetching appointments.';
      }
    });
  }


  loadNotifications(): void {
    this.notificationService.getNotificationsByUser(this.userId).subscribe({
      next: (res) => {
        console.log(res);
        this.notifications = res
      },
      error: (err) => console.error('Error fetching notifications', err)
    });
  }

  markSent(notification: Notification): void {
    this.notificationService.markAsSent(notification.id).subscribe({
      next: () => notification.sent = true,
      error: (err) => console.error('Error marking notification as sent', err)
    });
  }
}
