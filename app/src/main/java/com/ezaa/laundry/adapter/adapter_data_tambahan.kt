package com.ezaa.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import com.ezaa.laundry.modeldata.modeltambahan

class adapter_data_tambahan (
    private val list: List<modeltambahan>,
    private val onItemLongClick: (modeltambahan) -> Unit // Tambahan fungsi long click
) : RecyclerView.Adapter<adapter_data_tambahan.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTambahan: TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_ID)
        val nama: TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_nama)
        val harga: TextView = itemView.findViewById(R.id.tvCARD_TAMBAHAN_harga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.idTambahan.text = "${item.idTambahan}"
        holder.nama.text = item.namaTambahan
        holder.harga.text = "Rp. ${item.hargaTambahan}"

        // Tambahkan long click listener
        holder.itemView.setOnLongClickListener {
            onItemLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int = list.size
}