package com.zidan.skripsikelor.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zidan.skripsikelor.R
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.ViewModelFactory
import com.zidan.skripsikelor.data.repository.ProfileRepository
import com.zidan.skripsikelor.data.repository.UserRepository
import com.zidan.skripsikelor.data.response.ProfileResponse

class ProfileFragment : Fragment() {

    private lateinit var nikTextView: TextView
    private lateinit var viewModel: ProfileViewModel
    private lateinit var namaTextView: TextView
    private lateinit var pekerjaanTextView: TextView
    private lateinit var agamaTextView: TextView
    private lateinit var alamatTextView: TextView
    private lateinit var logoutButton: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nikTextView = view.findViewById(R.id.tv_nik_value)
        namaTextView = view.findViewById(R.id.tv_nama_value)
        pekerjaanTextView = view.findViewById(R.id.tv_pekerjaan_value)
        agamaTextView = view.findViewById(R.id.tv_agama_value)
        alamatTextView = view.findViewById(R.id.tv_alamat_value)
        // Inisialisasi semua TextView lainnya dengan findViewById

        val repository = ProfileRepository()
        val userRepository = UserRepository()
        val factory = ViewModelFactory(userRepository,repository)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val nik = sharedPreferences.getString("user_nik", "") ?: ""
        val token = sharedPreferences.getString("auth_token", "") ?: ""

        Log.d("ProfileFragment", "NIK: $nik")
        Log.d("ProfileFragment", "Token: $token")

        if (nik.isNotEmpty()) {
            nikTextView.text = nik
        } else {
            Log.e("ProfileFragment", "NIK tidak ditemukan di SharedPreferences.")
            // Tambahkan logika jika NIK kosong, misalnya menampilkan pesan kesalahan.
        }


        logoutButton = view.findViewById(R.id.card_keluar)
        logoutButton.setOnClickListener {
            showCustomLogoutDialog()
        }

        viewModel.getProfilePenduduk(nik, token).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    Log.d("ProfileFragment", "Profile data received: ${result.data}")
                    Log.d("ProfileFragment", "Fetching profile data for NIK: $nik")
                    updateUI(result.data)
                }
                is Result.Error -> {
                    Log.e("ProfileFragment", "Failed to load profile: ${result.errorMessage}")
                    Toast.makeText(requireContext(), "Failed to load profile: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateUI(profile: ProfileResponse) {
        namaTextView.text = profile.nama
        pekerjaanTextView.text = profile.pekerjaan
        agamaTextView.text = profile.agama
        alamatTextView.text = profile.alamat

    }

    private fun showCustomLogoutDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()

        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)
        val titleTextView = dialogView.findViewById<TextView>(R.id.tv_alert_title)
        val messageTextView = dialogView.findViewById<TextView>(R.id.tv_alert_message)

        titleTextView.text = "Logout"
        messageTextView.text = "Apakah Anda ingin keluar?"

        btnYes.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            alertDialog.dismiss()
        }

        btnNo.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
