
# Loan Management System

## 📖 **Deskripsi**
Sistem manajemen pinjaman ini dirancang untuk membantu lembaga keuangan mengelola proses pengajuan, evaluasi, persetujuan, dan penolakan pinjaman. Aplikasi ini mencakup fitur pengelolaan pelanggan, analisis risiko kredit, dan pembatasan pengajuan ulang.

---

## 🚀 **Fitur Utama**
1. **Pengelolaan Pelanggan**  
   - Menyimpan data pelanggan, termasuk skor kredit, penghasilan, status pekerjaan, dan histori pinjaman.

2. **Pengajuan Pinjaman (Loan Application)**  
   - Pelanggan dapat mengajukan pinjaman dengan jumlah tertentu, durasi, dan suku bunga.

3. **Evaluasi Pinjaman oleh Admin**  
   - Admin dapat menyetujui atau menolak pinjaman berdasarkan kriteria tertentu seperti:
     - Skor kredit pelanggan.
     - Penghasilan dan rasio cicilan terhadap pendapatan.
     - Riwayat pelunasan pinjaman.
     - Status pekerjaan pelanggan.

4. **Notifikasi Keputusan**  
   - Pelanggan diberitahu melalui notifikasi tentang status pengajuan pinjaman.

5. **Audit dan Riwayat Evaluasi**  
   - Setiap keputusan admin tercatat dalam riwayat evaluasi untuk keperluan audit.

6. **Pembatasan Pengajuan Ulang**  
   - Pelanggan yang ditolak hanya dapat mengajukan ulang setelah periode tertentu.

---

## ⚙️ **Alur Aplikasi**

### 1. **Pendaftaran Pelanggan**
- **Input**: Data pelanggan seperti nama, email, penghasilan, status pekerjaan.  
- **Proses**:
  - Data pelanggan disimpan di database.
  - Skor kredit awal dihitung (default: 700).  
- **Endpoint**: `POST /api/v1/customers`  
- **Respons**:
  ```json
  {
    "id": "customer_id",
    "name": "Customer Name",
    "creditScore": 700,
    "income": 5000000,
    "employmentStatus": "FULL_TIME"
  }
  ```

---

### 2. **Pengajuan Pinjaman**
- **Input**: ID pelanggan, jumlah pinjaman, durasi, suku bunga.  
- **Proses**:
  - Validasi apakah pelanggan tidak memiliki pinjaman aktif.
  - Jika valid, data pinjaman disimpan dengan status `PENDING`.  
- **Endpoint**: `POST /api/loans/apply`  
- **Respons**:
  ```json
  {
    "loanId": "loan_id",
    "customerId": "customer_id",
    "amount": 1000000,
    "duration": 12,
    "interestRate": 5.5,
    "approvalStatus": "PENDING"
  }
  ```

---

### 3. **Evaluasi Pinjaman oleh Admin**
- **Input**: ID pinjaman, ID admin, keputusan (approved/rejected), alasan penolakan (jika ditolak).  
- **Proses**:
  - Validasi apakah user adalah admin.
  - Evaluasi risiko berdasarkan skor kredit, pendapatan, histori pinjaman, dll.
  - Update status pinjaman (APPROVED/REJECTED).
  - Kirim notifikasi ke pelanggan.
  - Simpan riwayat evaluasi.  
- **Endpoint**: `POST /api/v1/loans/evaluate`  
- **Respons** (jika disetujui):
  ```json
  {
    "loanId": "loan_id",
    "approvalStatus": "APPROVED",
    "amount": 1000000,
    "duration": 12,
    "interestRate": 5.5,
    "applicationDate": "2024-11-17T13:20:46",
    "customerId": "customer_id"
  }
  ```
- **Respons** (jika ditolak):
  ```json
  {
    "loanId": "loan_id",
    "approvalStatus": "REJECTED",
    "rejectionReason": "Insufficient income",
    "applicationDate": "2024-11-17T13:20:46",
    "customerId": "customer_id"
  }
  ```

---

### 4. **Notifikasi Keputusan**
- **Proses**:
  - Pelanggan diberitahu melalui email atau SMS tentang status pengajuan.

---

### 5. **Riwayat Evaluasi**
- **Proses**:
  - Keputusan admin dicatat untuk keperluan audit dan analisis.  
- **Struktur Data**:
  ```json
  {
    "evaluationId": 1,
    "loanId": "loan_id",
    "adminId": "admin_id",
    "status": "APPROVED",
    "evaluationDate": "2024-11-17T14:00:00",
    "creditScore": 700,
    "income": 5000000,
    "monthlyInstallment": 50000
  }
  ```

---

### 6. **Pembatasan Pengajuan Ulang**
- **Proses**:
  - Jika pinjaman ditolak, pelanggan hanya dapat mengajukan ulang setelah 30 hari.  
- **Database Update**:
  ```java
  customer.setNextEligibleDate(LocalDateTime.now().plusDays(30));
  ```
  
---


## 🌟 **Fitur Mendatang**
- Laporan bulanan otomatis.
- Integrasi dengan layanan skor kredit pihak ketiga.
- Sistem pemberitahuan real-time menggunakan WebSocket.

---

## 💻 **Cara Menjalankan**
1. Clone repository ini:  
   ```bash
   git clone https://github.com/username/loan-management-system.git
   ```
2. Masuk ke direktori proyek:  
   ```bash
   cd loan-management-system
   ```
3. Jalankan aplikasi:  
   ```bash
   ./mvnw spring-boot:run
   ```


### API Workflow

1. **Customer Registration**
   - **Endpoint:** `POST /api/customers/register`
   - **Input:** Customer details (name, email, income, employment status, etc.)
   - **Output:** Confirmation of successful registration or an error message.

2. **Apply for Loan**
   - **Endpoint:** `POST /api/loans/apply`
   - **Input:** Customer ID, loan details (amount, duration, interest rate).
   - **Workflow:**
     - Validate if the customer has active loans.
     - Create a new loan record with `PENDING` status.
     - Respond with loan details in a DTO.

3. **Evaluate Loan Application (Admin Action)**
   - **Endpoint:** `PUT /api/loans/evaluate/{loanId}`
   - **Input:** Admin ID, loan ID, evaluation decision (approved/rejected), optional rejection reason.
   - **Workflow:**
     - Verify admin privileges.
     - Validate customer's creditworthiness (credit score, income, repayment history, etc.).
     - Update loan status to `APPROVED` or `REJECTED`.
     - If `REJECTED`, include a rejection reason.
   - **Output:** Updated loan details in a DTO.

4. **Check Loan Status**
   - **Endpoint:** `GET /api/loans/{loanId}`
   - **Input:** Loan ID
   - **Output:** Loan details including current status (PENDING, APPROVED, REJECTED).

5. **Update Credit Score**
   - **Endpoint:** `PUT /api/customers/{customerId}/credit-score`
   - **Input:** Customer ID
   - **Workflow:**
     - Calculate the new credit score based on payment history and other parameters.
     - Update the customer record with the new score.
   - **Output:** Confirmation of credit score update or an error message.

6. **Loan Repayment**
   - **Endpoint:** `POST /api/loans/repay`
   - **Input:** Loan ID, payment amount.
   - **Workflow:**
     - Validate payment amount.
     - Reduce outstanding balance for the loan.
     - If balance becomes zero, mark loan as `PAID`.
   - **Output:** Updated loan details including the remaining balance.

7. **Retrieve Loan History (Customer)**
   - **Endpoint:** `GET /api/customers/{customerId}/loans`
   - **Input:** Customer ID
   - **Output:** List of all loans associated with the customer along with their statuses.

8. **Retrieve Defaulted Loans (Admin)**
   - **Endpoint:** `GET /api/loans/defaulted`
   - **Output:** List of loans with `DEFAULTED` status including customer information.
