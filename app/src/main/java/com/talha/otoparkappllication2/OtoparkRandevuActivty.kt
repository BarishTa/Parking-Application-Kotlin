package com.talha.otoparkappllication2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityOtoparkRandevuActivtyBinding
import com.talha.otoparkappllication2.databinding.DialogRandevuBinding
import java.text.SimpleDateFormat
import java.util.*

class OtoparkRandevuActivty : AppCompatActivity() {
    private lateinit var binding: ActivityOtoparkRandevuActivtyBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtoparkRandevuActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("com.talha.otoparkapplication2.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)

        // Veritabanındaki otopark bilgilerini al
        val otoparkList = dbHelper.getOtoparkList()

        // Otopark bilgilerini listView'e bağla
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, otoparkList.map { it.toString() })
        binding.OtoparkRandevuList.adapter = adapter

        // Otopark listesinden bir öğe seçildiğinde randevu al dialogunu göster
        binding.OtoparkRandevuList.setOnItemClickListener { _, _, position, _ ->
            val selectedOtopark = otoparkList[position]
            showRandevuDialog(selectedOtopark)
        }
    }

    private fun showRandevuDialog(selectedOtopark: OtoparkModel) {
        // Dialog penceresini oluştur
        val builder = AlertDialog.Builder(this)
        val dialogBinding = DialogRandevuBinding.inflate(LayoutInflater.from(this))
        builder.setView(dialogBinding.root)

        // Tarih seçme
        dialogBinding.btnDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val selectedDate = dateFormat.format(calendar.time)
                    dialogBinding.tvSelectedDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Saat seçme
        dialogBinding.btnTimePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val selectedTime = timeFormat.format(calendar.time)
                    dialogBinding.tvSelectedTime.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        // Kaydet butonu
        dialogBinding.btnKaydet.setOnClickListener {
            // Seçilen tarih ve saat ile randevuyu kaydet
            val selectedDate = dialogBinding.tvSelectedDate.text.toString()
            val selectedTime = dialogBinding.tvSelectedTime.text.toString()
            val kullaniciId = sharedPreferences.getInt("kullaniciId", -1)

            if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty() && kullaniciId != -1) {
                val randevuId = dbHelper.randevuEkle(selectedOtopark.otoparkId, kullaniciId, selectedDate, selectedTime)
                if (randevuId != -1L) {
                    Toast.makeText(this, "Randevu başarıyla alındı!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Randevu alınırken bir hata oluştu!", Toast.LENGTH_SHORT).show()
                }
            }

            builder.create().dismiss() // Dialog kapat
        }

        builder.create().show()
    }
}
