package com.ezaa.laundry.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import com.ezaa.laundry.modeldata.modeltambahan

class adapter_pilih_tambahan (
    private val listTambahan: MutableList<modeltambahan>,
    private val enableLongDelete: Boolean = false, // true hanya di DataTransaksi
    private val onDelete: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<adapter_pilih_tambahan.ViewHolder>() {

    private val visibleDeleteSet = mutableSetOf<Int>()
    lateinit var appContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        appContext = parent.context
        val view = LayoutInflater.from(appContext).inflate(R.layout.card_pilih_tambahan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nomor = position + 1
        val item = listTambahan[position]

        holder.tvID.text = "[$nomor]"
        val context = holder.itemView.context

        holder.tvNama.text = item.namaTambahan ?: context.getString(R.string.nama_tidak_tersedia)

        val harga = item.hargaTambahan ?: context.getString(R.string.harga_tidak_tersedia)
        holder.tvHarga.text = context.getString(R.string.label_harga, harga)

        // Tampilkan atau sembunyikan tombol delete
        holder.btnDelete.visibility = if (visibleDeleteSet.contains(position)) View.VISIBLE else View.GONE

        // Long click untuk tampilkan tombol delete
        if (enableLongDelete) {
            holder.cvCARD.setOnLongClickListener {
                visibleDeleteSet.add(position)
                notifyItemChanged(position)
                true
            }
        } else {
            // Klik biasa untuk memilih data tambahan
            holder.cvCARD.setOnClickListener {
                val intent = Intent()
                intent.putExtra("idTambahan", item.idTambahan)
                intent.putExtra("namaTambahan", item.namaTambahan)
                intent.putExtra("hargaTambahan", item.hargaTambahan)
                (appContext as Activity).setResult(Activity.RESULT_OK, intent)
                (appContext as Activity).finish()
            }
        }

        // Klik tombol hapus
        holder.btnDelete.setOnClickListener {
            onDelete?.invoke(position)
            visibleDeleteSet.remove(position)
        }
    }

    override fun getItemCount(): Int = listTambahan.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvID: TextView = itemView.findViewById(R.id.tvCARD_PILIHTAMBAHAN_ID)
        val tvNama: TextView = itemView.findViewById(R.id.tvCARD_PILIHTAMBAHAN_nama)
        val tvHarga: TextView = itemView.findViewById(R.id.tvCARD_PILIHTAMBAHAN_harga)
        val cvCARD: CardView = itemView.findViewById(R.id.cvCARD_PILIHTAMBAHAN)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }
}