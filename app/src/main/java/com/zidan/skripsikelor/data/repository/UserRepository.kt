package com.zidan.skripsikelor.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.response.LoginRequest
import com.zidan.skripsikelor.data.response.LoginResponse
import com.zidan.skripsikelor.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {

    companion object {
        const val TAG = "LoginRepository"
    }
     fun login(username: String, password: String): LiveData<Result<LoginResponse>> {
        val result = MutableLiveData<Result<LoginResponse>>()
        val loginRequest = LoginRequest(username, password)

         Log.d(TAG, "Initiating login request with username: $username, password: $password")

         RetrofitClient.apiService.postLogin("android", loginRequest).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d(TAG, "Login response received: ${response.code()}")

                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d(TAG, "Login successful, response: $it")

                        result.value = Result.Success(it)
                    } ?: run {
                        Log.e(TAG, "Login failed: empty response")

                        result.value = Result.Error(Exception("Login failed: empty response").toString())
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e(TAG, "Login failed: ${response.message()}, error body: $errorBody")
                    result.value = Result.Error("Login failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(TAG, "Login request failed: ${t.message}")
                result.value = Result.Error("Login failed: ${t.message}")

            }
        })

        return result
    }

}
