package com.talha.otoparkappllication2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityKullaniciGirisBinding

class KullaniciGirisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKullaniciGirisBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var KullaniciGirisYapButton: Button
    private lateinit var KullaniciKayitButton: Button

    private lateinit var KullaniciEmailAddress: EditText
    private lateinit var KullaniciPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKullaniciGirisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("com.talha.otoparkapplication2.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)

        KullaniciGirisYapButton = binding.KullaniciGirisYapButton
        KullaniciKayitButton = binding.KullaniciKayitButton

        KullaniciEmailAddress = binding.KullaniciEmailAddress
        KullaniciPassword = binding.KullaniciPassword

        KullaniciGirisYapButton.setOnClickListener {
            val email = KullaniciEmailAddress.text.toString()
            val password = KullaniciPassword.text.toString()

            val (kullanici, kullaniciTipi) = databaseHelper.kullaniciyiGetirVeTipiKontrolEt(email, password)

            if (kullanici != null) {
                val editor = sharedPreferences.edit()

                // Kullanıcı tipini kontrol et ve id'yi sakla
                when (kullaniciTipi) {
                    DatabaseHelper.KullaniciTipi.NORMAL -> {
                        editor.putInt("kullaniciId", kullanici.id)
                        editor.remove("otoparkSahipId") // Diğer değeri temizle
                        editor.apply()

                        val intent = Intent(this, KullaniciArayuzActivty::class.java)
                        startActivity(intent)
                        finish()
                    }
                    DatabaseHelper.KullaniciTipi.OTOPARK_SAHIPI -> {
                        val otoparkSahipId = databaseHelper.getOtoparkSahipId(email)
                        if (otoparkSahipId != null) {
                            editor.putInt("otoparkSahipId", otoparkSahipId)
                            editor.remove("kullaniciId") // Diğer değeri temizle
                            editor.apply()

                            val intent = Intent(this, OtoparkSahipArayuzActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Otopark sahibi bilgisi bulunamadı.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        // Beklenmeyen bir durumla karşılaşıldığında buraya girilecek
                        Toast.makeText(this, "Bilinmeyen kullanıcı tipi.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Kullanıcı adı veya şifre hatalı.", Toast.LENGTH_SHORT).show()
            }
        }

        KullaniciKayitButton.setOnClickListener {
            val intent = Intent(this, KullaniciKayitOlAcitivty::class.java)
            startActivity(intent)
        }
    }
}
