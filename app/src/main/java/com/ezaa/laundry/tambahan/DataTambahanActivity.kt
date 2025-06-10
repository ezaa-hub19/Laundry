package com.ezaa.laundry.tambahan

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import com.ezaa.laundry.adapter.adapter_data_tambahan
import com.ezaa.laundry.modeldata.modeltambahan
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataTambahanActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("tambahan")

    private lateinit var rvDataTambahan: RecyclerView
    private lateinit var fabTambahTambahan: FloatingActionButton
    private lateinit var tambahanList: ArrayList<modeltambahan>

    private fun init() {
        rvDataTambahan = findViewById(R.id.rvDATA_TAMBAHAN)
        fabTambahTambahan = findViewById(R.id.fabDATA_TAMBAHAN_Tambah)
        rvDataTambahan.layoutManager = LinearLayoutManager(this)
    }

    private fun getDATA() {
        val query = myRef.orderByChild("idTambahan").limitToLast(100)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    tambahanList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val tambahan = dataSnapshot.getValue(modeltambahan::class.java)
                        tambahan?.let { tambahanList.add(it) }
                    }

                    // Adapter dengan fitur hapus
                    val adapter = adapter_data_tambahan(tambahanList) { selectedItem ->
                        val idToDelete = selectedItem.idTambahan
                        if (idToDelete != null) {
                            AlertDialog.Builder(this@DataTambahanActivity)
                                .setTitle("Konfirmasi Hapus")
                                .setMessage("Yakin ingin menghapus \"${selectedItem.namaTambahan}\"?")
                                .setPositiveButton("Hapus") { _, _ ->
                                    myRef.child(idToDelete).removeValue()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this@DataTambahanActivity,
                                                "Data berhasil dihapus",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                this@DataTambahanActivity,
                                                "Gagal menghapus data",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                                .setNegativeButton("Batal", null)
                                .show()
                        }
                    }

                    rvDataTambahan.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataTambahanActivity, "Gagal load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_tambahan)

        tambahanList = ArrayList()
        init()
        getDATA()

        fabTambahTambahan.setOnClickListener {
            val intent = Intent(this, TambahTambahanActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_tambahan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}