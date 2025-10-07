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

---
## ⚙️ Setup
### Start Zookeeper
<img width="964" height="474" alt="start zookeeper" src="https://github.com/user-attachments/assets/107b6455-20fc-434b-bad8-0e4eb6439598" />


### Start Kafka
<img width="1366" height="768" alt="start kafka" src="https://github.com/user-attachments/assets/cfc8d9c3-46c3-46a9-949c-11df178b8378" />


### List of created topics
<img width="1366" height="768" alt="list kafka topics" src="https://github.com/user-attachments/assets/e37b762d-67a7-48b5-81ac-9662d5ec429e" />


## 🧪 Testing JWT Authentication (Postman tool)
### Register 
<img width="1366" height="768" alt="register" src="https://github.com/user-attachments/assets/47933841-089c-490c-8e33-ce04bc4d8fa4" />


### Login
<img width="1366" height="768" alt="login" src="https://github.com/user-attachments/assets/bf639747-9577-4f01-a4a0-0cd6bd634766" />


### User(Patient) Access
<img width="1366" height="768" alt="test" src="https://github.com/user-attachments/assets/ba9839bd-50b5-4240-bb90-15c11af26a28" />


## 📸 Screenshots (UI)
### Register page
<div style="display: flex; gap: 10px;">

  <img src="https://github.com/user-attachments/assets/63bf2f98-e2b0-4b9a-916e-21227ad9c71a" width="48%" alt="Architecture 1" />
  
  <img  src="https://github.com/user-attachments/assets/3b330ff6-42f7-40f1-a461-9fff291d5c5d" width="48%" alt="Architecture 2" />

</div>


### Login page                                   ### Login page
<div style="display: flex; gap: 10px;">
  <img src="https://github.com/user-attachments/assets/37e1416a-a6d1-46b9-87eb-85eed8885470" width="48%" alt="Architecture 1" />
  <img src="https://github.com/user-attachments/assets/a08a55c9-defa-4c7d-9294-5aef9c205426" width="48%" alt="Architecture 2" />
</div>


### Doctor Dashboard
<img width="1366" height="768" alt="Screenshot (454)" src="https://github.com/user-attachments/assets/470234d0-9ca6-4b4e-b042-3c25d12e83ea" />



