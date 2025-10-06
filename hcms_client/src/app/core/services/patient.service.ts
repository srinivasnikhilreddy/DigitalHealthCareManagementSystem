import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Patient } from '../models/patient.model'; // create this model

@Injectable({
  providedIn: 'root'
})
export class PatientService
{
    private apiUrl = `${environment.apiBaseUrl}/patients`;

    constructor(private http: HttpClient) {}

    // Get all patients
    getPatients(): Observable<Patient[]>
    {
      return this.http.get<Patient[]>(`${this.apiUrl}/getAll`);
    }

    // Get patient by id
    getPatientByUsername(username: string): Observable<Patient>
    {
      return this.http.get<Patient>(`${this.apiUrl}/get/${username}`);
    }

    // Update patient
    updatePatient(patient: Patient): Observable<Patient>
    {
      return this.http.put<Patient>(`${this.apiUrl}/update/${patient.id}`, patient);
    }

    // Delete patient
    deletePatient(id: number): Observable<void>
    {
      return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
    }
}
