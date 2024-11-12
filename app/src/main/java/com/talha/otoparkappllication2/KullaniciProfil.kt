package com.talha.otoparkappllication2

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityKullaniciProfilBinding

class KullaniciProfil : AppCompatActivity() {
    private lateinit var binding: ActivityKullaniciProfilBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var adEditText: EditText
    private lateinit var soyadEditText: EditText
    private lateinit var telefonEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var sifreEditText: TextView
    private lateinit var bilgiGuncelleButton: Button
    private var currentUser: Kullanici? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKullaniciProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)

        adEditText = binding.etAd
        soyadEditText = binding.etSoyad
        telefonEditText = binding.etTelefon
        emailEditText = binding.etEmail
        sifreEditText = binding.etSifre
        bilgiGuncelleButton = binding.btnBilgiGuncelle

        sharedPreferences = getSharedPreferences("com.talha.otoparkapplication2.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)

        // Kullanıcı ID'sini SharedPreferences'ten al
        val kullaniciId = sharedPreferences.getInt("kullaniciId", -1)

        // Eğer kullanıcı ID'si geçerli ise
        if (kullaniciId != -1) {
            // Kullanıcının bilgilerini ID'ye göre veritabanından al
            currentUser = databaseHelper.getKullaniciBilgileri(kullaniciId)
            if (currentUser != null) {
                // Kullanıcının bilgilerini göster
                adEditText.setText(currentUser!!.ad)
                soyadEditText.setText(currentUser!!.soyad)
                telefonEditText.setText(currentUser!!.telefon)
                emailEditText.setText(currentUser!!.email)
                sifreEditText.text = currentUser!!.sifre
            } else {
                Toast.makeText(this, "Kullanıcı bilgileri alınamadı.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Geçersiz kullanıcı ID.", Toast.LENGTH_SHORT).show()
        }

        // Bilgi güncelle butonuna tıklama işlemi
        bilgiGuncelleButton.setOnClickListener {
            val randomSecurityCode = generateRandomSecurityCode()
            showSecurityCodeDialog(randomSecurityCode)
        }
    }

    private fun generateRandomSecurityCode(): String {
        return (100000..999999).random().toString()
    }

    private fun showSecurityCodeDialog(securityCode: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Güvenlik Kodu")
        builder.setMessage("Güvenlik Kodunuz: $securityCode")
        val editText = EditText(this)
        builder.setView(editText)

        builder.setPositiveButton("Onayla") { dialog: DialogInterface, _: Int ->
            val enteredCode = editText.text.toString()
            if (enteredCode == securityCode) {
                editUserInfo()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Güvenlik kodu hatalı. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show()
            }
        }

        builder.show()
    }

    private fun editUserInfo() {
        // Bilgileri güncelleme işlemi
        val ad = adEditText.text.toString()
        val soyad = soyadEditText.text.toString()
        val telefon = telefonEditText.text.toString()
        val email = emailEditText.text.toString()
        val sifre = sifreEditText.text.toString()

        val updated = databaseHelper.updateKullaniciBilgileri(ad, soyad, telefon, email, sifre)
        if (updated) {
            navigateToMainActivity()
            Toast.makeText(this, "Şifreniz Başarı ile güncellendi.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Bilgileriniz güncellenirken bir hata oluştu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

