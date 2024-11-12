package com.talha.otoparkappllication2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityKullaniciKayitOlAcitivtyBinding

class KullaniciKayitOlAcitivty : AppCompatActivity() {
    private lateinit var binding: ActivityKullaniciKayitOlAcitivtyBinding
    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var etAd: EditText
    private lateinit var etSoyad: EditText
    private lateinit var etTelefon: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSifre: EditText
    private lateinit var k_kayitbtn: Button
    private lateinit var tvPasswordStrength: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityKullaniciKayitOlAcitivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)

        etAd = binding.etAd
        etSoyad = binding.etSoyad
        etTelefon = binding.etTelefon
        etEmail = binding.etEmail
        etSifre = binding.etSifre
        k_kayitbtn = binding.kKayitbtn
        tvPasswordStrength = binding.tvPasswordStrength

        etSifre.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                val passwordStrength = getPasswordStrength(password)
                setStrengthText(passwordStrength)
            }
        })

        etTelefon.addTextChangedListener(object : TextWatcher {
            var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                val input = s.toString().replace(" ", "")
                if (input.length <= 11) { // 11 haneli telefon numarası kontrolü
                    isFormatting = true
                    val formatted = StringBuilder()

                    for (i in input.indices) {
                        if (i == 4 || i == 7 || i == 9) { // Boşluklar burada ekleniyor
                            formatted.append(" ")
                        }
                        formatted.append(input[i])

                        if (i == 10) { // Son 2 haneden sonra kontrol
                            etTelefon.isEnabled = input.length <= 10 // Son 2 haneden sonra sadece boşluk girilebilir
                            if (input.length == 11) {
                                etTelefon.clearFocus() // EditText'in odaklanmasını kaldır
                                etTelefon.isFocusableInTouchMode = true // EditText tıklanabilir hale getirilir
                                etEmail.requestFocus() // Şifre EditText'ine odaklan
                            }
                        }
                    }

                    etTelefon.setText(formatted.toString())
                    etTelefon.setSelection(formatted.length)
                    isFormatting = false
                }
            }
        })


        k_kayitbtn.setOnClickListener {
            val ad = etAd.text.toString()
            val soyad = etSoyad.text.toString()
            val telefon = etTelefon.text.toString()
            val email = etEmail.text.toString()
            val sifre = etSifre.text.toString()

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Geçerli bir e-posta adresi girin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val yeniKullaniciId = databaseHelper.kullaniciEkle(ad, soyad, telefon, email, sifre)

            if (yeniKullaniciId != -1L) {
                // Kullanıcı başarıyla eklendi
                Toast.makeText(this, "Kullanıcı başarıyla kaydedildi.", Toast.LENGTH_SHORT).show()

            } else {
                // Kullanıcı eklenirken hata oluştu
                Toast.makeText(this, "Kullanıcı eklenirken bir hata oluştu.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPasswordStrength(password: String): PasswordStrength {
        return when {
            password.length < 8 -> PasswordStrength.WEAK
            password.any { it.isDigit() } && password.any { it.isLetter() } -> PasswordStrength.STRONG
            else -> PasswordStrength.MEDIUM
        }
    }

    private fun setStrengthText(passwordStrength: PasswordStrength) {
        tvPasswordStrength.text = "Şifre gücü: ${passwordStrength.name}"
        when (passwordStrength) {
            PasswordStrength.WEAK -> tvPasswordStrength.setTextColor(resources.getColor(android.R.color.holo_red_dark))
            PasswordStrength.MEDIUM -> tvPasswordStrength.setTextColor(resources.getColor(android.R.color.holo_orange_dark))
            PasswordStrength.STRONG -> tvPasswordStrength.setTextColor(resources.getColor(android.R.color.holo_green_dark))
        }
    }
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
