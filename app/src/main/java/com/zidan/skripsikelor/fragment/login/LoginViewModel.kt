package com.zidan.skripsikelor.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.data.repository.UserRepository
import com.zidan.skripsikelor.data.response.LoginResponse

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
     fun login(username: String, password: String): LiveData<Result<LoginResponse>> {
        return repository.login(username, password)
    }
}
