package com.talha.otoparkappllication2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RandevuAdapter(private val randevuList: List<RandevuModel>) : RecyclerView.Adapter<RandevuAdapter.RandevuViewHolder>() {

    inner class RandevuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val adSoyadTextView: TextView = itemView.findViewById(R.id.adSoyadTextView)
        val telefonTextView: TextView = itemView.findViewById(R.id.telefonTextView)
        val tarihTextView: TextView = itemView.findViewById(R.id.tarihTextView)
        val saatTextView: TextView = itemView.findViewById(R.id.saatTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandevuViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_randevu, parent, false)
        return RandevuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RandevuViewHolder, position: Int) {
        val currentItem = randevuList[position]

        holder.adSoyadTextView.text = currentItem.otoparkAdi
        holder.telefonTextView.text = currentItem.telefon
        holder.tarihTextView.text = currentItem.tarih
        holder.saatTextView.text = currentItem.saat
    }

    override fun getItemCount() = randevuList.size
}
