package com.zidan.skripsikelor.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.repository.ProfileRepository
import com.zidan.skripsikelor.data.response.ProfileResponse

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    fun getProfilePenduduk(nik: String, token: String): LiveData<Result<ProfileResponse>> {
        Log.d("ProfileViewModel", "Requesting profile for NIK: $nik")
        return repository.getProfileData(nik, token)
    }
}