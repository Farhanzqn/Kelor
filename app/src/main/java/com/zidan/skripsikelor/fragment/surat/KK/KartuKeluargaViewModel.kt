package com.zidan.skripsikelor.fragment.surat.KK

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.repository.SuratRepository
import com.zidan.skripsikelor.data.response.KartuKeluargaRequest
import com.zidan.skripsikelor.data.response.suratResponse

class KartuKeluargaViewModel
    (private val repository: SuratRepository
) : ViewModel() {

    fun submitDocumentKartuKeluarga(token: String, request: KartuKeluargaRequest): LiveData<Result<suratResponse>> {
        return repository.submitSuratKartuKeluarga(token, request)
    }
}