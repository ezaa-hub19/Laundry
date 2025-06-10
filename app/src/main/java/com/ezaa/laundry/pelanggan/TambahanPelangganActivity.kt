package com.ezaa.laundry.pelanggan

import android.os.Bundle
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ezaa.laundry.R
import com.google.firebase.database.FirebaseDatabase
import com.ezaa.laundry.modeldata.modelpelanggan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahanPelangganActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("pelanggan")

    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoHP: EditText
    private lateinit var etCabang: EditText
    private lateinit var btSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambahan_pelanggan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_pelanggan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi View
        etNama = findViewById(R.id.etNamaTambahPelanggan)
        etAlamat = findViewById(R.id.etAlamatTambahPelanggan)
        etNoHP = findViewById(R.id.etNoHpTambahPelanggan)
        etCabang = findViewById(R.id.etCabangTambahPelanggan)
        btSimpan = findViewById(R.id.btSimpanTambahPelanggan)

        val idPelanggan = intent.getStringExtra("idPelanggan")
        val isEdit = idPelanggan != null
        val namaPelanggan = intent.getStringExtra("namaPelanggan")
        val alamatPelanggan = intent.getStringExtra("alamatPelanggan")
        val noHPPelanggan = intent.getStringExtra("noHPPelanggan")
        val cabangPelanggan = intent.getStringExtra("cabangPelanggan")

        // Set judul halaman berdasarkan mode
        setupTitle(isEdit)

        if (idPelanggan != null) {
            // Isi form untuk edit
            etNama.setText(namaPelanggan)
            etAlamat.setText(alamatPelanggan)
            etNoHP.setText(noHPPelanggan)
            etCabang.setText(cabangPelanggan)

            // Ubah text tombol untuk mode edit
            btSimpan.text = getString(R.string.update_pelanggan)
        }

        // Set event listener untuk tombol simpan
        btSimpan.setOnClickListener {
            validasi()
        }
    }

    private fun setupTitle(isEdit: Boolean) {

        // Atau jika ada TextView khusus untuk title di layout
        val titleTextView = findViewById<TextView>(R.id.tvJudulTambahPelanggan)
        titleTextView?.text = if (isEdit)
            getString(R.string.sunting_pelanggan)
        else
            getString(R.string.title_tambahpelanggan)
    }

    private fun validasi() {
        val nama = etNama.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()
        val noHP = etNoHP.text.toString().trim()
        val cabang = etCabang.text.toString().trim()

        if (nama.isEmpty()) {
            etNama.error = getString(R.string.validasi_nama_pelanggan)
            etNama.requestFocus()
            return
        }
        if (alamat.isEmpty()) {
            etAlamat.error = getString(R.string.validasi_alamat_pelanggan)
            etAlamat.requestFocus()
            return
        }
        if (noHP.isEmpty()) {
            etNoHP.error = getString(R.string.validasi_nohp_pelanggan)
            etNoHP.requestFocus()
            return
        }
        if (cabang.isEmpty()) {
            etCabang.error = getString(R.string.validasi_cabang_pelanggan)
            etCabang.requestFocus()
            return
        }
        if (!noHP.matches(Regex("\\d{10,13}"))) {
            etNoHP.error = "Nomor HP harus terdiri dari 10-13 angka"
            etNoHP.requestFocus()
            return
        }

        // Jika semua validasi lolos, simpan data
        simpan(nama, alamat, noHP, cabang)
    }

    private fun simpan(nama: String, alamat: String, noHP: String, cabang: String) {
        val idPelanggan = intent.getStringExtra("idPelanggan")
        val isEdit = idPelanggan != null

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val tanggalSekarang = sdf.format(Date())

        if (isEdit) {
            // MODE EDIT
            val dataUpdate = mapOf<String, Any>(
                "namaPelanggan" to nama,
                "alamatPelanggan" to alamat,
                "noHPPelanggan" to noHP,
                "cabangPelanggan" to cabang,
                "tanggalTerdaftar" to tanggalSekarang // Optional: bisa skip kalau gak mau ubah tanggal
            )

            myRef.child(idPelanggan!!)
                .updateChildren(dataUpdate)
                .addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.sukses_update_pelanggan), Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.gagal_update_pelanggan), Toast.LENGTH_SHORT).show()
                }

        } else {
            // MODE TAMBAH BARU
            val pelangganBaru = myRef.push()
            val pelangganId = pelangganBaru.key ?: return

            val dataBaru = modelpelanggan(
                idPelanggan = pelangganId,
                namaPelanggan = nama,
                alamatPelanggan = alamat,
                noHPPelanggan = noHP,
                cabangPelanggan = cabang,
                tanggalTerdaftar = tanggalSekarang
            )

            pelangganBaru.setValue(dataBaru)
                .addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.sukses_simpan_pelanggan), Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, getString(R.string.gagal_simpan_pelanggan), Toast.LENGTH_SHORT).show()
                }
        }
    }
}