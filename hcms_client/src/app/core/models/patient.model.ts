export interface Patient {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  gender: string;
  dateOfBirth: string;
  address?: string;
  bloodGroup?: string;
  medicalHistory?: string;
  status: 'Active' | 'Inactive';
}
