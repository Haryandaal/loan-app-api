
# Loan Approval Management System

## 📖 **Deskripsi**
Sistem manajemen pinjaman ini dirancang untuk membantu mengelola proses pengajuan, evaluasi, persetujuan, dan penolakan pinjaman. Aplikasi ini mencakup fitur pengelolaan pelanggan, analisis risiko kredit, dan pembatasan pengajuan ulang.
Aplikasi ini menggunakan Security (Authentication) dengan mengimplementasi JWT (Json Web Token) untuk melakukan generate access token

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

---

## ⚙️ **Alur Aplikasi**

### 1. **Pendaftaran Pelanggan**
- **Input**: Data pelanggan seperti nama, email, penghasilan, status pekerjaan.  
- **Proses**:
  - Data pelanggan disimpan di database.
  - Skor kredit awal dihitung (default: 700).  
- **Endpoint**: `POST /api/customers`  
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
- **Endpoint**: `POST /api/loans/loanId/evaluate`  
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
        "id": "random UUID",
        "customerId": "7d54ffa2-37b7-46a9-a0fb-3b8d898dc6e8",
        "amount": 200000,
        "duration": 2,
        "interestRate": 5.5,
        "approvalStatus": "REJECTED",
        "rejectionReason": "Rejection Reason",
        "applicationDate": "2024-11-17 20:10:27"
  }
  ```

---

## 🌟 **Fitur Mendatang**
- Laporan bulanan otomatis.
- Integrasi dengan layanan skor kredit pihak ketiga.
- Sistem pemberitahuan real-time menggunakan WebSocket.
- Feature jika pinjaman ditolak, pelanggan hanya dapat mengajukan ulang setelah 30 hari.  
- (Notifikasi) Pelanggan diberitahu melalui email atau SMS tentang status pengajuan.

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
3. Sesuaikan database nya di application.properties
   
4. Jalankan aplikasi:  
   ```bash
   ./mvnw spring-boot:run
   ```
5. Detail list endpoint dapat mengakses swagger melalui link berikut:
   ```bash
   dijalankan di local : http://localhost:8080/swagger-ui/index.html
   ```

