[![Android CI Build](https://github.com/saleemuchtar/Ingetin-Task-Notifier-App/actions/workflows/gradle.yml/badge.svg)](https://github.com/saleemuchtar/Ingetin-Task-Notifier-App/actions/workflows/gradle.yml)

# Ingetin - Aplikasi Pengingat Tugas

Aplikasi Android sederhana untuk mahasiswa atau pengguna umum dalam mencatat tugas dan mengatur notifikasi deadline agar tidak terlewat.

## Screenshot Aplikasi

<p align="center">
  <img src="screenshot/Page_Utama.jpg" width="250"/>
  <img src="screenshot/Input_Tanggal.jpg" width="250"/>
  <img src="screenshot/Input_Jam.jpg" width="250"/>
  <img src="screenshot/List.jpg" width="250"/>
  <img src="screenshot/Notif.jpg" width="250"/>
</p>

## Fitur Utama
- **Manajemen Tugas:** Menambah, melihat, dan menghapus daftar tugas.
- **Input Deadline:** Memilih tanggal dan waktu deadline dengan `DatePicker` dan `TimePicker`.
- **Notifikasi Tepat Waktu:** Menggunakan `AlarmManager` untuk menjadwalkan dan `NotificationManager` untuk menampilkan notifikasi saat deadline tiba.
- **Penyimpanan Lokal:** Semua data tugas disimpan secara aman di dalam database SQLite lokal di perangkat.
- **Tampilan Modern:** Antarmuka yang bersih dan menarik menggunakan komponen Material Design.

## Teknologi yang Digunakan
- **Bahasa:** Java
- **Arsitektur:** Basic/No-Architecture (Standard Android)
- **Database:** SQLite
- **Notifikasi:** AlarmManager, NotificationManager, BroadcastReceiver
- **Library:**
  - AndroidX AppCompat & Material Components
  - RecyclerView/ListView

## Download Aplikasi

Anda dapat mengunduh versi terbaru dari aplikasi ini melalui halaman rilis kami.



**[➡️ Unduh Ingetin v1.0.0 (APK)](https://github.com/saleemuchtar/Ingetin-Task-Notifier-App/releases/download/v1.0.0/Ingetin.apk)**
