import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from '../../../core/services/user.service';
import { UserProfile } from '../../../core/models/user-profile.model';
import { AuthService } from '../../../core/services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-profile',
    imports: [CommonModule, ReactiveFormsModule, RouterModule],
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit
{
    private userService = inject(UserService);
    private authService = inject(AuthService);

    profileForm: FormGroup = new FormGroup({
        firstName: new FormControl(''),
        lastName: new FormControl(''),
        email: new FormControl(''),
        phone: new FormControl(''),
        address: new FormControl(''),
        gender: new FormControl('OTHER'),
        dateOfBirth: new FormControl(''),
        specialization: new FormControl(''),
        licenseNumber: new FormControl(''),
        experienceYears: new FormControl(0),
        bloodGroup: new FormControl(''),
        medicalHistory: new FormControl(''),
        profilePictureUrl: new FormControl('')
    });

    initForm(user: UserProfile): void
    {
        this.profileForm.patchValue(user);
    }

    user?: UserProfile;
    submitting = false;
    errorMessage?: string;
    successMessage?: string;

    ngOnInit(): void
    {
        this.loadUser();
    }

    loadUser(): void
    {
        this.userService.getCurrentUser().subscribe({
            next: (data: UserProfile) => {
                this.user = data;
                this.initForm(data);
            },
            error: () => (this.errorMessage = 'Failed to load user profile')
        });
    }

    saveProfile(): void
    {
        if(!this.profileForm.valid) return;

        this.submitting = true;
        const updatedUser: UserProfile = { ...this.user, ...this.profileForm.value };

        this.userService.updateUser(updatedUser).subscribe({
            next: () => {
                this.submitting = false;
                this.successMessage = 'Profile updated successfully!';
            },
            error: () => {
                this.submitting = false;
                this.errorMessage = 'Failed to update profile.';
            }
        });
    }

    get isDoctor(): boolean
    {
        //console.log(this.user?.roles);
        return this.user?.roles?.includes('ROLE_DOCTOR') ?? false;
    }

    get isPatient(): boolean
    {
        //console.log(this.user?.roles);
        return this.user?.roles?.includes('ROLE_PATIENT') ?? false;
    }

    logout(): void
    {
        this.authService.logout();
    }
}
