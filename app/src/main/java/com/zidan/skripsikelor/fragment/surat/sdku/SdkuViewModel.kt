package com.zidan.skripsikelor.fragment.surat.sdku

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.repository.ResidentRepository
import com.zidan.skripsikelor.data.repository.SuratRepository
import com.zidan.skripsikelor.data.response.DomisiliUsahaRequest
import com.zidan.skripsikelor.data.response.ResidentResponse
import com.zidan.skripsikelor.data.response.suratResponse

class SdkuViewModel (private val repository: SuratRepository,private val repositoryResident: ResidentRepository) : ViewModel() {

    fun submitDocumentSdku(token: String, request: DomisiliUsahaRequest): LiveData<Result<suratResponse>> {
        return repository.submitSuratSDKU(token, request)
    }
    fun getResident(nik: String): LiveData<Result<ResidentResponse>> {
        return repositoryResident.getResident(nik)
    }
}