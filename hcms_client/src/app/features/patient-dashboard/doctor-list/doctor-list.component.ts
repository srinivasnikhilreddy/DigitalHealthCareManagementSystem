import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Appointment, AppointmentStatus, AppointmentSlot } from '../../../core/models/appointment.model';
import { AppointmentService } from '../../../core/services/appointment.service';
import { UserService } from '../../../core/services/user.service';
import { UserProfile } from '../../../core/models/user-profile.model';
import { DoctorService } from '../../../core/services/doctor.service';
import { AuthService } from '../../../core/services/auth.service';
import { PaymentService } from '../../../core/services/payment.service';
import { Payment } from '../../../core/models/payment.model';

@Component({
  selector: 'app-doctor-list',
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    FormsModule
  ],
  templateUrl: './doctor-list.component.html',
  styleUrls: ['./doctor-list.component.css']
})
export class DoctorListComponent implements OnInit
{
    displayedColumns: string[] = [
      'id', 'fullName', 'email', 'phone', 'gender', 'specialization', 'experienceYears', 'actions'
    ];

    dataSource: MatTableDataSource<UserProfile> = new MatTableDataSource();
    loading = false;
    error?: string;
    searchName: string = '';

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    private appointmentService = inject(AppointmentService);
    private doctorService = inject(DoctorService);
    private userService = inject(UserService);
    private authService = inject(AuthService);
    private paymentService = inject(PaymentService);

    ngOnInit(): void
    {
        this.fetchDoctors();
    }

    fetchDoctors(): void
    {
        this.loading = true;
        this.doctorService.getAllDoctors().subscribe({
            next: (data: UserProfile[]) => {
                this.dataSource.data = data;
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
                this.loading = false;
            },
            error: (err: any) => {
                console.error(err);
                this.error = 'Failed to load doctors';
                this.loading = false;
            }
        });
    }

    onSearchInputChange(): void
    {
        this.dataSource.filter = this.searchName.trim().toLowerCase();
    }

    highlightMatch(text: string): string
    {
        if(!this.searchName) return text;
        const regex = new RegExp(`(${this.searchName})`, 'gi');
        return text.replace(regex, `<mark>$1</mark>`);
    }

    bookAppointment(doctor: UserProfile): void {
      this.userService.getCurrentUser().subscribe({
        next: (user: UserProfile) => {
          if (!user.patientId) {
            alert("Cannot book appointment: Patient ID missing.");
            return;
          }

          const appointment: Appointment = {
            patientId: user.patientId,
            patientName: user.username,
            patientDob: new Date(user.dateOfBirth!).toISOString(),
            patientAge: this.calculateAge(user.dateOfBirth!),
            doctorId: doctor.id!,
            requestDate: new Date().toISOString(),
            appointmentDate: new Date().toISOString(),
            type: "Consultation",
            slot: "SLOT_10_11" as any,
            status: "PENDING_PAYMENT" as AppointmentStatus
          };

          this.appointmentService.schedule(appointment).subscribe({
            next: created => {
              this.paymentService.processPayment(created.id!, 500).subscribe({
                next: payment => {
                  // simulate success
                  this.paymentService.markPaymentSuccess(payment.id).subscribe({
                    next: success => alert('Appointment booked & payment successful!'),
                    error: err => alert('Failed to mark payment success')
                  });
                },
                error: err => alert('Payment processing failed')
              });
            },
            error: err => alert('Failed to book appointment')
          });
        }
      });
    }

    private calculateAge(dob: string): string
    {
        const birthDate = new Date(dob);
        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        if(monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())){
            age--;
        }
        return age.toString();
    }
}
