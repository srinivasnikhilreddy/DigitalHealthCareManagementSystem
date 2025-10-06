import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { PatientService } from '../../../core/services/patient.service';
import { Patient } from '../../../core/models/patient.model';
import { MatTableDataSource } from '@angular/material/table';
import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { PatientDetailComponent } from '../patient-detail/patient-detail.component';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { MatIconModule } from '@angular/material/icon';

@Component({
    selector: 'app-patient-list',
    standalone: true,
    imports: [
      CommonModule,
      FormsModule,
      MatTableModule,
      MatPaginatorModule,
      MatSortModule,
      MatFormFieldModule,
      MatInputModule,
      MatButtonModule,
      MatProgressSpinnerModule,
      MatDialogModule,
      ReactiveFormsModule,
      MatIconModule
    ],
    templateUrl: './patient-list.component.html',
    styleUrls: ['./patient-list.component.css']
})
export class PatientListComponent implements OnInit
{
    displayedColumns: string[] = [
      'id', 'username', 'fullName', 'email', 'phone', 'gender', 'dob', 'bloodGroup', 'medicalHistory', 'status', 'actions'
    ];
    dataSource = new MatTableDataSource<Patient>([]);
    loading = true;
    error?: string;
    searchId: string = '';

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;
    ngAfterViewInit(): void {
        //this.dataSource.paginator = this.paginator;
        //this.dataSource.sort = this.sort;
    }

    updateForm: FormGroup = new FormGroup({
        id: new FormControl(''),
        username: new FormControl(''),
        firstName: new FormControl(''),
        lastName: new FormControl(''),
        email: new FormControl(''),
        phone: new FormControl(''),
        gender: new FormControl(''),
        dateOfBirth: new FormControl(''),
        bloodGroup: new FormControl(''),
        medicalHistory: new FormControl(''),
        status: new FormControl('')
    });

    constructor(private dialog: MatDialog, private patientService: PatientService, private sanitizer: DomSanitizer) {}

    allPatients: Patient[] = []; //store all patients locally

    ngOnInit(): void
    {
        this.fetchPatients();
    }

    fetchPatients(): void {
        this.loading = true;
        this.patientService.getPatients().subscribe({
            next: (data) => {
                this.allPatients = data;
                this.dataSource.data = data;
                if(this.paginator && this.sort){
                      this.dataSource.paginator = this.paginator;
                      this.dataSource.sort = this.sort;
                }
                this.loading = false;
            },
            error: () => {
                this.error = 'Failed to load patients';
                this.loading = false;
            }
        });
    }

    highlightMatch(text: string): SafeHtml
    {
        if(!this.searchId) return text;
        const searchText = this.searchId.trim().toLowerCase();
        const regex = new RegExp(`(${searchText})`, 'gi'); //case-insensitive
        const highlighted = text.replace(regex, `<span style="color:red; background-color: yellow;">$1</span>`);
        return this.sanitizer.bypassSecurityTrustHtml(highlighted);
    }

    onSearchInputChange(): void
    {
        const searchText = this.searchId.trim().toLowerCase();
        if(!searchText){
            this.dataSource.data = [...this.allPatients]; // reset full list
            this.error = undefined;
            return;
        }
        //filter locally for matching username or first/last name
        this.dataSource.data = this.allPatients.filter(p =>
            p.username.toLowerCase().includes(searchText) ||
            p.firstName.toLowerCase().includes(searchText) ||
            p.lastName.toLowerCase().includes(searchText)
        );
        if(this.paginator) this.dataSource.paginator = this.paginator;
        //optional: show "no match" if nothing found
        this.error = this.dataSource.data.length === 0 ? 'No matching patients' : undefined;
    }

    deletePatient(id: number): void
    {
        if(!confirm('Are you sure you want to delete this patient?')) return;
        this.patientService.deletePatient(id).subscribe({
            next: () => {
                this.dataSource.data = this.dataSource.data.filter((p: Patient) => p.id !== id);
            },
            error: () => {
                this.error = 'Failed to delete patient';
            }
        });
    }

    /*populateUpdateForm(patient: Patient) {
      console.log(patient);
      this.updateForm.setValue({ ...patient });
    }*/
    populateUpdateForm(patient: Patient)
    {
        const dialogRef = this.dialog.open(PatientDetailComponent, {
            width: '600px',
            data: patient
        });
        dialogRef.afterClosed().subscribe((updatedPatient: Patient) => {
            if(updatedPatient){
                this.patientService.updatePatient(updatedPatient).subscribe({
                    next: (p) => {
                        const index = this.dataSource.data.findIndex(d => d.id === p.id);
                        if(index > -1) this.dataSource.data[index] = p;
                        this.dataSource._updateChangeSubscription();
                    },
                    error: () => alert('Failed to update patient')
                });
            }
        });
    }

    updatePatient(): void
    {
        const updatedPatient: Patient = this.updateForm.value;
        this.patientService.updatePatient(updatedPatient).subscribe({
            next: (p) => {
                const index = this.dataSource.data.findIndex((d: Patient) => d.id === p.id);
                if(index > -1) this.dataSource.data[index] = p;
                this.dataSource._updateChangeSubscription(); //refresh table
                alert('Patient updated successfully!');
                this.updateForm.reset();
            },
            error: () => alert('Failed to update patient')
        });
    }
}
