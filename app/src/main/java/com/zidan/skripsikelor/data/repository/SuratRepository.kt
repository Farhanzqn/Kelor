package com.zidan.skripsikelor.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.response.DomisiliUsahaRequest
import com.zidan.skripsikelor.data.response.KartuKeluargaRequest
import com.zidan.skripsikelor.data.response.PernyataanSKURequest
import com.zidan.skripsikelor.data.response.SkkmRumahSakitRequest
import com.zidan.skripsikelor.data.response.SkkmSekolahRequest
import com.zidan.skripsikelor.data.response.suratResponse
import com.zidan.skripsikelor.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuratRepository {

    fun submitPernyataanSKU(token: String, request: PernyataanSKURequest): LiveData<Result<suratResponse>> {
        val result = MutableLiveData<Result<suratResponse>>()
        result.value = Result.Loading

        RetrofitClient.apiService.postPernyataanSKU("Bearer $token","android", request).enqueue(object : Callback<suratResponse> {
            override fun onResponse(call: Call<suratResponse>, response: Response<suratResponse>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    result.value = Result.Error("Failed to submit document")
                }
            }

            override fun onFailure(call: Call<suratResponse>, t: Throwable) {
                result.value = Result.Error(t.message ?: "Unknown error")
            }
        })

        return result
    }

    fun submitSuratSDKU(token: String, request: DomisiliUsahaRequest): LiveData<Result<suratResponse>> {
        val result = MutableLiveData<Result<suratResponse>>()
        result.value = Result.Loading

        RetrofitClient.apiService.postSuratSDKU("Bearer $token", "android", request).enqueue(object : Callback<suratResponse> {
            override fun onResponse(call: Call<suratResponse>, response: Response<suratResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(Result.Success(it))
                    } ?: run {
                        result.postValue(Result.Error("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    result.postValue(Result.Error("Failed to submit document: $errorBody"))
                }
            }

            override fun onFailure(call: Call<suratResponse>, t: Throwable) {
                result.postValue(Result.Error(t.message ?: "Unknown error"))
            }
        })

        return result
    }

    fun submitSuratSkkmSekolah(token: String, request: SkkmSekolahRequest): LiveData<Result<suratResponse>> {
        val result = MutableLiveData<Result<suratResponse>>()
        result.value = Result.Loading

        RetrofitClient.apiService.postSkkmSekolah("Bearer $token", "android", request).enqueue(object : Callback<suratResponse> {
            override fun onResponse(call: Call<suratResponse>, response: Response<suratResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(Result.Success(it))
                    } ?: run {
                        result.postValue(Result.Error("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    result.postValue(Result.Error("Failed to submit document: $errorBody"))
                }
            }

            override fun onFailure(call: Call<suratResponse>, t: Throwable) {
                result.postValue(Result.Error(t.message ?: "Unknown error"))
            }
        })

        return result
    }

    fun submitSuratSkkmRumahSakit(token: String, request: SkkmRumahSakitRequest): LiveData<Result<suratResponse>> {
        val result = MutableLiveData<Result<suratResponse>>()
        result.value = Result.Loading

        RetrofitClient.apiService.postSkkmRumahSakit("Bearer $token", "android", request).enqueue(object : Callback<suratResponse> {
            override fun onResponse(call: Call<suratResponse>, response: Response<suratResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(Result.Success(it))
                    } ?: run {
                        result.postValue(Result.Error("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    result.postValue(Result.Error("Failed to submit document: $errorBody"))
                }
            }

            override fun onFailure(call: Call<suratResponse>, t: Throwable) {
                result.postValue(Result.Error(t.message ?: "Unknown error"))
            }
        })

        return result
    }

    fun submitSuratKartuKeluarga(token: String, request: KartuKeluargaRequest): LiveData<Result<suratResponse>> {
        val result = MutableLiveData<Result<suratResponse>>()
        result.value = Result.Loading

        RetrofitClient.apiService.postKartuKeluarga("Bearer $token", "android", request).enqueue(object : Callback<suratResponse> {
            override fun onResponse(call: Call<suratResponse>, response: Response<suratResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        result.postValue(Result.Success(it))
                    } ?: run {
                        result.postValue(Result.Error("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    result.postValue(Result.Error("Failed to submit document: $errorBody"))
                }
            }

            override fun onFailure(call: Call<suratResponse>, t: Throwable) {
                result.postValue(Result.Error(t.message ?: "Unknown error"))
            }
        })

        return result
    }
}