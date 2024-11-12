package com.talha.otoparkappllication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.talha.otoparkappllication2.databinding.ActivityYetkiliGirisActivtyBinding

class YetkiliGirisActivty : AppCompatActivity() {

    private lateinit var binding: ActivityYetkiliGirisActivtyBinding
    lateinit var YoneticiEmailAddress: EditText
    lateinit var YoneticiPassword: EditText
    lateinit var YoneticiGirisButton: Button

    // Admin bilgileri
    val adminUsername = "yntc@hotmail.com"
    val adminPassword = "123yntc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYetkiliGirisActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        YoneticiEmailAddress = binding.YoneticiEmailAddress
        YoneticiPassword = binding.YoneticiPassword
        YoneticiGirisButton = binding.YoneticiGirisButton

        binding.YoneticiGirisButton.setOnClickListener {
            admin_kontrol()
        }
    }

    private fun admin_kontrol() {
        val adminUsernameInput = YoneticiEmailAddress.text.toString()
        val adminPasswordInput = YoneticiPassword.text.toString()

        if (adminUsernameInput == adminUsername && adminPasswordInput == adminPassword) {
            Toast.makeText(this, "Giriş Başarılı!!", Toast.LENGTH_SHORT).show()
            val yetkiliarayuzintent = Intent(applicationContext, YetkiliArayuzActivty::class.java)
            startActivity(yetkiliarayuzintent)
        } else {
            Toast.makeText(this, "Giriş Hatalı!!", Toast.LENGTH_SHORT).show()
        }
    }
}

