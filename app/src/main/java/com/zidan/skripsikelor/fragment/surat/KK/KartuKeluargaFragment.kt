package com.zidan.skripsikelor.fragment.surat.KK

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
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
import com.zidan.skripsikelor.data.response.KartuKeluargaRequest
import com.zidan.skripsikelor.retrofit.RetrofitClient
import java.util.Calendar

class KartuKeluargaFragment : Fragment() {

    private lateinit var viewModel: KartuKeluargaViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var submitButton: Button
    private lateinit var tambahAnggotaButton: Button
    private lateinit var dataWargaContainer: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kartu_keluarga, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val historyRepository = HistoryRepository( apiService = RetrofitClient.apiService)
        val suratRepository = SuratRepository()
        val residentRepository = ResidentRepository(RetrofitClient.apiService, requireContext())

        viewModel = ViewModelProvider(
            this,
            SuratViewModelFactory(suratRepository, historyRepository, residentRepository)
        ).get(KartuKeluargaViewModel::class.java)

        progressBar = view.findViewById(R.id.progressBar)
        submitButton = view.findViewById(R.id.btn_buat_surat)
        tambahAnggotaButton = view.findViewById(R.id.btn_tambah_anggota)
        dataWargaContainer = view.findViewById(R.id.data_warga_container)

        val spinnerDusun: Spinner = view.findViewById(R.id.spinner_dusun)
        val spinnerRw: Spinner = view.findViewById(R.id.spinner_rw)
        val spinnerRt: Spinner = view.findViewById(R.id.spinner_rt)

        // Setup spinner dengan array yang sesuai
        setupSpinner(spinnerDusun, R.array.dusun_array)
        setupSpinner(spinnerRw, R.array.rw_array)
        setupSpinner(spinnerRt, R.array.rt_array)
        tambahAnggotaButton = Button(requireContext()).apply {
            text = "Tambah Anggota"
            setOnClickListener {
                addDataWargaForm()
            }
        }

        // Tombol Buat Surat
        submitButton = Button(requireContext()).apply {
            text = "Buat Surat"
            setOnClickListener {
                submitKartuKeluargaForm()
            }
        }

        // Tambahkan tombol ke container
        dataWargaContainer.addView(tambahAnggotaButton)
        dataWargaContainer.addView(submitButton)

        submitButton.setOnClickListener {
            submitKartuKeluargaForm()
        }
    }

    private fun addDataWargaForm() {
        val inflater = LayoutInflater.from(context)
        val dataWargaView = inflater.inflate(R.layout.form_data_warga_kk, dataWargaContainer, false)
        val btnHapus = dataWargaView.findViewById<Button>(R.id.btn_hapus)
        btnHapus.setOnClickListener {
            dataWargaContainer.removeView(dataWargaView)
        }

        setupSpinners(dataWargaView)
        setupDatePicker(dataWargaView.findViewById(R.id.ed_tanggal_lahir))
        setupDatePicker(dataWargaView.findViewById(R.id.ed_tgl_berakhir_paspor))

        val hapusButton = Button(requireContext()).apply {
            text = "Hapus"
            setOnClickListener {
                // Menghapus form ini dari container
                dataWargaContainer.removeView(dataWargaView)
            }
        }
        dataWargaView.findViewById<LinearLayout>(R.id.data_warga_container).addView(hapusButton)

        // Tambahkan form sebelum tombol tambah anggota dan buat surat
        dataWargaContainer.addView(dataWargaView, dataWargaContainer.childCount - 2)
    }

    private fun setupSpinners(view: View) {
        setupSpinner(view.findViewById(R.id.spinner_jenis_kelaminKK), R.array.jenis_kelamin_array)
        setupSpinner(view.findViewById(R.id.spinner_agama), R.array.agama_array)
        setupSpinner(view.findViewById(R.id.spinner_golongan_darah), R.array.golongan_darah_array)
        setupSpinner(view.findViewById(R.id.spinner_status_keluarga), R.array.status_keluarga_array)
        setupSpinner(view.findViewById(R.id.spinner_pendidikan), R.array.pendidikan_terakhir_array)
        setupSpinner(view.findViewById(R.id.spinner_akta_kelahiran), R.array.akta_kelahiran_array)
        setupSpinner(view.findViewById(R.id.spinner_cacat), R.array.penyandang_cacat_array)
        setupSpinner(view.findViewById(R.id.spinner_kelahiran_fisik_mental), R.array.kelahiran_fisik_mental_array)
    }

    private fun setupDatePicker(editText: EditText) {
        editText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
                editText.setText(formattedDate)
            }, year, month, day).show()
        }
    }

    private fun submitKartuKeluargaForm() {
        val noReg = view?.findViewById<EditText>(R.id.ed_nomor_registrasi)?.text.toString()
        val alamat = view?.findViewById<EditText>(R.id.ed_alamat)?.text.toString()
        val kodePos = view?.findViewById<EditText>(R.id.ed_kode_pos)?.text.toString()
        val spinRt = view?.findViewById<Spinner>(R.id.spinner_rt)?.selectedItem.toString()
        val spinRw = view?.findViewById<Spinner>(R.id.spinner_rw)?.selectedItem.toString()
        val spinDusun = view?.findViewById<Spinner>(R.id.spinner_dusun)?.selectedItem.toString()

        if (noReg.isEmpty() || alamat.isEmpty() || kodePos.isEmpty() || spinRt.isEmpty() || spinRw.isEmpty() || spinDusun.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields in the main form", Toast.LENGTH_SHORT).show()
            return
        }

        val anggotaKeluargaRequests = mutableListOf<KartuKeluargaRequest>()

        val nikEditText = view?.findViewById<EditText>(R.id.ed_nikKK)
        val namaEditText = view?.findViewById<EditText>(R.id.ed_nama)
        val tempatLahirEditText = view?.findViewById<EditText>(R.id.ed_tempat_lahir)
        val tanggalLahirEditText = view?.findViewById<EditText>(R.id.ed_tanggal_lahir)
        val jenisKelaminSpinner = view?.findViewById<Spinner>(R.id.spinner_jenis_kelaminKK)
        val agamaSpinner = view?.findViewById<Spinner>(R.id.spinner_agama)
        val golonganDarahSpinner = view?.findViewById<Spinner>(R.id.spinner_golongan_darah)
        val statusKeluargaSpinner = view?.findViewById<Spinner>(R.id.spinner_status_keluarga)
        val pendidikanSpinner = view?.findViewById<Spinner>(R.id.spinner_pendidikan)
        val pekerjaanEditText = view?.findViewById<EditText>(R.id.ed_pekerjaan)
        val noPasporEditText = view?.findViewById<EditText>(R.id.ed_no_paspor)
        val tanggalBerakhirPasporEditText = view?.findViewById<EditText>(R.id.ed_tgl_berakhir_paspor)
        val aktaLahirEditText = view?.findViewById<EditText>(R.id.ed_akta_lahir)
        val aktaKelahiranSpinner = view?.findViewById<Spinner>(R.id.spinner_akta_kelahiran)
        val statusPerkawinanRadioGroup = view?.findViewById<RadioGroup>(R.id.rg_status_perkawinan)
        val penyandangCacatSpinner = view?.findViewById<Spinner>(R.id.spinner_cacat)
        val kelahiranFisikMentalSpinner = view?.findViewById<Spinner>(R.id.spinner_kelahiran_fisik_mental)
        val nikAyahEditText = view?.findViewById<EditText>(R.id.ed_nik_ayah)
        val namaAyahEditText = view?.findViewById<EditText>(R.id.ed_nama_ayah)
        val nikIbuEditText = view?.findViewById<EditText>(R.id.ed_nik_ibu)
        val namaIbuEditText = view?.findViewById<EditText>(R.id.ed_nama_ibu)

        if (nikEditText == null || namaEditText == null || tempatLahirEditText == null || tanggalLahirEditText == null || jenisKelaminSpinner == null ||
            agamaSpinner == null || golonganDarahSpinner == null || statusKeluargaSpinner == null || pendidikanSpinner == null || pekerjaanEditText == null) {
            Toast.makeText(context, "Please fill all required fields in the Data Warga form", Toast.LENGTH_SHORT).show()
            return
        }

        val nik = nikEditText.text.toString()
        val nama = namaEditText.text.toString()
        val tempatLahir = tempatLahirEditText.text.toString()
        val tanggalLahir = tanggalLahirEditText.text.toString()
        val jenisKelamin = jenisKelaminSpinner.selectedItem.toString()
        val agama = agamaSpinner.selectedItem.toString()
        val golonganDarah = golonganDarahSpinner.selectedItem.toString()
        val statusKeluarga = statusKeluargaSpinner.selectedItem.toString()
        val pekerjaan = pekerjaanEditText.text.toString()
        val pendidikan = pendidikanSpinner.selectedItem.toString()
        val noPaspor = noPasporEditText?.text.toString()
        val tanggalBerakhirPaspor = tanggalBerakhirPasporEditText?.text.toString()
        val aktaLahir = aktaLahirEditText?.text.toString()
        val aktaKelahiran = aktaKelahiranSpinner?.selectedItem.toString()
        val statusPerkawinan = when (statusPerkawinanRadioGroup?.checkedRadioButtonId) {
            R.id.rb_belum_kawin -> "Belum kawin"
            R.id.rb_kawin -> "Kawin"
            R.id.rb_cerai_hidup -> "Cerai hidup"
            R.id.rb_cerai_mati -> "Cerai mati"
            else -> ""
        }
        val penyandangCacat = penyandangCacatSpinner?.selectedItem.toString()
        val kelahiranFisikMental = kelahiranFisikMentalSpinner?.selectedItem.toString()
        val nikAyah = nikAyahEditText?.text.toString()
        val namaAyah = namaAyahEditText?.text.toString()
        val nikIbu = nikIbuEditText?.text.toString()
        val namaIbu = namaIbuEditText?.text.toString()

        if (nik.isEmpty() || nama.isEmpty() || tempatLahir.isEmpty() || tanggalLahir.isEmpty() || pekerjaan.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields in the Data Warga form", Toast.LENGTH_SHORT).show()
            return
        }

        val request = KartuKeluargaRequest(
            no_reg = noReg,
            nik = nik,
            nama = nama,
            alamat = alamat,
            tempatLahir = tempatLahir,
            tanggalLahir = tanggalLahir,
            jenisKelamin = jenisKelamin,
            agama = agama,
            golonganDarah = golonganDarah,
            statusKeluarga = statusKeluarga,
            pekerjaan = pekerjaan,
            pendidikan = pendidikan,
            noPaspor = noPaspor,
            tanggalBerakhirPaspor = tanggalBerakhirPaspor,
            aktaLahir = aktaLahir,
            aktaKelahiran = aktaKelahiran,
            statusPerkawinan = statusPerkawinan,
            penyandangCacat = penyandangCacat,
            kelahiranFisikMental = kelahiranFisikMental,
            nikAyah = nikAyah,
            namaAyah = namaAyah,
            nikIbu = nikIbu,
            namaIbu = namaIbu,
            rt = spinRt,
            rw = spinRw,
            dusun = spinDusun
        )

        anggotaKeluargaRequests.add(request)

        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", "") ?: ""

        if (token.isEmpty()) {
            Toast.makeText(context, "Authorization token is missing. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        anggotaKeluargaRequests.forEach { request ->
            viewModel.submitDocumentKartuKeluarga(token, request).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(context, "Kartu Keluarga created successfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_KartuKeluargaFragment_to_homeFragment)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(context, "Failed to create Kartu Keluarga: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }




    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        submitButton.isEnabled = !isLoading
        tambahAnggotaButton.isEnabled = !isLoading
    }

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        ArrayAdapter.createFromResource(
            requireContext(),
            arrayResId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}