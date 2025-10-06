import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Payment } from '../models/payment.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = 'http://localhost:9085/payments'; // backend URL

  constructor(private http: HttpClient) {}

  processPayment(appointmentId: number, amount: number): Observable<Payment> {
    const params = new HttpParams()
      .set('appointmentId', appointmentId)
      .set('amount', amount.toString());

    return this.http.post<Payment>(`${this.apiUrl}/initiate`, null, { params });
  }

  markPaymentSuccess(paymentId: number): Observable<Payment> {
    const params = new HttpParams()
      .set('paymentId', paymentId)
      .set('razorpayPaymentId', 'local-simulated'); // since no Razorpay now
    return this.http.post<Payment>(`${this.apiUrl}/success`, null, { params });
  }

  markPaymentFailure(paymentId: number, reason: string): Observable<Payment> {
    const params = new HttpParams()
      .set('paymentId', paymentId)
      .set('reason', reason);
    return this.http.post<Payment>(`${this.apiUrl}/failed`, null, { params });
  }

  refundPayment(paymentId: number): Observable<Payment> {
    return this.http.post<Payment>(`${this.apiUrl}/refund/${paymentId}`, {});
  }

  getAllPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.apiUrl}/getAll`);
  }

}
