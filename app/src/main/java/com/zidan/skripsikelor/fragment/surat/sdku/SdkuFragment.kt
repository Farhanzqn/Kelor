package com.zidan.skripsikelor.fragment.surat.sdku

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
import com.zidan.skripsikelor.data.response.DomisiliUsahaRequest
import com.zidan.skripsikelor.retrofit.RetrofitClient
import java.util.Calendar
import kotlin.random.Random

class SdkuFragment : Fragment() {

    private lateinit var viewModel: SdkuViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var submitButton: Button
    private lateinit var tanggalPendirianEditText: EditText
    private lateinit var tanggalLahirEditText: EditText
    private lateinit var spinnerJenisKelamin: Spinner
    private lateinit var nikEditText: EditText
    private lateinit var namaEditText: EditText
    private lateinit var tempatLahirEditText: EditText
    private lateinit var pekerjaanEditText: EditText
    private lateinit var alamatEditText: EditText
    private lateinit var agamaEditText: EditText
    private lateinit var kewarganegaraanEditText: EditText

    private lateinit var nomorSuratEditText: EditText
    private lateinit var nomorRegistrasiEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sdku, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val suratRepository = SuratRepository()
        val historyRepository = HistoryRepository(apiService = RetrofitClient.apiService)
        val residentRepository = ResidentRepository(RetrofitClient.apiService,requireContext())
        val factory = SuratViewModelFactory(suratRepository, historyRepository, residentRepository)
        viewModel = ViewModelProvider(this, factory).get(SdkuViewModel::class.java)

        progressBar = view.findViewById(R.id.progressBar)
        submitButton = view.findViewById(R.id.btn_submit)
        tanggalPendirianEditText = view.findViewById(R.id.ed_tanggal_pendirian)
        tanggalLahirEditText = view.findViewById(R.id.ed_tglLahir)
        spinnerJenisKelamin = view.findViewById(R.id.spinner_jenis_kelamin)

        nikEditText = view.findViewById(R.id.ed_nik)
        namaEditText = view.findViewById(R.id.ed_name)
        tempatLahirEditText = view.findViewById(R.id.ed_tempat)
        pekerjaanEditText = view.findViewById(R.id.ed_pekerjaan)
        alamatEditText = view.findViewById(R.id.ed_alamat)
        agamaEditText = view.findViewById(R.id.ed_agama)
        kewarganegaraanEditText = view.findViewById(R.id.ed_kewarganegaraan)

        nomorSuratEditText = view.findViewById(R.id.ed_NomorSurat)
        nomorRegistrasiEditText = view.findViewById(R.id.ed_nomor_registrasi)

        val randomNomorSurat = Random.nextInt(1, 1001)
        val randomNomorRegistrasi = Random.nextInt(1, 1001)

        // Set random numbers to the EditTexts and make them read-only
        setReadOnlyEditText(nomorSuratEditText, randomNomorSurat.toString())
        setReadOnlyEditText(nomorRegistrasiEditText, randomNomorRegistrasi.toString())

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.jenis_kelamin_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerJenisKelamin.adapter = adapter

        tanggalPendirianEditText.setOnClickListener {
            showDatePickerDialog(tanggalPendirianEditText)
        }


        tanggalLahirEditText.setOnClickListener {
            showDatePickerDialog(tanggalLahirEditText)
        }
        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val nik = sharedPreferences.getString("user_nik", "")
        setReadOnlyEditText(nikEditText,nik)
        // Jika NIK tersedia, ambil data pengguna
        if (!nik.isNullOrEmpty()) {
            getResidentData(nik)
        }
        submitButton.setOnClickListener {
            val noKtp = view.findViewById<EditText>(R.id.ed_nik).text.toString()
            val nama = view.findViewById<EditText>(R.id.ed_name).text.toString()
            val tempatLahir = view.findViewById<EditText>(R.id.ed_tempat).text.toString()
            val tanggalLahir = tanggalLahirEditText.text.toString()
            val jenisKelamin = spinnerJenisKelamin.selectedItem.toString()
            val agama = view.findViewById<EditText>(R.id.ed_agama).text.toString()
            val pekerjaan = view.findViewById<EditText>(R.id.ed_pekerjaan).text.toString()
            val kewarganegaraan = view.findViewById<EditText>(R.id.ed_kewarganegaraan).text.toString()
            val alamat = view.findViewById<EditText>(R.id.ed_alamat).text.toString()
            val nomorSurat = view.findViewById<EditText>(R.id.ed_NomorSurat).text.toString()
            val nomorRegistrasi = view.findViewById<EditText>(R.id.ed_nomor_registrasi).text.toString()
            val tanggalPendirian = tanggalPendirianEditText.text.toString()
            val namaPerusahaan = view.findViewById<EditText>(R.id.ed_nama_perusahaan).text.toString()
            val jenisUsaha = view.findViewById<EditText>(R.id.ed_jenis_usaha).text.toString()
            val penanggungJawab = view.findViewById<EditText>(R.id.ed_Penanggung_Jawab).text.toString()
            val jumlahKaryawan = view.findViewById<EditText>(R.id.ed_jumlah_karyawan).text.toString()
            val statusBangunan = view.findViewById<EditText>(R.id.ed_status_bangunan).text.toString()
            val alamatPerusahaan = view.findViewById<EditText>(R.id.ed_alamat_perusahaan).text.toString()
            val lokasiUsaha = view.findViewById<EditText>(R.id.ed_lokasi_usaha).text.toString()

            if (noKtp.isEmpty()
                || nama.isEmpty()
                || tempatLahir.isEmpty()
                || tanggalLahir.isEmpty()
                || jenisKelamin.isEmpty()
                || agama.isEmpty()
                || pekerjaan.isEmpty()
                || kewarganegaraan.isEmpty()
                || alamat.isEmpty()
                || nomorSurat.isEmpty()
                || nomorRegistrasi.isEmpty()
                || tanggalPendirian.isEmpty()
                || namaPerusahaan.isEmpty()
                || jenisUsaha.isEmpty()
                || penanggungJawab.isEmpty()
                || jumlahKaryawan.isEmpty()
                || statusBangunan.isEmpty()
                || alamatPerusahaan.isEmpty()
                || lokasiUsaha.isEmpty()) {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = DomisiliUsahaRequest(
                nik = noKtp,
                nama = nama,
                tempat_lahir = tempatLahir,
                tanggal_lahir = tanggalLahir,
                jenis_kelamin = jenisKelamin,
                agama = agama,
                pekerjaan = pekerjaan,
                kewarganegaraan = kewarganegaraan,
                alamat = alamat,
                nomor_surat = nomorSurat,
                nomor_reg = nomorRegistrasi,
                tanggal_pendirian = tanggalPendirian,
                perusahaan = namaPerusahaan,
                jenis_usaha = jenisUsaha,
                penanggung_jawab = penanggungJawab,
                jumlah_karyawan = jumlahKaryawan,
                status_bangunan = statusBangunan,
                lokasi_usaha = lokasiUsaha
            )

            val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("auth_token", "") ?: ""

            progressBar.visibility = View.VISIBLE
            submitButton.isEnabled = false

            Log.d("SdkuFragment", "Request: $request")
            Log.d("SdkuFragment", "Token: $token")

            viewModel.submitDocumentSdku(token, request).observe(viewLifecycleOwner, Observer { result ->
                progressBar.visibility = View.GONE
                submitButton.isEnabled = true

                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        showCustomAlertDialog("Yeayy", "Surat SKKM Rumah Sakit Berhasil Dibuat.")
                        findNavController().navigate(R.id.action_suratSDKUFragment_to_homeFragment)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Log.e("SdkuFragment", "Error response: ${result.errorMessage}")
                        showCustomAlertDialog("Failed to submit document: ${result.errorMessage}", "Pastikan Semua Terisi.")
                    }
                    is Result.Loading -> {
                        showLoading(true)
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
                    setReadOnlyEditText(agamaEditText, data.agama)
                    setReadOnlyEditText(kewarganegaraanEditText, data.kewarganegaraan)
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
        editText.isFocusable = false
        editText.isEnabled = false
        editText.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.disabled_background))
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