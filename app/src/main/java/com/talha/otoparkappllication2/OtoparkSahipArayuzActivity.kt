package com.talha.otoparkappllication2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityOtoparkSahipArayuzBinding

class OtoparkSahipArayuzActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtoparkSahipArayuzBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtoparkSahipArayuzBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.OSotoparkEkleButton.setOnClickListener {
            startActivity(Intent(this, OSotoparkEkleActivity::class.java))
        }

        binding.OSrandevularButton.setOnClickListener {
            startActivity(Intent(this, OSrandevuListesiActivity::class.java))
        }

        binding.OSotoparkGuncelleButton.setOnClickListener {
            startActivity(Intent(this, OSotoparkGuncelleActivity::class.java))
        }
    }
}
