package com.talha.otoparkappllication2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talha.otoparkappllication2.databinding.ItemOtoparkBinding

class OtoparkAdapter(
    private val otoparkList: List<OtoparkModel>,
    private val onItemClickListener: (OtoparkModel) -> Unit
) : RecyclerView.Adapter<OtoparkAdapter.OtoparkViewHolder>() {

    inner class OtoparkViewHolder(val binding: ItemOtoparkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtoparkViewHolder {
        val binding = ItemOtoparkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OtoparkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OtoparkViewHolder, position: Int) {
        val otopark = otoparkList[position]

        // Verileri ViewHolder içindeki binding öğelerine atayabilirsin.
        holder.binding.apply {
            otoparkAdiTextView.text = otopark.otoparkAdi
            otoparkAdresiTextView.text = otopark.otoparkAdresi
            otoparkKapasitesiTextView.text = otopark.otoparkKapasitesi.toString()

            // Tıklama olayını burada tetikleyin
            root.setOnClickListener { onItemClickListener.invoke(otopark) }
        }
    }

    override fun getItemCount(): Int {
        return otoparkList.size
    }
}