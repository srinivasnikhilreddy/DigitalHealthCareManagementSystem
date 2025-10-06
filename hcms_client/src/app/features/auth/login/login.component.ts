import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-login',
    imports: [CommonModule, RouterModule, ReactiveFormsModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent
{
    reactiveForm = new FormGroup({
        username: new FormControl('', [Validators.required, Validators.minLength(2)]),
        password: new FormControl('', [Validators.required, Validators.minLength(5)])
    });

    submitting = false;
    errorMessage: string | null = null;

    authService = inject(AuthService);
    router = inject(Router);

    login()
    {
        this.errorMessage = null;
        if(this.reactiveForm.invalid){
            this.reactiveForm.markAllAsTouched();
            return;
        }

        const username: string = this.reactiveForm.value.username ?? '';
        const password: string = this.reactiveForm.value.password ?? '';

        this.submitting = true;
        this.authService.login({ username, password }).subscribe({
            next: success => {
              this.submitting = false;
              if(success){
                  localStorage.setItem('username', username);
                  const role = this.authService.getUserRole();
                  console.log(role);
                  if(role === 'ADMIN'){
                      this.router.navigate(['/settings']);
                  }else if(role === 'DOCTOR'){
                      this.router.navigate(['/doctor-dashboard']);
                  }else if(role === 'PATIENT'){
                      this.router.navigate(['/patient-dashboard']);
                  }else{
                      this.router.navigate(['/login']); // fallback
                  }
                }else{
                    this.errorMessage = 'Invalid credentials';
                }
            },
            error: () => {
                this.submitting = false;
                this.errorMessage = 'Login failed';
            }
        });
    }
}

