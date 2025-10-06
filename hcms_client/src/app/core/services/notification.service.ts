import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notification } from '../models/notification.model';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private apiUrl = 'http://localhost:9089/notifications'; // adjust backend URL

  constructor(private http: HttpClient) { }

  // Get all notifications for a user
  getNotificationsByUser(userId: number): Observable<Notification[]>
  {
    return this.http.get<Notification[]>(`${this.apiUrl}/user/${userId}`);
  }

  // Mark notification as sent/read
  markAsSent(notificationId: number): Observable<void>
  {
    return this.http.put<void>(`${this.apiUrl}/${notificationId}/sent`, {});
  }
}
