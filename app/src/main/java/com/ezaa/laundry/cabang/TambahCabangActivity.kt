package com.ezaa.laundry.cabang

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ezaa.laundry.R
import com.ezaa.laundry.modeldata.modelcabang
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahCabangActivity : AppCompatActivity() {

    private lateinit var etNamaCabang: EditText
    private lateinit var etAlamatCabang: EditText
    private lateinit var etTeleponCabang: EditText
    private lateinit var btnSimpan: Button

    private val database = FirebaseDatabase.getInstance()
    private val refCabang = database.getReference("cabang")

    private var idCabang: String? = null  // Untuk update data
    private var tanggalTerdaftar: String? = null // Untuk menyimpan tanggal terdaftar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_cabang)

        etNamaCabang = findViewById(R.id.etNamaCabang)
        etAlamatCabang = findViewById(R.id.etAlamatCabang)
        etTeleponCabang = findViewById(R.id.etNoHPCabang)
        btnSimpan = findViewById(R.id.btSimpanCabang)

        // Cek apakah ini mode update
        idCabang = intent.getStringExtra(DataCabangActivity.EXTRA_ID_CABANG)
        if (idCabang != null) {
            // Mode update - isi field dengan data existing
            etNamaCabang.setText(intent.getStringExtra(DataCabangActivity.EXTRA_NAMA_LOKASI))
            etAlamatCabang.setText(intent.getStringExtra(DataCabangActivity.EXTRA_ALAMAT))
            etTeleponCabang.setText(intent.getStringExtra(DataCabangActivity.EXTRA_TELEPON))
            tanggalTerdaftar = intent.getStringExtra(DataCabangActivity.EXTRA_TANGGAL_TERDAFTAR)

            // Update title atau text button untuk mode edit
            supportActionBar?.title = "Edit Cabang"
            btnSimpan.text = "Update"
        } else {
            // Mode tambah baru - set tanggal terdaftar ke hari ini
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            tanggalTerdaftar = dateFormat.format(Date())

            supportActionBar?.title = "Tambah Cabang"
            btnSimpan.text = "Simpan"
        }

        btnSimpan.setOnClickListener {
            val nama = etNamaCabang.text.toString().trim()
            val alamat = etAlamatCabang.text.toString().trim()
            val telepon = etTeleponCabang.text.toString().trim()

            if (nama.isEmpty() || alamat.isEmpty() || telepon.isEmpty()) {
                Toast.makeText(this, (getString(R.string.isisemuafield)), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi nomor telepon (opsional)
            if (!isValidPhoneNumber(telepon)) {
                Toast.makeText(this, "Format nomor telepon tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Gunakan ID dari intent jika update, atau generate dari push().key jika tambah baru
            val id = idCabang ?: refCabang.push().key
            if (id == null) {
                Toast.makeText(this, (getString(R.string.gagalmembuatid)), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            simpanDataCabang(id, nama, alamat, telepon)
        }
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        // Basic validation - bisa disesuaikan dengan kebutuhan
        val phonePattern = Regex("^[+]?[0-9\\-\\s]{8,15}$")
        return phonePattern.matches(phone)
    }

    private fun simpanDataCabang(id: String, nama: String, alamat: String, telepon: String) {
        val cabang = modelcabang(
            idCabang = id,
            namaLokasiCabang = nama,
            alamatCabang = alamat,
            teleponCabang = telepon,
            tanggalTerdaftar = tanggalTerdaftar ?: getCurrentDate()
        )

        refCabang.child(id).setValue(cabang)
            .addOnSuccessListener {
                val message = if (idCabang != null) {
                    "Data cabang berhasil diupdate"
                } else {
                    getString(R.string.datacabangberhasildisimpan)
                }
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Data Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
}