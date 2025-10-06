package com.iss.controllers;

import com.iss.models.Payment;
import com.iss.services.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController
{
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestParam Long appointmentId, @RequestParam Double amount)
    {
        try{
            Payment payment = paymentService.processPayment(appointmentId, amount);
            return ResponseEntity.ok(payment);
        }catch(Exception e){
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/success")
    public ResponseEntity<Payment> markSuccess(@RequestParam Long paymentId) {
        Payment payment = paymentService.markPaymentSuccess(paymentId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/failed")
    public ResponseEntity<Payment> markFailure(@RequestParam Long paymentId, @RequestParam String reason) {
        Payment payment = paymentService.markPaymentFailure(paymentId, reason);
        return ResponseEntity.ok(payment);
    }

    //http://localhost:9085/payments/refund/4
    @PostMapping("/refund/{paymentId}")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long paymentId)
    {
        Payment payment = paymentService.refundPayment(paymentId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPayments() {
        try {
            return ResponseEntity.ok(paymentService.getAllPayments());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
