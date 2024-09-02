package com.zidan.skripsikelor.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.response.ResidentResponse
import com.zidan.skripsikelor.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResidentRepository(private val apiService: ApiService, private val context: Context) {

    fun getResident(nik: String): LiveData<Result<ResidentResponse>> {
        val result = MutableLiveData<Result<ResidentResponse>>()
        result.value = Result.Loading

        val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", "") ?: ""

        apiService.getResident("Bearer $token", "android", nik).enqueue(object : Callback<ResidentResponse> {
            override fun onResponse(call: Call<ResidentResponse>, response: Response<ResidentResponse>) {
                if (response.isSuccessful) {
                    result.value = Result.Success(response.body()!!)
                } else {
                    result.value = Result.Error("Failed to fetch data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResidentResponse>, t: Throwable) {
                result.value = Result.Error(t.message ?: "Unknown error")
            }
        })

        return result
    }
}
