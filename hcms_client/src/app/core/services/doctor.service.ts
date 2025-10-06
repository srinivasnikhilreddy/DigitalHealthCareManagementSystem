import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfile } from '../models/user-profile.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DoctorService {

  private baseUrl = `${environment.apiBaseUrl}/doctors`;

  constructor(private http: HttpClient) { }

  // GET all doctors
  getAllDoctors(): Observable<UserProfile[]> {
    return this.http.get<UserProfile[]>(`${this.baseUrl}/getAll`);
  }

  // GET doctor by username
  getDoctor(username: string): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.baseUrl}/get/${username}`);
  }

}
