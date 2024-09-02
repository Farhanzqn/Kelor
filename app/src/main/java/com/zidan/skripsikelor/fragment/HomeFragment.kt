package com.zidan.skripsikelor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zidan.skripsikelor.R
import com.zidan.skripsikelor.adapter.AdapterBerita
import com.zidan.skripsikelor.adapter.InfoPagerAdapter
import com.zidan.skripsikelor.data.DataBerita
import com.zidan.skripsikelor.data.DataInfo

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterBerita: AdapterBerita
    private val newsList = mutableListOf<DataBerita>()
    private lateinit var notificationBadge: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val ic_notifikasi = view.findViewById<ImageView>(R.id.ic_notification)

        val iv_persyaratanSKU = view.findViewById<ImageView>(R.id.iv_persyaratanSKU)
        val iv_sdku = view.findViewById<ImageView>(R.id.iv_skdu)
        val iv_kartu_keluarga = view.findViewById<ImageView>(R.id.iv_kartuKeluarga)
        val iv_skkm_sekolah = view.findViewById<ImageView>(R.id.iv_skkm_sekolah)
        val iv_skkm_rumahSakit = view.findViewById<ImageView>(R.id.iv_skkm_rumahSakit)



        val rv: ViewPager2 = view.findViewById(R.id.rv_info)


        val places = listOf(
            DataInfo("Jumlah Penduduk", "14.441 Jiwa", R.drawable.logo),
            DataInfo("Luas Desa", "3.410km²", R.drawable.logo),
            DataInfo("Kepadatan", "4,24 jiwa/km²", R.drawable.logo)
        )



        ic_notifikasi.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_notificationFragment)
            hideBottomNavigationView()
        }

        iv_persyaratanSKU.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_pernyataanSKUFragment)
            hideBottomNavigationView()
        }

        iv_sdku.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_suratSDKUFragment)
            hideBottomNavigationView()
        }
        iv_kartu_keluarga.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_kartuKeluarga)
            hideBottomNavigationView()
        }
        iv_skkm_sekolah.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_skkmSekolah)
            hideBottomNavigationView()
        }

        iv_skkm_rumahSakit.setOnClickListener {
            findNavController().navigate(R.id.skkmRumahSakitFragment)
            hideBottomNavigationView()
        }
        val adapter = InfoPagerAdapter(places)
        rv.adapter = adapter


        val recyclerView: RecyclerView = view.findViewById(R.id.rv_berita)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapterBerita = AdapterBerita(newsList) { news ->
            val bundle = Bundle().apply {
                putString("title", news.title)
                putString("date", news.date)
                putInt("imageResId", news.imageResId)
                putString("description", news.description)
            }
            findNavController().navigate(R.id.action_homepageFragment_to_detailNewsFragment, bundle)
        }

        view.findViewById<RecyclerView>(R.id.rv_berita).apply {
            layoutManager = LinearLayoutManager(context)
        }
        recyclerView.adapter = adapterBerita
        loadDummyNews()

        return view
    }

    private fun loadDummyNews() {
        newsList.add(DataBerita(
            "Pemdes Cipayung Fokuskan Pembangunan Infrastruktur Jalan Desa Tahun 2024",
            "10 August 2024",
            R.drawable.desa_cipayung,
            getString(R.string.berita1_description))
        )
        newsList.add(DataBerita(
            "23 Tahun Warga Pertanyakan Tapal Batas Dua Desa di Megamendung, Pemkab Bogor Turun Tangan",
            "09 August 2024",
            R.drawable.desa_cipayung2,
            getString(R.string.berita2_description))
        )
        newsList.add(DataBerita(
            "Tanah Longsor di Desa Cipayung Girang Megamendung, Jembatan Ambruk dan Pondasi Rumah Warga Tergerus",
            "08 August 2024",
            R.drawable.longsor,
            getString(R.string.berita3_description))
        )
        newsList.add(DataBerita(
            "Diskon Spesial di Hari Ulang Tahun Desa",
            "07 August 2024",
            R.drawable.diskon10,
            getString(R.string.berita4_description))
        )
        newsList.add(DataBerita(
            "Festival Budaya Desa Akan Diadakan Tahun Depan",
            "06 August 2024",
            R.drawable.festival,
            getString(R.string.berita5_description))
        )
        newsList.add(DataBerita(
            "Program Kesehatan Desa Baru Diluncurkan",
            "05 August 2024",
            R.drawable.health_program,
            getString(R.string.berita6_description))
        )
        adapterBerita.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.VISIBLE
    }

    private fun hideBottomNavigationView() {
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView?.visibility = View.GONE
    }


}