export class UserProfile
{
    id?: number;
    patientId?: number;
    username!: string;
    email!: string;
    roles: string[] = [];

    firstName?: string;
    lastName?: string;
    gender?: 'MALE' | 'FEMALE' | 'OTHER';
    dateOfBirth?: string; // ISO string (YYYY-MM-DD from backend)
    phone?: string;
    address?: string;
    profilePictureUrl?: string;

    // Doctor-specific fields
    doctorId?: number;
    specialization?: string;
    licenseNumber?: string;
    experienceYears?: number;

    // Patient-specific fields
    bloodGroup?: string;
    medicalHistory?: string; // keep it as a text blob for now; easier for backend/DB

    constructor(init?: Partial<UserProfile>) {
      Object.assign(this, init);
    }

    getFullName(): string {
      return `${this.firstName ?? ''} ${this.lastName ?? ''}`.trim();
    }

    isDoctor(): boolean {
      return this.roles.includes('DOCTOR');
    }

    isPatient(): boolean {
      return this.roles.includes('PATIENT');
    }

    isAdmin(): boolean {
      return this.roles.includes('ADMIN');
    }
}
