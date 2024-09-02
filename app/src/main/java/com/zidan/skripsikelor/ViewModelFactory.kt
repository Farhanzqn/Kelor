package com.zidan.skripsikelor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zidan.skripsikelor.data.repository.ProfileRepository
import com.zidan.skripsikelor.data.repository.UserRepository
import com.zidan.skripsikelor.fragment.ProfileViewModel
import com.zidan.skripsikelor.fragment.login.LoginViewModel


class ViewModelFactory (private val repository: UserRepository,private val profileRepository: ProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}