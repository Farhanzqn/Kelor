package com.zidan.skripsikelor.fragment.surat.pernyataanSKU

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.repository.ResidentRepository
import com.zidan.skripsikelor.data.repository.SuratRepository
import com.zidan.skripsikelor.data.response.PernyataanSKURequest
import com.zidan.skripsikelor.data.response.ResidentResponse
import com.zidan.skripsikelor.data.response.suratResponse

class SkuViewModel (private val repository: SuratRepository,private val repositoryResident: ResidentRepository) : ViewModel() {

    fun submitDocument(token: String, request: PernyataanSKURequest): LiveData<Result<suratResponse>> {
        return repository.submitPernyataanSKU(token, request)
    }

    fun getResident(nik: String): LiveData<Result<ResidentResponse>> {
        return repositoryResident.getResident(nik)
    }
}