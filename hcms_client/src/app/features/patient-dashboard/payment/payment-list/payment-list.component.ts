import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PaymentService } from '../../../../core/services/payment.service';
import { Payment } from '../../../../core/models/payment.model';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-payment-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule],
  templateUrl: './payment-list.component.html',
  styleUrls: ['./payment-list.component.css']
})
export class PaymentListComponent implements OnInit {
  payments: Payment[] = [];
  loading = true;
  error: string | null = null;

  displayedColumns: string[] = ['id', 'amount', 'status', 'actions'];

  paymentService = inject(PaymentService);

  ngOnInit(): void {
    this.loadPayments();
  }

  loadPayments() {
    this.paymentService.getAllPayments().subscribe({
      next: (data) => {
        console.log(data);
        this.payments = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load payments';
        this.loading = false;
      }
    });
  }

  refundPayment(paymentId: number) {
    this.paymentService.refundPayment(paymentId).subscribe({
      next: (data) => {
        alert('Payment refunded successfully');
        this.loadPayments(); // refresh table
      },
      error: () => alert('Refund failed')
    });
  }
}
