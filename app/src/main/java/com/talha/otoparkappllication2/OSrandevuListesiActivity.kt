package com.talha.otoparkappllication2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OSrandevuListesiActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RandevuAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_osrandevu_listesi)

        recyclerView = findViewById(R.id.recyclerViewRandevu)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseHelper = DatabaseHelper(this)

        val sharedPreferences = getSharedPreferences("com.talha.otoparkapplication2.PREFERENCE_FILE_KEY", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("otoparkSahipId", -1)

        val otoparkSahibi = databaseHelper.getOtoparkSahibiByUserId(userId)
        val otoparkSahibiId = otoparkSahibi?.ownerId ?: -1

        val otoparkId = databaseHelper.getOtoparkIdByOwnerId(otoparkSahibiId)

        if (otoparkId != -1L) {
            val randevuList = databaseHelper.getRandevuListForOtopark(otoparkId)

            if (randevuList.isEmpty()) {
                Toast.makeText(this, "Henüz randevunuz bulunmamaktadır.", Toast.LENGTH_SHORT).show()
            } else {
                adapter = RandevuAdapter(randevuList)
                recyclerView.adapter = adapter
            }
        } else {
            Toast.makeText(this, "Otopark bulunamadı.", Toast.LENGTH_SHORT).show()
        }
    }
}
