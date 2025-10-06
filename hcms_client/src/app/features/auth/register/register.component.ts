import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  reactiveForm = new FormGroup({
    username: new FormControl('', [Validators.required, Validators.minLength(2)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(5)]),
    roles: new FormControl(['ROLE_PATIENT'], [Validators.required]),
    firstName: new FormControl(''),
    lastName: new FormControl(''),
    gender: new FormControl('OTHER'),
    dateOfBirth: new FormControl(''),
    phone: new FormControl(''),
    address: new FormControl(''),
    specialization: new FormControl(''),
    licenseNumber: new FormControl(''),
    experienceYears: new FormControl(0),
    bloodGroup: new FormControl(''),
    medicalHistory: new FormControl('')
  });

  selectedFile: File | undefined = undefined;
  submitting = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  authService = inject(AuthService);
  router = inject(Router);

  onFileSelected(event: any)
  {
    if(event.target.files && event.target.files.length > 0){
      this.selectedFile = event.target.files[0];
    }
  }

  register() {
    this.errorMessage = null;
    this.successMessage = null;

    if(this.reactiveForm.invalid){
      this.reactiveForm.markAllAsTouched();
      return;
    }

    const formValue = this.reactiveForm.value;

    const user = {
      username: formValue.username ?? '',
      email: formValue.email ?? '',
      password: formValue.password ?? '',
      roles: formValue.roles ?? ['ROLE_PATIENT'],
      firstName: formValue.firstName ?? '',
      lastName: formValue.lastName ?? '',
      gender: formValue.gender ?? 'OTHER',
      dateOfBirth: formValue.dateOfBirth ?? '',
      phone: formValue.phone ?? '',
      address: formValue.address ?? '',
      specialization: formValue.specialization ?? '',
      licenseNumber: formValue.licenseNumber ?? '',
      experienceYears: formValue.experienceYears ?? 0,
      bloodGroup: formValue.bloodGroup ?? '',
      medicalHistory: formValue.medicalHistory ?? ''
    };

    this.submitting = true;

    this.authService.register(user, this.selectedFile ?? undefined).subscribe({
      next: success => {
        if (success) {
          this.successMessage = 'Registration successful!';
          this.router.navigate(['/login']);
        } else {
          this.errorMessage = 'Registration failed';
        }
      },
      error: err => {
        this.errorMessage = 'Registration failed';
      }
    });
  }
}
