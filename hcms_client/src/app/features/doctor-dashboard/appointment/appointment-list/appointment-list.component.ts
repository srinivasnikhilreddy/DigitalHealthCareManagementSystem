import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule, MatSort } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { AppointmentService } from '../../../../core/services/appointment.service';
import { Appointment } from '../../../../core/models/appointment.model';
import { UserService } from '../../../../core/services/user.service';
import { UserProfile } from '../../../../core/models/user-profile.model';

@Component({
  selector: 'app-appointment-list',
  templateUrl: './appointment-list.component.html',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ]
})
export class AppointmentListComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: string[] = [
    'id', 'patientName', 'patientAge', 'type',
    'appointmentDate', 'slot', 'status', 'action'
  ];
  dataSource = new MatTableDataSource<Appointment>([]);
  loading = false;
  error?: string;

  constructor(private appointmentService: AppointmentService) {}
  private userService = inject(UserService);

  doctorId!: number;
  ngOnInit(): void {
        this.userService.getCurrentUser().subscribe({
        next: (data: UserProfile) => {
          console.log(data.doctorId);
          if(data.doctorId != undefined)
            this.loadDoctorAppointments(data.doctorId);
        },
        error: () => (this.error = 'Failed to load user profile')
      });
    }

  loadDoctorAppointments(doctorId: number): void {
    this.loading = true;
    this.error = undefined;

    this.appointmentService.getByDoctor(doctorId).subscribe({
      next: (data) => {
        const currentYear = new Date().getFullYear();
        const appointmentsWithAge = data.map((a: Appointment) => ({
          ...a,
          patientAge: a.patientDob ? (currentYear - new Date(a.patientDob).getFullYear()).toString() : ''
        }));
        this.dataSource.data = appointmentsWithAge;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.loading = false;
      },
      error: () => {
        this.error = 'No appointments found.';
        this.loading = false;
      }
    });
  }

  getSlotDisplay(slot: string): string {
    const slotMap: Record<string, string> = {
      SLOT_9_10: '9:00 AM - 10:00 AM',
      SLOT_10_11: '10:00 AM - 11:00 AM',
      SLOT_11_12: '11:00 AM - 12:00 PM',
      SLOT_4_5: '4:00 PM - 5:00 PM',
      SLOT_5_6: '5:00 PM - 6:00 PM',
    };
    return slotMap[slot] || slot;
  }

  cancelAppointment(id: number): void {
    this.appointmentService.cancel(id).subscribe({
      next: () => {
        this.dataSource.data = this.dataSource.data.filter((a: Appointment) => a.id !== id);
      }
    });
  }

  updateAppointmentStatus(appointmentId: number, status: string): void {
    this.appointmentService.updateStatus(appointmentId, status).subscribe({
      next: (updatedAppt) => {
        const index = this.dataSource.data.findIndex((a: Appointment) => a.id === appointmentId);
        if (index !== -1) {
          this.dataSource.data[index].status = updatedAppt.status;
        }
      },
      error: () => {
        alert('Failed to update status.');
      }
    });
  }
}
