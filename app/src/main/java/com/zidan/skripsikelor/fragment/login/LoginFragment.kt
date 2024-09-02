package com.zidan.skripsikelor.fragment.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zidan.skripsikelor.R
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.ViewModelFactory
import com.zidan.skripsikelor.data.repository.ProfileRepository
import com.zidan.skripsikelor.data.repository.UserRepository

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
    }

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText: EditText = view.findViewById(R.id.email2)
        val passwordEditText: EditText = view.findViewById(R.id.password)
        val loginButton: Button = view.findViewById(R.id.btnLogin)
        progressBar = view.findViewById(R.id.progressBarLogin) // Initialize ProgressBar

        val loginRepository = UserRepository()
        val profileRepository = ProfileRepository()
        val factory = ViewModelFactory(loginRepository,profileRepository)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            Log.d(TAG, "Login button clicked with username: $username")

            if (username.isEmpty()) {
                showCustomAlertDialog("Error", "NIK harus diisi.")
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                showCustomAlertDialog("Error", "Password harus diisi.")
                return@setOnClickListener
            }

            loginViewModel.login(username, password).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        Log.d(TAG, "Login request is loading")
                        loginButton.isEnabled = false
                        progressBar.visibility = View.VISIBLE // Show ProgressBar
                    }
                    is Result.Success -> {
                        Log.d(TAG, "Login successful, token received: ${result.data.token}")

                        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                        sharedPreferences.edit().apply {
                            putString("auth_token", result.data.token)
                            putString("user_nik", username) // Ensure the key matches what is used elsewhere
                            apply()
                            Log.d("LoginFragment", "NIK saved: $username")
                        }

                        Log.d(TAG, "Token and NIK saved to SharedPreferences")
                        progressBar.visibility = View.GONE // Hide ProgressBar
                        loginButton.isEnabled = true
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                    is Result.Error -> {
                        Log.e(TAG, "Login failed: ${result.errorMessage}")
                        loginButton.isEnabled = true
                        progressBar.visibility = View.GONE // Hide ProgressBar

                       
                    }
                }
            })
        }
    }

    private fun showCustomAlertDialog(title: String, message: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_login, null)
        val dialogBuilder = AlertDialog.Builder(requireContext()).setView(dialogView)

        val alertDialog = dialogBuilder.create()

        dialogView.findViewById<TextView>(R.id.dialogTitle).text = title
        dialogView.findViewById<TextView>(R.id.dialogMessage).text = message
        dialogView.findViewById<Button>(R.id.dialogButton).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
