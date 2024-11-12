package com.talha.otoparkappllication2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityYetkiliArayuzActivtyBinding

class YetkiliArayuzActivty : AppCompatActivity() {

    private lateinit var binding: ActivityYetkiliArayuzActivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYetkiliArayuzActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonYoneticiOtoparkEkle.setOnClickListener {
            val intent = Intent(this, YetkiliOtoparkKaydetActivty::class.java)
            startActivityForResult(intent, REQUEST_OTOPARK_KAYDET)
        }

        binding.buttonYoneticiOtoparkListele.setOnClickListener {
            val intent = Intent(this, YetkiliOtoparkListeleActivty::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OTOPARK_KAYDET && resultCode == RESULT_OK) {
            // Otopark başarıyla kaydedildiğinde buraya gelecek işlemleri yapabilirsiniz
            Toast.makeText(this, "Otopark başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REQUEST_OTOPARK_KAYDET = 1
    }
}
