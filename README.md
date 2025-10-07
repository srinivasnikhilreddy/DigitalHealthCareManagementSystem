# 🏥 Digital HealthCare Management System

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Frontend](https://img.shields.io/badge/Frontend-Angular-blue)](#)
[![Backend](https://img.shields.io/badge/Backend-Spring%20Boot-orange)](#)
[![Database](https://img.shields.io/badge/Database-MSSQL-lightgrey)](#)
[![Microservices](https://img.shields.io/badge/Architecture-Microservices-red)](#)
[![Messaging](https://img.shields.io/badge/Kafka-Zookeeper-purple)](#)

A **full-stack, microservices-based web application** for managing healthcare operations digitally. Patients can book appointments, access medical reports, track payments, and receive notifications, while doctors manage appointments and reports.  

This system integrates **Angular frontend**, **Spring Boot microservices**, **MSSQL**, and **Kafka/Zookeeper** for event-driven notifications.

---

## **🚀 Features**

- **Patient Module:** Book & manage appointments, view reports, track payments, receive notifications.
- **Doctor Module:** Manage appointments, update status, upload reports.
- **Payments:** Simulated Razorpay workflow, track status, and handle refunds.
- **Notifications:** Event-driven real-time notifications via **Kafka/Zookeeper**.
- **Security:** JWT authentication & role-based access.
- **UI:** Responsive Angular Material interface.
- **Architecture:** Built with **microservices**, enabling scalability and modularity.

---

## **🛠 Tech Stack**

**Frontend:** Angular 18 | Angular Material | TypeScript | RxJS  
**Backend:** Spring Boot Microservices | REST APIs | JWT Authentication  
**Database:** MSSQL / H2  
**Messaging/Event Bus:** Kafka | Zookeeper  
**Services:** Appointments, Payments, Reports, Notifications, Users, Doctors, Patients  

---
## 🏗 System Architecture

Below is the architecture of the **Digital HealthCare Management System**, showing how the microservices interact with the frontend, database, and Kafka/Zookeeper messaging system.

<img width="964" height="474" alt="Image" src="https://github.com/user-attachments/assets/6047bb2e-aca8-43b1-a919-fdc89126afe4" />




