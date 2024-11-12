package com.talha.otoparkappllication2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.talha.otoparkappllication2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.YoneticiButton.setOnClickListener {
            val intent = Intent(this, YetkiliGirisActivty::class.java)
            startActivity(intent)
        }

        binding.KullaniciButton.setOnClickListener {
            val intent = Intent(this, KullaniciGirisActivity::class.java)
            startActivity(intent)
        }
    }
}


