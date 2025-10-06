import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserProfile } from '../models/user-profile.model';
import { environment } from '../../../environments/environment'; // make sure you have this

@Injectable({
  providedIn: 'root'
})
export class UserService
{
    private apiBaseUrl = `${environment.apiBaseUrl}/auth`;

    constructor(private http: HttpClient) {}

    getCurrentUser(): Observable<UserProfile>
    {
        const username = localStorage.getItem('username')!;
        return this.http.get<UserProfile>(`${this.apiBaseUrl}/profile?username=${username}`);
    }

    updateUser(user: UserProfile): Observable<UserProfile>
    {
        return this.http.put<UserProfile>(`${this.apiBaseUrl}/profile`, user);
    }
}
