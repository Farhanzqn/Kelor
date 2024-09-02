package com.zidan.skripsikelor.data.response

data class SkkmRumahSakitRequest(
    val nik: String,
    val nama: String,
    val tempat_lahir: String,
    val tanggal_lahir: String,
    val jenis_kelamin: String,
    val alamat: String,
    val formulir_judul: String,
    val nomor_surat: String,
    val nomor_reg: String,
    val tanggalSuratKeterangan: String,
    val tanggal_surat_inap: String,
    val rumahSakit: String
)
