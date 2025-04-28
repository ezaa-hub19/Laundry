package com.ezaa.laundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import android.widget.TextView
import com.ezaa.laundry.modeldata.modelpegawai

class adapter_data_pegawai (
    private val listPegawai: ArrayList<modelpegawai>) :
    RecyclerView.Adapter<adapter_data_pegawai.ViewHolder>() {
    override  fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pegawai = listPegawai[position]

        holder.tvCARD_PEGAWAI_ID.text = pegawai.idPegawai
        holder.tvCARD_PEGAWAI_NAMA.text = pegawai.namaPegawai
        holder.tvCARD_PEGAWAI_ALAMAT.text = pegawai.alamatPegawai
        holder.tvCARD_PEGAWAI_NOHP.text = pegawai.noHPPegawai
        holder.tvCARD_PEGAWAI_CABANG.text = pegawai.idCabangPegawai
        holder.cardPegawai.setOnClickListener {

        }
        holder.lihat.setOnClickListener {

        }
        holder.hubungi.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardPegawai = itemView.findViewById<View>(R.id.cvPegawai)
        val tvCARD_PEGAWAI_ID = itemView.findViewById<TextView>(R.id.tvCardPegawaiID)
        val tvCARD_PEGAWAI_NAMA = itemView.findViewById<TextView>(R.id.tvCardPegawaiNama)
        val tvCARD_PEGAWAI_ALAMAT = itemView.findViewById<TextView>(R.id.tvCardPegawaiAlamat)
        val tvCARD_PEGAWAI_NOHP = itemView.findViewById<TextView>(R.id.tvCardPegawaiNoHp)
        val tvCARD_PEGAWAI_CABANG = itemView.findViewById<TextView>(R.id.tvCardPegawaiCabang)
        val lihat = itemView.findViewById<Button>(R.id.btnCardPegawaiLihat)
        val hubungi = itemView.findViewById<Button>(R.id.btnCardPegawaiHubungi)
    }
}