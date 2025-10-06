import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment } from '../models/appointment.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService
{
    private apiUrl = 'http://localhost:9081/appointments';

    constructor(private http: HttpClient) {}

    schedule(appointment: Appointment): Observable<Appointment>
    {
        return this.http.post<Appointment>(`${this.apiUrl}/schedule`, appointment);
    }

    getByPatient(patientId: number): Observable<Appointment[]>
    {
        return this.http.get<Appointment[]>(`${this.apiUrl}/patient/${patientId}`);
    }

    getByDoctor(doctorId: number): Observable<Appointment[]>
    {
        //console.log(doctorId);
        return this.http.get<Appointment[]>(`${this.apiUrl}/doctor/${doctorId}`);
    }

    cancel(appointmentId: number): Observable<void>
    {
        return this.http.delete<void>(`${this.apiUrl}/${appointmentId}/cancel`);
    }

    updateStatus(appointmentId: number, status: string): Observable<Appointment>
    {
        return this.http.put<Appointment>(`${this.apiUrl}/${appointmentId}/status?status=${status}`, {});
    }

}
