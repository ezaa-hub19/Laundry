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
import com.ezaa.laundry.modeldata.modelpelanggan
import com.ezaa.laundry.transaksi.DataTransaksiActivity
import com.google.firebase.database.DatabaseReference

class adapter_pilih_pelanggan (private val listPelanggan: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_pilih_pelanggan.ViewHolder>() {

    private val TAG = "adapter_pilih_pelanggan"
    lateinit var appContext: Context
    lateinit var databaseReference: DatabaseReference

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        Log.d(TAG, "onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_pilih_pelanggan, parent, false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder for position: $position")
        val nomor = position + 1
        val item = listPelanggan[position]

        try {
            holder.tvID.text = "[$nomor]"
            val context = holder.itemView.context

            holder.tvNama.text = item.namaPelanggan ?: context.getString(R.string.nama_tidak_tersedia)

            val alamat = item.alamatPelanggan ?: context.getString(R.string.alamat_tidak_tersedia)
            holder.tvAlamat.text = context.getString(R.string.label_alamat, alamat)

            val noHP = item.noHPPelanggan ?: context.getString(R.string.nohp_tidak_tersedia)
            holder.tvNoHP.text = context.getString(R.string.label_nohp, noHP)


            Log.d(TAG, "Binding data: ${item.namaPelanggan} at position $position")

            holder.cvCARD.setOnClickListener {
                try {
                    val intent = Intent(appContext, DataTransaksiActivity::class.java)
                    intent.putExtra("idPelanggan", item.idPelanggan)
                    intent.putExtra("nama", item.namaPelanggan)
                    intent.putExtra("noHP", item.noHPPelanggan)
                    (appContext as Activity).setResult(Activity.RESULT_OK, intent)
                    (appContext as Activity).finish()
                } catch (e: Exception) {
                    Log.e(TAG, "Error in click listener: ${e.message}")
                    Toast.makeText(appContext, context.getString(R.string.terjadi_kesalahan), Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error binding view holder: ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        val count = listPelanggan.size
        Log.d(TAG, "getItemCount: $count")
        return count
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvID: TextView = itemView.findViewById(R.id.tvCARD_PILIHPELANGGAN_ID)
        val tvNama: TextView = itemView.findViewById(R.id.tvCARD_PILIHPELANGGAN_nama)
        val tvAlamat: TextView = itemView.findViewById(R.id.tvCARD_PILIHPELANGGAN_alamat)
        val tvNoHP: TextView = itemView.findViewById(R.id.tvCARD_PILIHPELANGGAN_nohp)
        val cvCARD: CardView = itemView.findViewById(R.id.cvCARD_PILIHPELANGGAN)
    }
}