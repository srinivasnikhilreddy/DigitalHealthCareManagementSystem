export type AppointmentStatus = 'SCHEDULED' | 'COMPLETED' | 'CANCELLED' | 'PENDING_PAYMENT';

export type AppointmentSlot = | 'SLOT_9_10' | 'SLOT_10_11' | 'SLOT_11_12' | 'SLOT_4_5' | 'SLOT_5_6';

export interface Appointment
{
    id?: number;
    patientId: number;
    doctorId: number;
    patientName: string;
    patientAge: string;
    patientDob: string;
    appointmentDate: string; // date of appointment
    requestDate: string;
    type: string;
    status: AppointmentStatus;
    slot: AppointmentSlot;
}
