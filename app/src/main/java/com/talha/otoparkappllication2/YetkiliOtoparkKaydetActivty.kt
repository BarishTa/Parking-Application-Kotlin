package com.talha.otoparkappllication2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityYetkiliOtoparkKaydetActivtyBinding

class YetkiliOtoparkKaydetActivty : AppCompatActivity() {

    private lateinit var binding: ActivityYetkiliOtoparkKaydetActivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYetkiliOtoparkKaydetActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonKaydet.setOnClickListener {
            kaydetButtonOnClick()
        }
    }

    private fun kaydetButtonOnClick() {
        try {
            val otoparkAdi = binding.editTextOtoparkAdi.text.toString().trim()
            val otoparkAdresi = binding.editTextOtoparkAdresi.text.toString().trim()
            val otoparkKapasitesi = binding.editTextOtoparkKapasitesi.text.toString().trim()

            // Boş alan kontrolü
            if (otoparkAdi.isEmpty() || otoparkAdresi.isEmpty() || otoparkKapasitesi.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
                return
            }

            // Veritabanına otopark eklemek için DatabaseHelper fonksiyonunu kullan
            val databaseHelper = DatabaseHelper(this)

            // Otoparkın zaten var olup olmadığını kontrol et
            if (!databaseHelper.otoparkVarMi(otoparkAdi, otoparkAdresi)) {
                val kapasite = otoparkKapasitesi.toInt()
                val sharedPreferences = getSharedPreferences("com.talha.otoparkapplication2.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
                val kaydedenOtoparkSahipId = sharedPreferences.getInt("otoparkSahipId", -1)

                if (kaydedenOtoparkSahipId != -1) {
                    val yeniOtoparkId = databaseHelper.otoparkEkle(otoparkAdi, otoparkAdresi, kapasite, kaydedenOtoparkSahipId)
                    if (yeniOtoparkId != -1L) {
                        Toast.makeText(this, "Otopark başarıyla eklendi!", Toast.LENGTH_SHORT).show()
                        // Otopark eklendikten sonra işlemler yapılabilir, örneğin ana ekrana dönülebilir.
                        setResult(RESULT_OK, Intent())
                        finish()
                    } else {
                        Toast.makeText(this, "Otopark eklenirken bir hata oluştu!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Otopark sahibi ID'si geçersiz!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Bu otopark zaten kayıtlı!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Bir hata oluştu: ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
    }
}
