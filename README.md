# ShieldSurf v0.2 — Tabs & Protection

## Fitur versi ini
- Multi-tab: buka tab baru dengan tombol `+`, pindah tab, dan tutup tab dengan `×`.
- Pemblokiran dasar domain iklan/pelacak.
- Pop-up dan jendela baru dari situs ditolak; gunakan `+` untuk tab yang dibuat sendiri.
- Konfirmasi untuk beberapa pengalihan otomatis lintas domain yang terdeteksi tanpa gestur pengguna.
- Bookmark lokal, unduhan, hapus cache/cookie/data situs.
- Workflow GitHub Actions untuk membuat APK debug.

## Build APK lewat GitHub Actions
1. Buat repository baru atau buka repository ShieldSurf.
2. Unggah isi folder `ShieldSurf` ke root repository, bukan folder pembungkusnya.
3. Buka **Actions** dan jalankan workflow **Build ShieldSurf APK**.
4. Unduh artifact `ShieldSurf-v0.2-debug-apk`, ekstrak, lalu instal `app-debug.apk`.

## Batasan yang perlu diketahui
Versi ini masih MVP dan belum setara Brave atau uBlock Origin. Blocklist hanya berbasis domain dan tidak dapat memblokir semua iklan. Iklan yang disajikan dari domain/aliran video yang sama dengan konten utama mungkin tetap muncul; pemblokiran agresif juga bisa merusak pemutaran. Deteksi redirect menggunakan sinyal WebView, jadi tidak menjamin semua pengalihan terdeteksi. Pop-up selalu ditolak. Uji pada situs streaming yang memang legal untuk digunakan.

Tidak ada API key yang disertakan. Engine halaman web memakai Android System WebView.
