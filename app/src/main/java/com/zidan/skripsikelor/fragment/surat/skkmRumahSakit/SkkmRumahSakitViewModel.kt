package com.zidan.skripsikelor.fragment.surat.skkmRumahSakit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.repository.ResidentRepository
import com.zidan.skripsikelor.data.repository.SuratRepository
import com.zidan.skripsikelor.data.response.ResidentResponse
import com.zidan.skripsikelor.data.response.SkkmRumahSakitRequest
import com.zidan.skripsikelor.data.response.suratResponse

class SkkmRumahSakitViewModel (private val repository: SuratRepository,private val repositoryResident: ResidentRepository) : ViewModel() {

    fun submitDocumentSkkmRumahSakit(token: String, request: SkkmRumahSakitRequest): LiveData<Result<suratResponse>> {
        return repository.submitSuratSkkmRumahSakit(token, request)
    }

    fun getResident(nik: String): LiveData<Result<ResidentResponse>> {
        return repositoryResident.getResident(nik)
    }
}