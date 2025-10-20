# Referral System

A multi-level referral and commission system for Nika Finance, built using:
- Java: 25
- Spring Boot: 3.5.6
- PostgreSQL: 18.0
- Maven build tool

## Setup
1. Create DB `referral_db` in PostgreSQL 18.0
2. Add `.env` file with DB_USER and DB_PASSWORD
3. Run:
   mvn clean install
   mvn spring-boot:run

## API Endpoints
POST /api/referral/generate      → Generate referral code 
<br>
POST /api/referral/register      → Register user with optional referral
<br>
POST /api/webhook/trade          → Simulate trade & distribute commission
<br>
GET  /api/users                  → Get all users
<br>
GET  /api/users/{id}             → Get user by ID
<br>
GET  /api/users/{id}/referrals   → Get referrals for user
<br>
GET  /api/commissions/{userId}   → Get user commissions

## Testing
Run:
mvn test
<br>
All tests use JUnit 5, Mockito, and MockMvc.

## Notes
Simulates Web3 payout logic (not connected to real blockchain).
Uses USDC as the token for fee and commission simulation.
<br><br><br>

© 2025 Abhishek Kumar.   
All rights reserved. Not licensed for production or commercial use.
