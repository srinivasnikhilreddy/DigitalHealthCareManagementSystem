export interface Doctor {
  id: number;
  name: string;
  specialty: string;
  licenseNumber: string;
  contact: string;
  email: string;
  passwordHash?: string;
}
