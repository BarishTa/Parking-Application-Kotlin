package com.talha.otoparkappllication2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.talha.otoparkappllication2.databinding.ActivityYetkiliOtoparkListeleActivtyBinding

class YetkiliOtoparkListeleActivty : AppCompatActivity() {

    private lateinit var binding: ActivityYetkiliOtoparkListeleActivtyBinding
    private lateinit var otoparkAdapter: OtoparkAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYetkiliOtoparkListeleActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Veritabanından otopark verilerini çekmek için bir örnek
        databaseHelper = DatabaseHelper(this)
        val otoparkList = databaseHelper.getOtoparkList()

        // RecyclerView ve Adapter'ı oluştur
        val recyclerView = binding.recyclerViewOtoparkList
        otoparkAdapter = OtoparkAdapter(otoparkList) { clickedOtopark ->
            // Tıklanan otopark öğesine bağlı olarak DialogFragment'ı başlat
            val dialogFragment = OtoparkDialogFragment.newInstance(clickedOtopark)
            dialogFragment.show(supportFragmentManager, "OtoparkDialog")
        }
        recyclerView.adapter = otoparkAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}


