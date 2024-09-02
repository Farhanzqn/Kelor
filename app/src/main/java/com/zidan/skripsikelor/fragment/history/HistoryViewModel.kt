package com.zidan.skripsikelor.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.SuratItem
import com.zidan.skripsikelor.data.repository.HistoryRepository

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    fun getDocumentHistory(nik: String, token: String): LiveData<Result<List<SuratItem>>> {
        return repository.getDocumentHistory(nik, token)
    }
}