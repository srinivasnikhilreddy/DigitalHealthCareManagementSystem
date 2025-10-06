import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';

import { DoctorDashboardComponent } from './features/doctor-dashboard/doctor-dashboard.component';
import { PatientDashboardComponent } from './features/patient-dashboard/patient-dashboard.component';

// Patient views doctors
import { DoctorListComponent } from './features/patient-dashboard/doctor-list/doctor-list.component';
import { DoctorDetailComponent } from './features/patient-dashboard/doctor-detail/doctor-detail.component';

// Doctor views patients
import { PatientListComponent } from './features/doctor-dashboard/patient-list/patient-list.component';
import { PatientDetailComponent } from './features/doctor-dashboard/patient-detail/patient-detail.component';

import { SettingsComponent } from './features/admin/settings/settings.component';
import { ProfileComponent } from './features/auth/profile/profile.component'
import { NotificationListComponent } from './features/doctor-dashboard/notification/notification-list/notification-list.component';
import { AppointmentListComponent } from './features/doctor-dashboard/appointment/appointment-list/appointment-list.component';
import { ReportListComponent } from './features/doctor-dashboard/report/report-list/report-list.component';
import { ReportDetailComponent } from './features/patient-dashboard/report/report-detail/report-detail.component';
import { PaymentListComponent } from './features/patient-dashboard/payment/payment-list/payment-list.component';
import { PatientNotificationsComponent } from './features/patient-dashboard/notification/patient-notifications/patient-notifications.component';

export const routes: Routes = [
    /*path: '' -> This is the default empty path, i.e., when the app first loads at the root URL (http://localhost:4200/).
      redirectTo: 'login' -> Automatically redirects the user to the /login page.
      pathMatch: 'full' -> This tells Angular to match the entire URL ('') for this route. */
    { path: '', redirectTo: 'login', pathMatch: 'full' },

    /*path: 'login' -> This is the URL path: /login.
      component: LoginComponent -> Angular will render the LoginComponent in the <router-outlet> when the user navigates to /login.*/
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },

    { path: 'doctor-dashboard', component: DoctorDashboardComponent,
        children: [
            { path: 'patient-list', component: PatientListComponent },
            //{ path: '', redirectTo: 'patient-list', pathMatch: 'full' }, //default page
            { path: 'patient-detail/:id', component: PatientDetailComponent },
            { path: 'notification/notification-list', component: NotificationListComponent },
            { path: 'appointment/appointment-list', component: AppointmentListComponent },
            { path: 'report/report-list', component: ReportListComponent }
        ]
    },
    { path: 'patient-dashboard', component: PatientDashboardComponent,
        children: [
            { path: 'doctor-list', component: DoctorListComponent },
            { path: 'doctor-detail/:id', component: DoctorDetailComponent },
            { path: 'report/report-detail', component: ReportDetailComponent },
            { path: 'payment/payment-list', component: PaymentListComponent},
            { path: 'notification/patient-notifications', component: PatientNotificationsComponent}
        ]
    },

    { path: 'settings', component: SettingsComponent },

    { path: 'profile', component: ProfileComponent },

    /*path: '**' -> This is the wildcard route, meaning any URL that doesn’t match above routes.
      redirectTo: 'login' -> If a user enters a URL that doesn’t exist, Angular redirects them to /login.*/
    { path: '**', redirectTo: 'login' }
];
