import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Report } from '../models/report.model';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = 'http://localhost:9086/reports';

  constructor(private http: HttpClient) {}

  getReportsByAppointment(appointmentId: number): Observable<Report[]> {
    return this.http.get<Report[]>(`${this.apiUrl}/${appointmentId}`);
  }

  createReport(appointmentId: number, type: string, reportData?: string): Observable<Report> {
    return this.http.post<Report>(
      `${this.apiUrl}/createReport/${appointmentId}?type=${type}&reportData=${reportData ?? ''}`,
      {}
    );
  }

  markDelivered(reportId: number): Observable<Report> {
    return this.http.post<Report>(`${this.apiUrl}/deliver/${reportId}`, {});
  }

}
