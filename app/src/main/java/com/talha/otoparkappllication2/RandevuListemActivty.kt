package com.talha.otoparkappllication2

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityRandevuListemActivtyBinding

class RandevuListemActivty : AppCompatActivity() {
    private lateinit var binding: ActivityRandevuListemActivtyBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandevuListemActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Veritabanındaki randevu bilgilerini al
        val randevuList = dbHelper.getRandevuList()

        // Randevu bilgilerini listView'e bağla
        val adapter = object : ArrayAdapter<RandevuModel>(this, android.R.layout.simple_list_item_2, android.R.id.text1, randevuList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)

                // Her bir öğeyi temsil eden RandevuModel objesini al
                val randevu = getItem(position)

                // Görünüme tarih ve saat bilgilerini yerleştir
                val textView1 = view.findViewById<TextView>(android.R.id.text1)
                val textView2 = view.findViewById<TextView>(android.R.id.text2)

                textView1.text = "Randevu Tarihi: ${randevu?.tarih}"
                textView2.text = "Randevu Saati: ${randevu?.saat}"

                return view
            }
        }

        binding.RandevulistView.adapter = adapter
    }
}
