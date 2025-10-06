import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { Patient } from '../../../core/models/patient.model';

@Component({
  selector: 'app-patient-detail',
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatButtonModule],
  templateUrl: './patient-detail.component.html',
  styleUrl: './patient-detail.component.css'
})
export class PatientDetailComponent
{
    updateForm: FormGroup;

    constructor(
      private dialogRef: MatDialogRef<PatientDetailComponent>,
      @Inject(MAT_DIALOG_DATA) public patient: Patient)
    {
        this.updateForm = new FormGroup({
            id: new FormControl(patient.id),
            username: new FormControl(patient.username),
            firstName: new FormControl(patient.firstName),
            lastName: new FormControl(patient.lastName),
            email: new FormControl(patient.email),
            phone: new FormControl(patient.phone),
            gender: new FormControl(patient.gender),
            dateOfBirth: new FormControl(patient.dateOfBirth),
            bloodGroup: new FormControl(patient.bloodGroup),
            medicalHistory: new FormControl(patient.medicalHistory)
        });
    }

    save()
    {
        if(this.updateForm.valid){
            this.dialogRef.close(this.updateForm.value);
        }
    }

    close()
    {
        this.dialogRef.close();
    }
}
