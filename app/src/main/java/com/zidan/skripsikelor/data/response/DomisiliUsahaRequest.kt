package com.zidan.skripsikelor.data.response

data class DomisiliUsahaRequest(
    val nik: String,
    val nama: String,
    val tempat_lahir: String,
    val tanggal_lahir: String,
    val jenis_kelamin: String,
    val agama: String,
    val pekerjaan: String,
    val kewarganegaraan: String,
    val alamat: String,
    val tanggal_pendirian: String,
    val perusahaan: String,
    val jenis_usaha: String,
    val penanggung_jawab: String,
    val jumlah_karyawan: String,
    val status_bangunan: String,
    val nomor_surat : String,
    val nomor_reg : String,
    val lokasi_usaha: String
)
