package com.talha.otoparkappllication2

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityOsotoparkGuncelleBinding

class OSotoparkGuncelleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOsotoparkGuncelleBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var otoparkList: List<OtoparkModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOsotoparkGuncelleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DatabaseHelper sınıfını initialize et
        databaseHelper = DatabaseHelper(this)

        // Otopark sahibi ID'sini SharedPreferences'tan al
        val sharedPreferences = getSharedPreferences("com.talha.otoparkapplication2.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val otoparkSahipId = sharedPreferences.getInt("otoparkSahipId", -1)

        if (otoparkSahipId != -1) {
            // Otopark listesini al
            otoparkList = databaseHelper.getOtoparkListBySahipId(otoparkSahipId)

            if (otoparkList.isNotEmpty()) {
                // Otopark adlarını içeren bir dizi oluştur
                val otoparkAdlari = otoparkList.map { it.otoparkAdi }.toTypedArray()

                // Spinner'a otopark adlarını ekle
                binding.otoparkSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, otoparkAdlari)

                // Spinner'dan seçim yapıldığında tetiklenecek olan işlevi tanımla
                binding.otoparkSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedOtopark = otoparkList[position]
                        // Seçilen otoparkın bilgilerini doldur
                        binding.apply {
                            otoparkAdiEditText.setText(selectedOtopark.otoparkAdi)
                            otoparkAdresiEditText.setText(selectedOtopark.otoparkAdresi)
                            otoparkKapasitesiEditText.setText(selectedOtopark.otoparkKapasitesi.toString())
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Hiçbir şey seçilmediğinde yapılacak işlem yok
                    }
                }
            } else {
                // Kayıtlı otopark bulunamadı
                Toast.makeText(this, "Kayıtlı otoparkınız bulunmamaktadır.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            // Geçersiz otopark sahibi ID'si
            Toast.makeText(this, "Geçersiz otopark sahibi ID'si!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Güncelleme butonuna tıklanınca yapılacak işlemler
        binding.otoparkGuncelleButton.setOnClickListener {
            // Spinner'dan seçilen otoparkın pozisyonunu al
            val selectedPosition = binding.otoparkSpinner.selectedItemPosition
            if (selectedPosition != AdapterView.INVALID_POSITION) {
                // Seçilen otoparkı al
                val selectedOtopark = otoparkList[selectedPosition]
                // Yeni değerleri al
                val yeniAd = binding.otoparkAdiEditText.text.toString().trim()
                val yeniAdres = binding.otoparkAdresiEditText.text.toString().trim()
                val yeniKapasite = binding.otoparkKapasitesiEditText.text.toString().trim()

                // Yeni değerlerin geçerli olup olmadığını kontrol et
                if (validateInput(yeniAd, yeniAdres, yeniKapasite)) {
                    // Otoparkı güncelle
                    val rowsAffected = databaseHelper.otoparkGuncelle(
                        selectedOtopark.otoparkId,
                        yeniAd,
                        yeniAdres,
                        yeniKapasite.toInt()
                    )
                    // Güncelleme başarılıysa
                    if (rowsAffected > 0) {
                        Toast.makeText(this, "Otopark başarıyla güncellendi!", Toast.LENGTH_SHORT).show()
                        // Ekranı kapat
                        finish()
                    } else {
                        // Güncelleme başarısızsa
                        Toast.makeText(this, "Otopark güncellenirken bir hata oluştu!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Lütfen bir otopark seçin!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Giriş verilerinin doğruluğunu kontrol etmek için işlev
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
