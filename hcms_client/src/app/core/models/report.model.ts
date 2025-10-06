export type ReportType = 'PRESCRIPTION' | 'LAB';
export type ReportStatus = 'GENERATED' | 'DELIVERED' | 'FAILED';

export interface Report {
  id: number;
  appointmentId: number;
  type: ReportType;
  status: ReportStatus;
  reportData?: string;
  patientName?: string;
}
