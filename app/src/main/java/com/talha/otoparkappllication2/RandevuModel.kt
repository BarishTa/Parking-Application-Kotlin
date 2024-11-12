package com.talha.otoparkappllication2

data class RandevuModel(
    val randevuId: Long,
    val otoparkId: Long,
    val kullaniciId: Long,
    val tarih: String,
    val saat: String,
    val otoparkAdi: String,
    val telefon: String // Yeni eklenen özellik
)