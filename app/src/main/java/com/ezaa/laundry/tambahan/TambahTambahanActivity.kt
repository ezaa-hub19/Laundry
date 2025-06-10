package com.ezaa.laundry.tambahan

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ezaa.laundry.R
import com.ezaa.laundry.modeldata.modeltambahan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TambahTambahanActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("tambahan")

    private lateinit var etNama: EditText
    private lateinit var etHarga: EditText
    private lateinit var btnSimpan: Button

    // Edit mode variables
    private var isEditMode = false
    private var editIdLayanan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_tambahan)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tambah_tambahan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        checkEditMode()
        setupClickListeners()
    }

    private fun initViews() {
        etNama = findViewById(R.id.etNamaTambahan)
        etHarga = findViewById(R.id.etHargaTambahan)
        btnSimpan = findViewById(R.id.btSimpanTambahan)
    }

    private fun checkEditMode() {
        // Check if this is edit mode
        isEditMode = intent.getBooleanExtra("EDIT_MODE", false)

        if (isEditMode) {
            editIdLayanan = intent.getStringExtra("ID_TAMBAHAN")
            val namaTambahan = intent.getStringExtra("NAMA_TAMBAHAN")
            val hargaTambahan = intent.getStringExtra("HARGA_TAMBAHAN")

            // Set placeholder data with null safety
            etNama.setText(namaTambahan ?: "")
            etHarga.setText(hargaTambahan ?: "")

            // Change button text for edit mode
            btnSimpan.text = "Update Tambahan"

            // Change title if you have toolbar
            title = "Sunting Layanan Tambahan"
        } else {
            title = "Tambah Layanan Tambahan"
        }
    }

    private fun setupClickListeners() {
        btnSimpan.setOnClickListener {
            validasi()
        }
    }

    private fun validasi() {
        val nama = etNama.text.toString().trim()
        val hargaStr = etHarga.text.toString().trim()

        if (nama.isEmpty()) {
            etNama.error = "Nama layanan harus diisi"
            etNama.requestFocus()
            return
        }
        if (hargaStr.isEmpty()) {
            etHarga.error = "Harga layanan harus diisi"
            etHarga.requestFocus()
            return
        }

        val harga = hargaStr.toIntOrNull()
        if (harga == null || harga <= 0) {
            etHarga.error = "Harga harus berupa angka positif"
            etHarga.requestFocus()
            return
        }

        if (isEditMode) {
            updateData(nama, harga)
        } else {
            simpanBaru(nama, harga)
        }
    }

    private fun simpanBaru(nama: String, harga: Int) {
        val tambahanBaru = myRef.push()
        val tambahanId = tambahanBaru.key ?: return

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val tanggalDitambahkan = sdf.format(Date())

        val data = modeltambahan(
            idTambahan = tambahanId,
            namaTambahan = nama,
            hargaTambahan = harga.toString(),
            tanggalTerdaftar = tanggalDitambahkan
        )

        tambahanBaru.setValue(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Berhasil menambah layanan tambahan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Gagal menambah layanan: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateData(nama: String, harga: Int) {
        editIdLayanan?.let { id ->
            // Find and update the specific record
            myRef.orderByChild("idTambahan").equalTo(id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (dataSnapshot in snapshot.children) {
                                val updates = hashMapOf<String, Any>(
                                    "namaTambahan" to nama,
                                    "hargaTambahan" to harga.toString()
                                )

                                dataSnapshot.ref.updateChildren(updates)
                                    .addOnSuccessListener {
                                        Toast.makeText(this@TambahTambahanActivity, "Berhasil memperbarui layanan", Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                    .addOnFailureListener { error ->
                                        Toast.makeText(this@TambahTambahanActivity, "Gagal memperbarui: ${error.message}", Toast.LENGTH_SHORT).show()
                                    }
                                break
                            }
                        } else {
                            Toast.makeText(this@TambahTambahanActivity, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@TambahTambahanActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}