package com.zidan.skripsikelor.fragment.surat.pernyataanSKU

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.zidan.skripsikelor.R
import com.zidan.skripsikelor.Result
import com.zidan.skripsikelor.SuratViewModelFactory
import com.zidan.skripsikelor.data.repository.HistoryRepository
import com.zidan.skripsikelor.data.repository.ResidentRepository
import com.zidan.skripsikelor.data.repository.SuratRepository
import com.zidan.skripsikelor.data.response.PernyataanSKURequest
import com.zidan.skripsikelor.retrofit.RetrofitClient
import java.util.Calendar

class PernyataanSKUFragment : Fragment() {

    private lateinit var viewModel: SkuViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var submitButton: Button
    private lateinit var spinnerJenisKelamin: Spinner
    private lateinit var tanggalLahirEditText: EditText
    private lateinit var nikEditText: EditText
    private lateinit var namaEditText: EditText
    private lateinit var tempatLahirEditText: EditText
    private lateinit var pekerjaanEditText: EditText
    private lateinit var alamatEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pernyataan_s_k_u, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val suratRepository = SuratRepository()
        val historyRepository = HistoryRepository(apiService = RetrofitClient.apiService)
        val residentRepository = ResidentRepository(RetrofitClient.apiService, requireContext())
        val factory = SuratViewModelFactory(suratRepository, historyRepository, residentRepository)
        viewModel = ViewModelProvider(this, factory).get(SkuViewModel::class.java)

        tanggalLahirEditText = view.findViewById(R.id.ed_tglLahir)
        nikEditText = view.findViewById(R.id.ed_nik)
        namaEditText = view.findViewById(R.id.ed_name)
        tempatLahirEditText = view.findViewById(R.id.ed_tempat)
        pekerjaanEditText = view.findViewById(R.id.ed_pekerjaan)
        alamatEditText = view.findViewById(R.id.ed_alamat)
        progressBar = view.findViewById(R.id.progressBar)
        submitButton = view.findViewById(R.id.btn_submit)
        spinnerJenisKelamin = view.findViewById(R.id.spinner_jenis_kelamin)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jenis_kelamin_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJenisKelamin.adapter = adapter

        tanggalLahirEditText.setOnClickListener {
            showDatePickerDialog(tanggalLahirEditText)
        }

        // Ambil NIK dari SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val nik = sharedPreferences.getString("user_nik", "")

        Log.d("PernyataanSKUFragment", "NIK dari SharedPreferences: $nik")
        setReadOnlyEditText(nikEditText,nik)
        // Jika NIK tersedia, ambil data pengguna
        if (!nik.isNullOrEmpty()) {
            getResidentData(nik)
        }

        submitButton.setOnClickListener {
            val nama = namaEditText.text.toString()
            val tanggalLahir = tanggalLahirEditText.text.toString()
            val tempatLahir = tempatLahirEditText.text.toString()
            val jenisKelamin = spinnerJenisKelamin.selectedItem.toString()
            val pekerjaan = pekerjaanEditText.text.toString()
            val alamat = alamatEditText.text.toString()
            val jenisUsaha = view.findViewById<EditText>(R.id.ed_jenisUsaha).text.toString()
            val lokasiUsaha = view.findViewById<EditText>(R.id.ed_lokasiUsaha).text.toString()

            val request = PernyataanSKURequest(
                nik ?: "",
                nama,
                tanggalLahir,
                tempatLahir,
                jenisKelamin,
                pekerjaan,
                alamat,
                jenisUsaha,
                lokasiUsaha
            )

            val token = sharedPreferences.getString("auth_token", "") ?: ""

            Log.d("PernyataanSKUFragment", "Token: $token")

            if (token.isEmpty()) {
                Toast.makeText(context, "Authorization token is missing. Please log in again.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.submitDocument(token, request).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        showCustomAlertDialog("Yeayy", "Surat Pernyataan SKU Berhasil Dibuat.")
                        findNavController().navigate(R.id.action_pernyataanSKUFragment_to_homeFragment)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showCustomAlertDialog("Error", "Pastikan Semua Filed Terisi.")
                    }
                }
            })
        }
    }

    private fun getResidentData(nik: String) {
        showLoading(true)
        viewModel.getResident(nik).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("PernyataanSKUFragment", "Loading resident data")
                }
                is Result.Success -> {
                    val data = result.data
                    Log.d("PernyataanSKUFragment", "Resident data: $data")

                    setReadOnlyEditText(namaEditText, data.nama)
                    setReadOnlyEditText(tempatLahirEditText, data.tempat_lahir)
                    setReadOnlyEditText(tanggalLahirEditText, data.tanggal_lahir)
                    setReadOnlyEditText(pekerjaanEditText, data.pekerjaan)
                    setReadOnlyEditText(alamatEditText, data.alamat)
                    setSpinnerSelection(spinnerJenisKelamin, data.jenis_kelamin)
                    spinnerJenisKelamin.isEnabled = false
                    spinnerJenisKelamin.background = ContextCompat.getDrawable(requireContext(), R.drawable.disable_bg)

                    showLoading(false)
                }
                is Result.Error -> {
                    showLoading(false)
                    Log.e("PernyataanSKUFragment", "Failed to fetch data: ${result.errorMessage}")
                    Toast.makeText(context, "Failed to fetch data: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setSpinnerSelection(spinner: Spinner, value: String?) {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == value) {
                spinner.setSelection(i)
                break
            }
        }
    }
    private fun setReadOnlyEditText(editText: EditText, value: String?) {
        editText.setText(value)
        editText.isEnabled = false
        editText.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.disabled_background))
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
            editText.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        submitButton.isEnabled = !isLoading
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
