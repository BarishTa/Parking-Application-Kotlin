package com.talha.otoparkappllication2

data class Kullanici(
    val id: Int, // Kullanıcıya ait benzersiz kimlik numarası (id) eklendi
    val ad: String,
    val soyad: String,
    val telefon: String,
    val email: String,
    val sifre: String
)
