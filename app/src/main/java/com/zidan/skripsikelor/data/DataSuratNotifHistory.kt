package com.zidan.skripsikelor.data

data class SuratItem(
    val document_name: String,
    val nomor_surat: String,
    val status: String,
    val created_at: String
)

data class HistoryResponse(
    val NIK: String,
    val surat: List<SuratItem>
)