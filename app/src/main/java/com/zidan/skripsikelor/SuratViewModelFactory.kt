package com.zidan.skripsikelor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zidan.skripsikelor.data.repository.HistoryRepository
import com.zidan.skripsikelor.data.repository.ResidentRepository
import com.zidan.skripsikelor.data.repository.SuratRepository
import com.zidan.skripsikelor.fragment.history.HistoryViewModel
import com.zidan.skripsikelor.fragment.surat.KK.KartuKeluargaViewModel
import com.zidan.skripsikelor.fragment.surat.pernyataanSKU.SkuViewModel
import com.zidan.skripsikelor.fragment.surat.sdku.SdkuViewModel
import com.zidan.skripsikelor.fragment.surat.skkmRumahSakit.SkkmRumahSakitViewModel
import com.zidan.skripsikelor.fragment.surat.skkmSekolah.SkkmViewModel

class SuratViewModelFactory(
    private val suratRepository: SuratRepository,
    private val historyRepository: HistoryRepository,
    private val residentRepository: ResidentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SkuViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SkuViewModel(suratRepository, residentRepository) as T
            }
            modelClass.isAssignableFrom(SdkuViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SdkuViewModel(suratRepository,residentRepository) as T
            }
            modelClass.isAssignableFrom(SkkmViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SkkmViewModel(suratRepository,residentRepository) as T
            }
            modelClass.isAssignableFrom(SkkmRumahSakitViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                SkkmRumahSakitViewModel(suratRepository,residentRepository) as T
            }
            modelClass.isAssignableFrom(KartuKeluargaViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                KartuKeluargaViewModel(suratRepository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                HistoryViewModel(historyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}