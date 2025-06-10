package com.ezaa.laundry.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import com.ezaa.laundry.modeldata.modellayanan
import com.ezaa.laundry.transaksi.DataTransaksiActivity
import com.google.firebase.database.DatabaseReference

class adapter_pilih_layanan (private val listLayanan: ArrayList<modellayanan>) :
    RecyclerView.Adapter<adapter_pilih_layanan.ViewHolder>() {

    private val TAG = "AdapterPilihLayanan"
    lateinit var appContext: Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        Log.d(TAG, "onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_layanan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder for position: $position")
        val nomor = position + 1
        val item = listLayanan[position]

        try {
            holder.tvID.text = "[$nomor]"
            holder.tvNama.text = item.namaLayanan ?: appContext.getString(R.string.nama_tidak_tersedia)
            val hargaTeks = item.hargaLayanan ?: appContext.getString(R.string.harga_tidak_tersedia)
            holder.tvHarga.text = appContext.getString(R.string.label_harga, hargaTeks)

            Log.d(TAG, "Binding data: ${item.namaLayanan} at position $position")

            holder.cvCARD.setOnClickListener {
                try {
                    val intent = Intent(appContext, DataTransaksiActivity::class.java)
                    intent.putExtra("idPelanggan", item.idLayanan)
                    intent.putExtra("nama", item.namaLayanan)
                    intent.putExtra("harga", item.hargaLayanan)
                    (appContext as Activity).setResult(Activity.RESULT_OK, intent)
                    (appContext as Activity).finish()
                } catch (e: Exception) {
                    Log.e(TAG, "Error in click listener: ${e.message}")
                    Toast.makeText(appContext, appContext.getString(R.string.terjadi_kesalahan), Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error binding view holder: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        val count = listLayanan.size
        Log.d(TAG, "getItemCount: $count")
        return count
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvID: TextView = itemView.findViewById(R.id.tvCARD_PILIHLAYANAN_ID)
        val tvNama: TextView = itemView.findViewById(R.id.tvCARD_PILIHLAYANAN_nama)
        val tvHarga: TextView = itemView.findViewById(R.id.tvCARD_PILIHLAYANAN_harga)
        val cvCARD: CardView = itemView.findViewById(R.id.cvCARD_PILIHLAYANAN)
    }
}