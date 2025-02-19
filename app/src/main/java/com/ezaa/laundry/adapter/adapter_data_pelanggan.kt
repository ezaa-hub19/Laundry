package com.ezaa.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import com.ezaa.laundry.modeldata.modelpelanggan

class adapter_data_pelanggan(
    private val listPelanggan: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = listPelanggan[position]
        holder.tvCARD_PELANGGAN_ID.text = pelanggan.idPelanggan
        holder.tvCARD_PELANGGAN_NAMA.text = pelanggan.namaPelanggan
        holder.tvCARD_PELANGGAN_ALAMAT.text = pelanggan.alamatPelanggan
        holder.tvCARD_PELANGGAN_NOHP.text = pelanggan.noHPPelanggan
        holder.tvCARD_PELANGGAN_CABANG.text = pelanggan.idCabang
        holder.cvCARD_PELANGGAN.setOnClickListener {
        }
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvCARD_PELANGGAN = itemView.findViewById<View>(R.id.cvPelanggan)
        val tvCARD_PELANGGAN_ID: TextView = itemView.findViewById(R.id.tvCardPelangganID)
        val tvCARD_PELANGGAN_NAMA: TextView = itemView.findViewById(R.id.tvCardNamaPelanggan)
        val tvCARD_PELANGGAN_ALAMAT: TextView = itemView.findViewById(R.id.tvCardPelangganAlamat)
        val tvCARD_PELANGGAN_NOHP: TextView = itemView.findViewById(R.id.tvCardPelangganNoHp)
        val tvCARD_PELANGGAN_CABANG: TextView = itemView.findViewById(R.id.tvCabangTambahPelanggan)
    }
}
