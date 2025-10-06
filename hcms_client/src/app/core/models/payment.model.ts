export type PaymentStatus = 'PENDING' | 'SUCCESS' | 'FAILED' | 'REFUNDED';

export interface Payment {
  id: number;
  appointmentId: number;
  amount: number;
  status: PaymentStatus;
}
