package com.zidan.skripsikelor.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("nik") val nik: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("tempat_lahir") val tempatLahir: String,
    @SerializedName("tanggal_lahir") val tanggalLahir: String,
    @SerializedName("jenis_kelamin") val jenisKelamin: String,
    @SerializedName("pekerjaan") val pekerjaan: String,
    @SerializedName("agama") val agama: String,
    @SerializedName("kewarganegaraan") val kewarganegaraan: String,
    @SerializedName("golongan_darah") val golonganDarah: String,
    @SerializedName("status_pernikahan") val statusPernikahan: String,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("nama_dusun") val namaDusun: String,
    @SerializedName("no_rw") val noRW: String,
    @SerializedName("no_rt") val noRT: String
)
