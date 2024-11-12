package com.talha.otoparkappllication2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "OtoparkDB"

        // Kullanıcı tablosu
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_NAME = "ad"
        const val COLUMN_SURNAME = "soyad"
        const val COLUMN_PHONE = "telefon"
        const val COLUMN_USER_BALANCE = "bakiye"

        // Yönetici tablosu
        const val TABLE_ADMIN = "admin"

        // OtoparkSahip tablosu
        const val TABLE_OTOPARK_SAHIPI = "otopark_sahip"
        const val COLUMN_OTOPARK_SAHIPI_ID = "otopark_sahip_id"
        const val COLUMN_OTOPARK_SAHIPI_AD = "ad"
        const val COLUMN_OTOPARK_SAHIPI_SOYAD = "soyad"
        const val COLUMN_OTOPARK_SAHIPI_TELEFON = "telefon"
        const val COLUMN_OTOPARK_SAHIPI_EMAIL = "email"
        const val COLUMN_OTOPARK_SAHIPI_PASSWORD = "password"

        // Otopark tablosu
        const val TABLE_OTOPARK = "otopark"
        const val COLUMN_OTOPARK_ID = "otopark_id"
        const val COLUMN_OTOPARK_ADI = "otopark_adi"
        const val COLUMN_OTOPARK_ADRESI = "otopark_adresi"
        const val COLUMN_OTOPARK_KAPASITESI = "otopark_kapasitesi"
        const val COLUMN_KAYDEDEN_OTOPARKSAHIP_ID = "kaydeden_otoparksahip_id"

        // Randevu tablosu
        const val TABLE_RANDEVU = "randevu"
        const val COLUMN_RANDEVU_ID = "randevu_id"
        const val COLUMN_RANDEVU_KULLANICI_ID = "kullanici_id" // Yeni sütun
        const val COLUMN_RANDEVU_OTOPARK_ID = "otopark_id"
        const val COLUMN_RANDEVU_TARIH = "tarih"
        const val COLUMN_RANDEVU_SAAT = "saat"



        private const val CREATE_RANDEVU_TABLE = (
                "CREATE TABLE $TABLE_RANDEVU ($COLUMN_RANDEVU_ID INTEGER PRIMARY KEY, " +
                        "$COLUMN_RANDEVU_KULLANICI_ID INTEGER, " +
                        "$COLUMN_RANDEVU_OTOPARK_ID INTEGER, " +
                        "$COLUMN_RANDEVU_TARIH TEXT, " +
                        "$COLUMN_RANDEVU_SAAT TEXT, " +
                        "FOREIGN KEY($COLUMN_RANDEVU_KULLANICI_ID) REFERENCES $TABLE_USERS($COLUMN_ID), " +
                        "FOREIGN KEY($COLUMN_RANDEVU_OTOPARK_ID) REFERENCES $TABLE_OTOPARK($COLUMN_OTOPARK_ID))"
                )




        private const val CREATE_OTOPARK_SAHIPI_TABLE = (
                "CREATE TABLE $TABLE_OTOPARK_SAHIPI ($COLUMN_OTOPARK_SAHIPI_ID INTEGER PRIMARY KEY, " +
                        "$COLUMN_OTOPARK_SAHIPI_AD TEXT, $COLUMN_OTOPARK_SAHIPI_SOYAD TEXT, " +
                        "$COLUMN_OTOPARK_SAHIPI_TELEFON TEXT, $COLUMN_OTOPARK_SAHIPI_EMAIL TEXT, " +
                        "$COLUMN_OTOPARK_SAHIPI_PASSWORD TEXT)"
                )

        // Tablo oluşturma sorguları
        private const val CREATE_USERS_TABLE = (
                "CREATE TABLE $TABLE_USERS ($COLUMN_ID INTEGER PRIMARY KEY, " +
                        "$COLUMN_EMAIL TEXT, $COLUMN_PASSWORD TEXT, " +
                        "$COLUMN_NAME TEXT, $COLUMN_SURNAME TEXT, $COLUMN_PHONE TEXT, $COLUMN_USER_BALANCE INTEGER DEFAULT 0)"
                )

        private const val CREATE_ADMIN_TABLE = (
                "CREATE TABLE $TABLE_ADMIN ($COLUMN_ID INTEGER PRIMARY KEY, " +
                        "$COLUMN_EMAIL TEXT, $COLUMN_PASSWORD TEXT, " +
                        "$COLUMN_NAME TEXT, $COLUMN_SURNAME TEXT, $COLUMN_PHONE TEXT)"
                )

        private const val CREATE_OTOPARK_TABLE = (
                "CREATE TABLE $TABLE_OTOPARK ($COLUMN_OTOPARK_ID INTEGER PRIMARY KEY, " +
                        "$COLUMN_OTOPARK_ADI TEXT, $COLUMN_OTOPARK_ADRESI TEXT, " +
                        "$COLUMN_OTOPARK_KAPASITESI INTEGER, $COLUMN_KAYDEDEN_OTOPARKSAHIP_ID INTEGER)"
                )



    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_USERS_TABLE)
        db?.execSQL(CREATE_ADMIN_TABLE)
        db?.execSQL(CREATE_OTOPARK_TABLE)
        db?.execSQL(CREATE_RANDEVU_TABLE)
        db?.execSQL(CREATE_OTOPARK_SAHIPI_TABLE)
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_OTOPARK")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RANDEVU")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_OTOPARK_SAHIPI")
        onCreate(db)
    }


    enum class KullaniciTipi {
        NORMAL,
        OTOPARK_SAHIPI,
        YOK
    }





    // Kullanıcı ekleme
    fun kullaniciEkle(ad: String, soyad: String, telefon: String, email: String, sifre: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, ad)
            put(COLUMN_SURNAME, soyad)
            put(COLUMN_PHONE, telefon)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, sifre)
        }

        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }



    fun otoparkEkle(otoparkAdi: String, otoparkAdresi: String, otoparkKapasitesi: Int, kaydedenOtoparkSahipId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_OTOPARK_ADI, otoparkAdi)
            put(COLUMN_OTOPARK_ADRESI, otoparkAdresi)
            put(COLUMN_OTOPARK_KAPASITESI, otoparkKapasitesi)
            put(COLUMN_KAYDEDEN_OTOPARKSAHIP_ID, kaydedenOtoparkSahipId) // Güncellenmiş sütuna değeri ekle
        }

        val id = db.insert(TABLE_OTOPARK, null, values)
        db.close()
        return id
    }

    fun getOtoparkList(): List<OtoparkModel> {
        val otoparkList = mutableListOf<OtoparkModel>()
        val db = this.readableDatabase

        val projection = arrayOf(
            COLUMN_OTOPARK_ID,
            COLUMN_OTOPARK_ADI,
            COLUMN_OTOPARK_ADRESI,
            COLUMN_OTOPARK_KAPASITESI
        )

        val cursor = db.query(
            TABLE_OTOPARK,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val otoparkId = cursor.getLong(cursor.getColumnIndex(COLUMN_OTOPARK_ID))
            val otoparkAdi = cursor.getString(cursor.getColumnIndex(COLUMN_OTOPARK_ADI))
            val otoparkAdresi = cursor.getString(cursor.getColumnIndex(COLUMN_OTOPARK_ADRESI))
            val otoparkKapasitesi = cursor.getInt(cursor.getColumnIndex(COLUMN_OTOPARK_KAPASITESI))

            val otopark = OtoparkModel(otoparkId, otoparkAdi, otoparkAdresi, otoparkKapasitesi)
            otoparkList.add(otopark)
        }

        cursor.close()
        db.close()

        return otoparkList
    }

    // Otopark güncelleme
    fun otoparkGuncelle(otoparkId: Long, updatedOtoparkAdi: String, updatedOtoparkAdresi: String, updatedOtoparkKapasitesi: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_OTOPARK_ADI, updatedOtoparkAdi)
            put(COLUMN_OTOPARK_ADRESI, updatedOtoparkAdresi)
            put(COLUMN_OTOPARK_KAPASITESI, updatedOtoparkKapasitesi)
        }

        val rowsAffected = db.update(
            TABLE_OTOPARK,
            values,
            "$COLUMN_OTOPARK_ID = ?",
            arrayOf(otoparkId.toString())
        )

        db.close()
        return rowsAffected
    }

    // Otopark silme
    fun otoparkSil(otoparkId: Long): Int {
        val db = this.writableDatabase
        val rowsAffected = db.delete(
            TABLE_OTOPARK,
            "$COLUMN_OTOPARK_ID = ?",
            arrayOf(otoparkId.toString())
        )

        db.close()
        return rowsAffected
    }

    // Randevu ekleme
    fun randevuEkle(otoparkId: Long, kullaniciId: Int, tarih: String, saat: String): Long {
        val db = this.writableDatabase

        try {
            val values = ContentValues().apply {
                put(COLUMN_RANDEVU_OTOPARK_ID, otoparkId)
                put(COLUMN_RANDEVU_KULLANICI_ID, kullaniciId)
                put(COLUMN_RANDEVU_TARIH, tarih)
                put(COLUMN_RANDEVU_SAAT, saat)
            }

            return db.insertOrThrow(TABLE_RANDEVU, null, values)
        } catch (e: Exception) {
            // Hata durumunda buraya düşecek
            e.printStackTrace()
            return -1L
        } finally {
            db.close()
        }
    }




    fun getTelefonFromKullaniciId(kullaniciId: Long): String? {
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_PHONE) // Telefon sütununu projeksiyona ekleyin
        val selection = "$COLUMN_ID = ?" // Kullanıcı ID'sine göre seçim yapın
        val selectionArgs = arrayOf(kullaniciId.toString())
        val cursor = db.query(
            TABLE_USERS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        var telefon: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            telefon = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)) // Telefon numarasını doğru sütundan alın
        }
        cursor?.close()
        db.close()
        return telefon
    }



    fun getOtoparkIdByOwnerId(ownerId: Int): Long {
        val db = this.readableDatabase
        val cursor = db.query(
            "otopark",
            arrayOf("otopark_id"),
            "kaydeden_otoparksahip_id = ?",
            arrayOf(ownerId.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val otoparkId = cursor.getLong(cursor.getColumnIndexOrThrow("otopark_id"))
            cursor.close()
            otoparkId
        } else {
            cursor.close()
            -1L
        }
    }






    fun getRandevuList(): List<RandevuModel> {
        val randevuList = mutableListOf<RandevuModel>()
        val db = this.readableDatabase

        val projection = arrayOf(
            COLUMN_RANDEVU_ID,
            COLUMN_RANDEVU_OTOPARK_ID,
            COLUMN_RANDEVU_TARIH,
            COLUMN_RANDEVU_SAAT
        )

        val cursor = db.query(
            TABLE_RANDEVU,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val randevuId = cursor.getLong(cursor.getColumnIndex(COLUMN_RANDEVU_ID))
            val otoparkId = cursor.getLong(cursor.getColumnIndex(COLUMN_RANDEVU_OTOPARK_ID))
            val tarih = cursor.getString(cursor.getColumnIndex(COLUMN_RANDEVU_TARIH))
            val saat = cursor.getString(cursor.getColumnIndex(COLUMN_RANDEVU_SAAT))
            val kullaniciId = getKullaniciIdFromRandevu(randevuId)
            val otoparkAdi = getOtoparkAdi(otoparkId) ?: ""
            val telefon = getTelefonFromKullaniciId(kullaniciId) ?: ""

            val randevu = RandevuModel(randevuId, otoparkId, kullaniciId, tarih, saat, otoparkAdi, telefon)
            randevuList.add(randevu)
        }

        cursor.close()
        db.close()

        return randevuList
    }


    private fun getKullaniciIdFromRandevu(randevuId: Long): Long {
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_RANDEVU_OTOPARK_ID)
        val selection = "$COLUMN_RANDEVU_ID = ?"
        val selectionArgs = arrayOf(randevuId.toString())
        val cursor = db.query(
            TABLE_RANDEVU,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var kullaniciId: Long = -1

        if (cursor.moveToFirst()) {
            kullaniciId = cursor.getLong(cursor.getColumnIndex(COLUMN_RANDEVU_OTOPARK_ID))
        }

        cursor.close()
        db.close()

        return kullaniciId
    }

    fun getOtoparkSahibiByUserId(otoparkSahipId: Int): OtoparkSahibi? {
        val db = this.readableDatabase
        val cursor = db.query(
            "otopark_sahip",
            arrayOf("otopark_sahip_id", "ad", "soyad", "telefon", "email", "password"),
            "otopark_sahip_id = ?",
            arrayOf(otoparkSahipId.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val otoparkSahibi = OtoparkSahibi(
                ownerId = cursor.getInt(cursor.getColumnIndexOrThrow("otopark_sahip_id")),
                ad = cursor.getString(cursor.getColumnIndexOrThrow("ad")),
                soyad = cursor.getString(cursor.getColumnIndexOrThrow("soyad")),
                telefon = cursor.getString(cursor.getColumnIndexOrThrow("telefon")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            )
            cursor.close()
            otoparkSahibi
        } else {
            cursor.close()
            null
        }
    }




    fun getOtoparkAdi(otoparkId: Long): String {
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_OTOPARK_ADI)
        val selection = "$COLUMN_OTOPARK_ID = ?"
        val selectionArgs = arrayOf(otoparkId.toString())
        val cursor = db.query(
            TABLE_OTOPARK,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var otoparkAdi = ""

        if (cursor.moveToFirst()) {
            otoparkAdi = cursor.getString(cursor.getColumnIndex(COLUMN_OTOPARK_ADI))
        }

        cursor.close()
        db.close()

        return otoparkAdi
    }

    fun getKullaniciBilgileri(kullaniciId: Int): Kullanici? {
        val db = this.readableDatabase
        val projection = arrayOf(
            COLUMN_ID, // ID sütunu eklendi
            COLUMN_NAME,
            COLUMN_SURNAME,
            COLUMN_PHONE,
            COLUMN_EMAIL,
            COLUMN_PASSWORD
        )
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(kullaniciId.toString())
        val cursor = db.query(
            TABLE_USERS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var kullanici: Kullanici? = null

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)) // ID'yi al
            val ad = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val soyad = cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME))
            val telefon = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val sifre = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            kullanici = Kullanici(id, ad, soyad, telefon, email, sifre) // ID'yi de Kullanici nesnesine ekle
        }

        cursor.close()
        db.close()

        return kullanici
    }



    fun getOtoparkSahipId(email: String): Int? {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_OTOPARK_SAHIPI_ID FROM $TABLE_OTOPARK_SAHIPI WHERE $COLUMN_OTOPARK_SAHIPI_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        var otoparkSahipId: Int? = null

        if (cursor.moveToFirst()) {
            otoparkSahipId = cursor.getInt(cursor.getColumnIndex(COLUMN_OTOPARK_SAHIPI_ID))
        }

        cursor.close()
        db.close()

        return otoparkSahipId
    }


    fun kullaniciyiGetirVeTipiKontrolEt(email: String, sifre: String): Pair<Kullanici?, KullaniciTipi> {
        val db = this.readableDatabase
        val projection = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_SURNAME,
            COLUMN_PHONE,
            COLUMN_PASSWORD
        )
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, sifre)
        val cursor = db.query(
            TABLE_USERS,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var kullanici: Kullanici? = null
        var kullaniciTipi = KullaniciTipi.YOK

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val ad = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val soyad = cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME))
            val telefon = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))
            val sifre = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            kullanici = Kullanici(id, ad, soyad, telefon, email, sifre)

            // Kullanıcıyı bulduktan sonra kullanıcı tipini belirle
            kullaniciTipi = if (email.contains("@otopark.com")) {
                KullaniciTipi.OTOPARK_SAHIPI
            } else {
                KullaniciTipi.NORMAL
            }
        } else {
            // Kullanıcı normal tabloda bulunamadı, otopark sahibi tablosuna bak
            val otoparkSahibiProjection = arrayOf(COLUMN_OTOPARK_SAHIPI_ID)
            val otoparkSahibiSelection = "$COLUMN_OTOPARK_SAHIPI_EMAIL = ? AND $COLUMN_OTOPARK_SAHIPI_PASSWORD = ?"
            val otoparkSahibiCursor = db.query(
                TABLE_OTOPARK_SAHIPI,
                otoparkSahibiProjection,
                otoparkSahibiSelection,
                selectionArgs,
                null,
                null,
                null
            )

            if (otoparkSahibiCursor.moveToFirst()) {
                // Kullanıcı otopark sahibi tablosunda bulundu, bu bir otopark sahibidir
                val id = otoparkSahibiCursor.getInt(otoparkSahibiCursor.getColumnIndex(COLUMN_OTOPARK_SAHIPI_ID))
                kullanici = Kullanici(id, "", "", "", email, sifre)
                kullaniciTipi = KullaniciTipi.OTOPARK_SAHIPI
            }

            otoparkSahibiCursor.close()
        }

        cursor.close()
        db.close()

        return Pair(kullanici, kullaniciTipi)
    }






    // Kullanıcı bilgilerini güncelleme
    fun updateKullaniciBilgileri(ad: String, soyad: String, telefon: String, email: String, sifre: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, ad)
            put(COLUMN_SURNAME, soyad)
            put(COLUMN_PHONE, telefon)
            put(COLUMN_EMAIL, email)
            // Yalnızca kullanıcı şifresini güncelle
            put(COLUMN_PASSWORD, sifre)
        }

        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val rowsAffected = db.update(
            TABLE_USERS,
            values,
            selection,
            selectionArgs
        )

        db.close()
        return rowsAffected > 0
    }

    fun kullaniciBakiyesiniGuncelle(kullaniciId: Int, yeniBakiye: Int): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_BALANCE, yeniBakiye)
        }

        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(kullaniciId.toString())

        val rowsAffected = db.update(
            TABLE_USERS,
            values,
            selection,
            selectionArgs
        )

        db.close()
        return rowsAffected > 0
    }

    fun ekleOtoparkSahibi(kullanici: Kullanici): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, kullanici.ad)
            put(COLUMN_SURNAME, kullanici.soyad)
            put(COLUMN_PHONE, kullanici.telefon)
            put(COLUMN_EMAIL, kullanici.email)
            put(COLUMN_PASSWORD, kullanici.sifre)
        }

        val id = db.insert(TABLE_OTOPARK_SAHIPI, null, values)
        db.close()
        return id != -1L
    }

    fun kullaniciyiSil(kullaniciId: Int): Boolean {
        val db = this.writableDatabase
        val rowsAffected = db.delete(
            TABLE_USERS,
            "$COLUMN_ID = ?",
            arrayOf(kullaniciId.toString())
        )

        db.close()
        return rowsAffected > 0
    }

    fun otoparkVarMi(otoparkAdi: String, otoparkAdresi: String): Boolean {
        val db = this.readableDatabase
        val projection = arrayOf(COLUMN_OTOPARK_ID)
        val selection = "$COLUMN_OTOPARK_ADI = ? AND $COLUMN_OTOPARK_ADRESI = ?"
        val selectionArgs = arrayOf(otoparkAdi, otoparkAdresi)
        val cursor = db.query(
            TABLE_OTOPARK,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val varMi = cursor.count > 0
        cursor.close()
        db.close()

        return varMi
    }

    fun getOtoparkListBySahipId(sahipId: Int): List<OtoparkModel> {
        val otoparkList = mutableListOf<OtoparkModel>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_OTOPARK,
            null,
            "$COLUMN_KAYDEDEN_OTOPARKSAHIP_ID = ?",
            arrayOf(sahipId.toString()),
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val otopark = OtoparkModel(
                otoparkId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_OTOPARK_ID)),
                otoparkAdi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OTOPARK_ADI)),
                otoparkAdresi = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OTOPARK_ADRESI)),
                otoparkKapasitesi = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_OTOPARK_KAPASITESI))
            )
            otoparkList.add(otopark)
        }
        cursor.close()
        return otoparkList
    }

    fun getRandevuListForOtopark(otoparkId: Long): List<RandevuModel> {
        val randevuList = mutableListOf<RandevuModel>()

        // Sorgunun oluşturulması
        val selectQuery = """
        SELECT r.$COLUMN_RANDEVU_ID, r.$COLUMN_RANDEVU_KULLANICI_ID, r.$COLUMN_RANDEVU_OTOPARK_ID, 
        r.$COLUMN_RANDEVU_TARIH, r.$COLUMN_RANDEVU_SAAT, o.$COLUMN_OTOPARK_ADI, o.$COLUMN_OTOPARK_ADRESI, 
        u.$COLUMN_PHONE 
        FROM $TABLE_RANDEVU r 
        INNER JOIN $TABLE_OTOPARK o ON r.$COLUMN_RANDEVU_OTOPARK_ID = o.$COLUMN_OTOPARK_ID 
        INNER JOIN $TABLE_USERS u ON r.$COLUMN_RANDEVU_KULLANICI_ID = u.$COLUMN_ID 
        WHERE r.$COLUMN_RANDEVU_OTOPARK_ID = ?
    """

        // Veritabanı işlemleri
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(otoparkId.toString()))

        cursor.use { cursor ->
            // Cursor'ın başına git
            if (cursor.moveToFirst()) {
                // Tüm satırları dolaş
                do {
                    // Verileri al
                    val randevuId = cursor.getLong(cursor.getColumnIndex(COLUMN_RANDEVU_ID))
                    val kullaniciId = cursor.getLong(cursor.getColumnIndex(COLUMN_RANDEVU_KULLANICI_ID))
                    val otoparkId = cursor.getLong(cursor.getColumnIndex(COLUMN_RANDEVU_OTOPARK_ID))
                    val tarih = cursor.getString(cursor.getColumnIndex(COLUMN_RANDEVU_TARIH))
                    val saat = cursor.getString(cursor.getColumnIndex(COLUMN_RANDEVU_SAAT))
                    val otoparkAdi = cursor.getString(cursor.getColumnIndex(COLUMN_OTOPARK_ADI))
                    val telefon = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE))

                    // RandevuModel oluştur ve listeye ekle
                    val randevu = RandevuModel(randevuId, otoparkId, kullaniciId, tarih, saat, otoparkAdi, telefon)
                    randevuList.add(randevu)
                } while (cursor.moveToNext())
            }
        }

        // Cursor ve veritabanını kapat
        cursor.close()
        db.close()

        // Randevu listesini döndür
        return randevuList
    }





}