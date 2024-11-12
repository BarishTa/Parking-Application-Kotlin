package com.talha.otoparkappllication2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.talha.otoparkappllication2.databinding.ActivityKullaniciArayuzActivtyBinding

class KullaniciArayuzActivty : AppCompatActivity() {
    private lateinit var binding: ActivityKullaniciArayuzActivtyBinding
    private lateinit var veritabaniYardimcisi: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences // SharedPreferences tanımı eklendi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKullaniciArayuzActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        veritabaniYardimcisi = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("com.talha.otoparkapplication2.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE) // SharedPreferences tanımı eklendi

        // Otopark Randevu butonuna tıklama işlemleri
        binding.OtoparkRandevubtnn.setOnClickListener {
            // Otopark Randevu Activity'ye geçiş yap
            val intent = Intent(this, OtoparkRandevuActivty::class.java)
            startActivity(intent)
        }

        binding.randevulistbtn.setOnClickListener {
            // Randevu Listesi Activity'ye geçiş yap
            val intent = Intent(this, RandevuListemActivty::class.java)
            startActivity(intent)
        }

        binding.KlncPrflButton.setOnClickListener {
            // Kullanıcı Profil Activity'ye geçiş yap
            val intent = Intent(this, KullaniciProfil::class.java)
            startActivity(intent)
        }

        // Üyelik Satın Al butonuna tıklama işlemleri
        binding.Uyelikbtn.setOnClickListener {
            uyelikDialogunuGoster()
        }
    }

    private fun uyelikDialogunuGoster() {
        val secenekler = arrayOf("Aylık (1000 TL)", "Yıllık (10000 TL)")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Üyelik Seç")
            .setItems(secenekler) { dialog, which ->
                val secilenSecenek = secenekler[which]
                val fiyat = if (which == 0) 1000 else 10000
                val onayMesaji =
                    " ${fiyat}TL Ödeyerek $secilenSecenek Otopark Sahibi Üyeliği Almak İstediğinize Emin misiniz?"

                val onayDialog = AlertDialog.Builder(this)
                    .setTitle("Üyelik Satın Alma")
                    .setMessage(onayMesaji)
                    .setPositiveButton("Evet") { dialog, _ ->
                        // Kullanıcı id'sini SharedPreferences'ten al
                        val kullaniciId = sharedPreferences.getInt("kullaniciId", -1)
                        if (kullaniciId != -1) {
                            // Bakiye kontrolü yap
                            val kullaniciBakiyesi = kullaniciBakiyesiniAl() // Kullanıcı bakiyesini al
                            if (kullaniciBakiyesi >= fiyat) {
                                // Üyelik satın alındı, işlemleri gerçekleştir
                                uyelikSatınAlmaIslemleriniGerçekleştir(fiyat, kullaniciId, this)
                            } else {
                                // Kullanıcının bakiyesi yetersiz, uyarı ver
                                AlertDialog.Builder(this)
                                    .setTitle("Bakiye Yetersiz")
                                    .setMessage("Üyelik satın almak için yeterli bakiyeniz bulunmamaktadır.")
                                    .setPositiveButton("Tamam", null)
                                    .show()
                            }
                        } else {
                            // Kullanıcı id'si bulunamadı, hata göster
                            Toast.makeText(this, "Kullanıcı id'si bulunamadı", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Hayır", null)
                    .create()

                onayDialog.show()
            }
        val dialog = builder.create()
        dialog.show()
    }


    private fun kullaniciBakiyesiniAl(): Int {
        // Kullanıcının bakiyesini burada al ve döndür
        // Örnek olarak 5000 TL olduğunu varsayalım
        return 5000
    }

    private fun uyelikSatınAlmaIslemleriniGerçekleştir(fiyat: Int, kullaniciId: Int, context: Context) {
        // Kullanıcı bakiyesinden ücreti düş ve üyelik işlemlerini gerçekleştir
        // Bu kısımda bakiyeden ücreti düş ve üyelik işlemlerini gerçekleştir
        AlertDialog.Builder(context)
            .setTitle("Üyelik Satın Alma")
            .setMessage("Üyeliğiniz başarıyla satın alınmıştır.")
            .setPositiveButton("Tamam") { _, _ ->
                // Üyelik satın alındıktan sonra KullaniciGirisActivity sayfasına yönlendir
                val intent = Intent(context, KullaniciGirisActivity::class.java)
                context.startActivity(intent)
                // Aktiviteyi kapat
                (context as Activity).finish()
            }
            .show()

        // Ücreti bakiyeden düş
        val kullaniciBakiyesi = kullaniciBakiyesiniAl()
        val yeniBakiye = kullaniciBakiyesi - fiyat
        // Bakiyeyi güncelle (burada gerçek bir işlem yapılmalıdır, bu sadece örnek amaçlıdır)
        kullaniciBakiyesiniGuncelle(context, kullaniciId, yeniBakiye)
    }


    private fun kullaniciBakiyesiniGuncelle(context: Context, kullaniciId: Int, yeniBakiye: Int) {
        val veritabaniYardimcisi = DatabaseHelper(context)

        // Kullanıcı bakiyesini güncelleme işlemi
        val bakiyeGuncellendi = veritabaniYardimcisi.kullaniciBakiyesiniGuncelle(kullaniciId, yeniBakiye)

        if (bakiyeGuncellendi) {
            // Bakiye başarıyla güncellendi
            println("Kullanıcı bakiyesi başarıyla güncellendi. Kullanıcı ID: $kullaniciId, Yeni bakiye: $yeniBakiye TL")

            // Kullanıcı bakiyesi başarıyla güncellendiği için, kullanıcıyı OtoparkSahip tablosuna ekle
            val kullaniciBilgileri = veritabaniYardimcisi.getKullaniciBilgileri(kullaniciId)
            if (kullaniciBilgileri != null) {
                // Kullanıcı bilgileri başarıyla alındı, OtoparkSahip tablosuna ekle
                val eklendi = veritabaniYardimcisi.ekleOtoparkSahibi(kullaniciBilgileri)
                if (eklendi) {
                    // Kullanıcı başarıyla OtoparkSahip tablosuna eklendi, şimdi users tablosundan kaldır
                    val kullaniciKaldırildi = veritabaniYardimcisi.kullaniciyiSil(kullaniciId)
                    if (kullaniciKaldırildi) {
                        println("Kullanıcı başarıyla OtoparkSahip tablosuna eklendi ve users tablosundan kaldırıldı.")
                    } else {
                        println("Kullanıcı OtoparkSahip tablosuna eklenirken bir hata oluştu.")
                    }
                } else {
                    println("Kullanıcı OtoparkSahip tablosuna eklenirken bir hata oluştu.")
                }
            } else {
                println("Kullanıcı bilgileri alınamadı, işlem tamamlanamadı.")
            }
        } else {
            // Bakiye güncelleme başarısız oldu
            println("Kullanıcı bakiyesi güncellenirken bir hata oluştu.")
        }
    }
}
