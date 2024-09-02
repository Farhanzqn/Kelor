package com.zidan.skripsikelor.fragment.surat.skkmSekolah

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.repository.ResidentRepository
import com.zidan.skripsikelor.data.repository.SuratRepository
import com.zidan.skripsikelor.data.response.ResidentResponse
import com.zidan.skripsikelor.data.response.SkkmSekolahRequest
import com.zidan.skripsikelor.data.response.suratResponse

class SkkmViewModel (private val repository: SuratRepository,private val repositoryResident: ResidentRepository) : ViewModel() {

    fun submitDocumentSkkmSekolah(token: String, request: SkkmSekolahRequest): LiveData<Result<suratResponse>> {
        return repository.submitSuratSkkmSekolah(token, request)
    }
    fun getResident(nik: String): LiveData<Result<ResidentResponse>> {
        return repositoryResident.getResident(nik)
    }
}