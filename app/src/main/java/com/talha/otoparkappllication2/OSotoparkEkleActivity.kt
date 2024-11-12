package com.talha.otoparkappllication2

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityOsotoparkEkleBinding

class OSotoparkEkleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOsotoparkEkleBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOsotoparkEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DatabaseHelper sınıfını initialize et
        databaseHelper = DatabaseHelper(this)

        binding.otoparkEkleButton.setOnClickListener {
            val otoparkAdi = binding.otoparkAdiEditText.text.toString().trim()
            val otoparkAdresi = binding.otoparkAdresiEditText.text.toString().trim()
            val otoparkKapasitesi = binding.otoparkKapasitesiEditText.text.toString().trim()

            if (validateInput(otoparkAdi, otoparkAdresi, otoparkKapasitesi)) {
                if (!databaseHelper.otoparkVarMi(otoparkAdi, otoparkAdresi)) {
                    val kapasite = otoparkKapasitesi.toInt()
                    val sharedPreferences = getSharedPreferences("com.talha.otoparkapplication2.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
                    val kaydedenOtoparkSahipId = sharedPreferences.getInt("otoparkSahipId", -1)

                    if (kaydedenOtoparkSahipId != -1) {
                        val yeniOtoparkId = databaseHelper.otoparkEkle(otoparkAdi, otoparkAdresi, kapasite, kaydedenOtoparkSahipId)
                        if (yeniOtoparkId != -1L) {
                            Toast.makeText(this, "Otopark başarıyla eklendi!", Toast.LENGTH_SHORT).show()
                            // Otopark eklendikten sonra işlemler yapılabilir, örneğin ana ekrana dönülebilir.
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
            }
        }
    }

    private fun validateInput(adi: String, adresi: String, kapasitesi: String): Boolean {
        if (adi.isEmpty()) {
            binding.otoparkAdiEditText.error = "Otopark adı boş olamaz!"
            return false
        }

        if (adresi.isEmpty()) {
            binding.otoparkAdresiEditText.error = "Otopark adresi boş olamaz!"
            return false
        }

        if (kapasitesi.isEmpty()) {
            binding.otoparkKapasitesiEditText.error = "Otopark kapasitesi boş olamaz!"
            return false
        }

        return true
    }
}
