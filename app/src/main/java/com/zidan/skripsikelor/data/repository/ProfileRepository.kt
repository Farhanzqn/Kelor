package com.zidan.skripsikelor.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.response.ProfileResponse
import com.zidan.skripsikelor.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository {

    fun getProfileData(nik: String, token: String): LiveData<Result<ProfileResponse>> {
        val result = MutableLiveData<Result<ProfileResponse>>()
        result.postValue(Result.Loading)

        RetrofitClient.apiService.getProfile(nik, "Bearer $token","android").enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    if (profile != null) {
                        result.postValue(Result.Success(profile))
                    } else {
                        Log.e("API_ERROR", "Response body is null, response: ${response.raw()}")
                        result.postValue(Result.Error("Response body is null"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("API_ERROR", "Server error: ${response.message()} - $errorBody")
                    result.postValue(Result.Error("Server error: ${response.message()} - $errorBody"))
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e("API_ERROR", "Exception: ${t.message}")
                result.postValue(Result.Error("Exception: ${t.message}"))
            }
        })

        return result
    }
}