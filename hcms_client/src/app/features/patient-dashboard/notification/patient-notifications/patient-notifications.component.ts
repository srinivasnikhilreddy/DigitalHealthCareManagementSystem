import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { NotificationService } from '../../../../core/services/notification.service';
import { Notification } from '../../../../core/models/notification.model';
import { UserService } from '../../../../core/services/user.service';
import { AppointmentService } from '../../../../core/services/appointment.service';

@Component({
  selector: 'app-patient-notifications',
  imports: [CommonModule],
  templateUrl: './patient-notifications.component.html',
  styleUrls: ['./patient-notifications.component.css'] // corrected
})
export class PatientNotificationsComponent implements OnInit {

  notifications: Notification[] = [];
  loading = true;
  error: string | null = null;

  private notificationService = inject(NotificationService);
  private appointmentService = inject(AppointmentService);
  private userService = inject(UserService);

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    const patientId = Number(localStorage.getItem('patientId'));
    if (!patientId) {
      this.error = 'Invalid patient ID';
      this.loading = false;
      return;
    }

    this.notificationService.getNotificationsByUser(patientId).subscribe({
      next: (res) => {
        this.notifications = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching notifications', err);
        this.error = 'Failed to load notifications';
        this.loading = false;
      }
    });
  }

  markSent(notification: Notification): void {
    if (!notification.id) return;
    this.notificationService.markAsSent(notification.id).subscribe({
      next: () => notification.sent = true,
      error: (err) => console.error('Error marking notification as sent', err)
    });
  }
}
