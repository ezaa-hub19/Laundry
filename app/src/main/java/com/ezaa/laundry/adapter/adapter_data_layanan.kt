package com.ezaa.laundry.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import com.ezaa.laundry.modeldata.modellayanan

class adapter_data_layanan (
    private val listLayanan: ArrayList<modellayanan>,
    private val onEditClick: ((modellayanan, Int) -> Unit)? = null,
    private val onDeleteClick: ((modellayanan, Int) -> Unit)? = null,
    private val onViewClick: ((modellayanan, Int) -> Unit)? = null
) : RecyclerView.Adapter<adapter_data_layanan.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_layanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listLayanan[position]
        holder.tvDataIDLayanan.text = item.idLayanan ?: ""
        holder.tvNama.text = item.namaLayanan ?: ""
        holder.tvHarga.text = item.hargaLayanan ?: ""
        holder.tvTerdaftar.text = "${item.tanggalTerdaftar ?: "-"}"
        holder.tvCabang.text = "${item.cabangLayanan ?: "Tidak Ada Cabang"}"

        // Click listener untuk card view (edit/sunting)
        holder.itemView.setOnClickListener {
            onEditClick?.invoke(item, position)
        }

        // Click listener untuk tombol hapus
        holder.btnHapus?.setOnClickListener {
            showDeleteConfirmation(holder.itemView, item, position)
        }

        // Click listener untuk tombol lihat (dialog_mod_layanan)
        holder.btnLihat?.setOnClickListener {
            onViewClick?.invoke(item, position)
        }
    }

    private fun showDeleteConfirmation(view: View, item: modellayanan, position: Int) {
        AlertDialog.Builder(view.context)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus layanan \"${item.namaLayanan}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                onDeleteClick?.invoke(item, position)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // Fungsi untuk menghapus item dari list
    fun removeItem(position: Int) {
        if (position >= 0 && position < listLayanan.size) {
            listLayanan.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, listLayanan.size)
        }
    }

    // Fungsi untuk update item setelah edit
    fun updateItem(position: Int, updatedItem: modellayanan) {
        if (position >= 0 && position < listLayanan.size) {
            listLayanan[position] = updatedItem
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return listLayanan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDataIDLayanan: TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_ID)
        val tvNama: TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_nama)
        val tvHarga: TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_harga)
        val tvCabang: TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_cabang)
        val tvTerdaftar: TextView = itemView.findViewById(R.id.tvCARD_LAYANAN_terdaftar)

        // Tombol hapus dan lihat
        val btnHapus: Button? = itemView.findViewById(R.id.btn_hapus_layanan)
        val btnLihat: Button? = itemView.findViewById(R.id.btn_lihat_layanan)
    }
}