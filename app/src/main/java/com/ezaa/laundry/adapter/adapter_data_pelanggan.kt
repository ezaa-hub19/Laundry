package com.ezaa.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import android.widget.TextView
import com.ezaa.laundry.modeldata.modelpelanggan

class adapter_data_pelanggan(
    private val listPelanggan: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {
    override  fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
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
        holder.cardPelanggan.setOnClickListener {

        }
        holder.lihat.setOnClickListener {

        }
        holder.hubungi.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardPelanggan = itemView.findViewById<View>(R.id.card_data_pelanggan)
        val tvCARD_PELANGGAN_ID = itemView.findViewById<TextView>(R.id.tvCardPelangganID)
        val tvCARD_PELANGGAN_NAMA = itemView.findViewById<TextView>(R.id.tvCardNamaPelanggan)
        val tvCARD_PELANGGAN_ALAMAT = itemView.findViewById<TextView>(R.id.tvCardPelangganAlamat)
        val tvCARD_PELANGGAN_NOHP = itemView.findViewById<TextView>(R.id.tvCardPelangganNoHp)
        val tvCARD_PELANGGAN_CABANG = itemView.findViewById<TextView>(R.id.tvCARD_PELANGGAN_CABANG)
        val lihat = itemView.findViewById<Button>(R.id.btnCardPelangganLihat1)
        val hubungi = itemView.findViewById<Button>(R.id.btnCardPelangganHubungi1)
    }
}
