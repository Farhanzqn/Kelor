package com.zidan.skripsikelor.data.response

data class SkkmSekolahRequest(
    val nik: String,
    val nama: String,
    val formulir_judul: String,
    val tempat_lahir: String,
    val tanggal_lahir: String,
    val jenis_kelamin: String,
    val pekerjaan: String,
    val agama: String,
    val kewarganegaraan: String,
    val alamat: String,
    val status_keluarga: String,
    val nomor_surat: String,
    val asal_sekolah: String
)
