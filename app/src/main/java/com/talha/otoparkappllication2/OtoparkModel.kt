// OtoparkModel.kt

package com.talha.otoparkappllication2

data class OtoparkModel(
    val otoparkId: Long,
    val otoparkAdi: String,
    val otoparkAdresi: String,
    val otoparkKapasitesi: Int
) {
    override fun toString(): String {
        return """
            Otopark Adı: $otoparkAdi
            Otopark Adresi: $otoparkAdresi
            Otopark Kapasitesi: $otoparkKapasitesi
        """.trimIndent()
    }
}
