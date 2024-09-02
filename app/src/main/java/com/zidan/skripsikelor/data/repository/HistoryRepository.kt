package com.zidan.skripsikelor.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.HistoryResponse
import com.zidan.skripsikelor.data.SuratItem
import com.zidan.skripsikelor.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryRepository(private val apiService: ApiService) {

    fun getDocumentHistory(nik: String, token: String): LiveData<Result<List<SuratItem>>> {
        val result = MutableLiveData<Result<List<SuratItem>>>()
        result.postValue(Result.Loading)

        apiService.getDocumentHistory(nik, "Bearer $token").enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful) {
                    result.postValue(Result.Success(response.body()?.surat ?: emptyList()))
                } else {
                    result.postValue(Result.Error("Failed to load from server: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                result.postValue(Result.Error("Failed to load from server: ${t.message}"))
            }
        })

        return result
    }
}