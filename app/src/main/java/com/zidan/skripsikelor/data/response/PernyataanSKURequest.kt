package com.zidan.skripsikelor.data.response

data class PernyataanSKURequest(
    val nik: String,
    val nama: String,
    val tanggal_lahir: String,
    val tempat_lahir: String,
    val jenis_kelamin: String,
    val pekerjaan: String,
    val alamat: String,
    val jenis_usaha: String,
    val lokasi_usaha: String
)
