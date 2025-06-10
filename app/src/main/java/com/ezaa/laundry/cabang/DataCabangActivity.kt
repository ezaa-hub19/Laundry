package com.ezaa.laundry.cabang

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import com.ezaa.laundry.adapter.adapter_data_cabang
import com.ezaa.laundry.modeldata.modelcabang
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataCabangActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DataCabang"
        private const val MAX_ITEMS = 100

        // Intent extra keys
        const val EXTRA_ID_CABANG = "idCabang"
        const val EXTRA_NAMA_LOKASI = "namaCabang"
        const val EXTRA_ALAMAT = "alamatCabang"
        const val EXTRA_TELEPON = "teleponCabang"
        const val EXTRA_TANGGAL_TERDAFTAR = "tanggalTerdaftar"
    }

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("cabang")

    private lateinit var rvDataCabang: RecyclerView
    private lateinit var fabTambahCabang: FloatingActionButton
    private lateinit var cabangList: ArrayList<modelcabang>
    private lateinit var adapter: adapter_data_cabang
    private var valueEventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_data_cabang)

        initializeViews()
        setupRecyclerView()
        setupClickListeners()
        setupWindowInsets()
        loadData()
    }

    private fun initializeViews() {
        rvDataCabang = findViewById(R.id.rvDATA_CABANG)
        fabTambahCabang = findViewById(R.id.fabDATA_CABANG_Tambah)
    }

    private fun setupRecyclerView() {
        cabangList = ArrayList()
        adapter = adapter_data_cabang(
            cabangList,
            onViewClick = { cabang ->
                showDetailDialog(cabang)
            },
            onItemClick = { cabang ->
                navigateToTambahCabang(cabang)
            }
        )
        rvDataCabang.layoutManager = LinearLayoutManager(this)
        rvDataCabang.setHasFixedSize(true)
        rvDataCabang.adapter = adapter
    }

    private fun setupClickListeners() {
        fabTambahCabang.setOnClickListener {
            navigateToTambahCabang()
        }
    }

    private fun navigateToTambahCabang(cabang: modelcabang? = null) {
        val intent = Intent(this, TambahCabangActivity::class.java)
        cabang?.let {
            intent.putExtra(EXTRA_ID_CABANG, it.idCabang)
            intent.putExtra(EXTRA_NAMA_LOKASI, it.namaLokasiCabang)
            intent.putExtra(EXTRA_ALAMAT, it.alamatCabang)
            intent.putExtra(EXTRA_TELEPON, it.teleponCabang)
            intent.putExtra(EXTRA_TANGGAL_TERDAFTAR, it.tanggalTerdaftar)
        }
        startActivity(intent)
    }

    private fun showDeleteConfirmationDialog(cabang: modelcabang) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus cabang \"${cabang.namaLokasiCabang}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                hapusDataCabang(cabang)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDetailDialog(cabang: modelcabang) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialogmod_cabang, null)

        // Set data to dialog views
        dialogView.findViewById<TextView>(R.id.tvDIALOG_CABANG_ID).text = cabang.idCabang ?: "-"
        dialogView.findViewById<TextView>(R.id.tvDIALOG_CABANG_NAMA).text = cabang.namaLokasiCabang ?: "-"
        dialogView.findViewById<TextView>(R.id.tvDIALOG_CABANG_ALAMAT).text = cabang.alamatCabang ?: "-"
        dialogView.findViewById<TextView>(R.id.tvDIALOG_NOHP_CABANG).text = cabang.teleponCabang ?: "-"

        // Format and set registration date
        val tanggalTerdaftar = if (cabang.tanggalTerdaftar.isNullOrEmpty()) {
            "-"
        } else {
            cabang.tanggalTerdaftar
        }
        dialogView.findViewById<TextView>(R.id.tvDIALOG_CABANG_TERDAFTAR).text = tanggalTerdaftar

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Setup button listeners
        dialogView.findViewById<MaterialButton>(R.id.btDIALOG_MOD_CABANG_Edit).setOnClickListener {
            dialog.dismiss()
            navigateToTambahCabang(cabang)
        }

        dialogView.findViewById<MaterialButton>(R.id.btDIALOG_MOD_CABANG_Hapus).setOnClickListener {
            dialog.dismiss()
            showDeleteConfirmationDialog(cabang)
        }

        dialog.show()
    }

    private fun hapusDataCabang(cabang: modelcabang) {
        if (cabang.idCabang.isNullOrEmpty()) {
            Toast.makeText(this, (getString(R.string.idcabangtakvalid)), Toast.LENGTH_SHORT).show()
            return
        }

        myRef.child(cabang.idCabang!!).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, (getString(R.string.Datacabangsucceeddelete)), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed access data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadData() {
        removeExistingListener()

        val query = myRef.orderByChild("idCabang").limitToLast(MAX_ITEMS)

        valueEventListener = query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cabangList.clear()
                for (dataSnapshot in snapshot.children) {
                    val cabang = dataSnapshot.getValue(modelcabang::class.java)
                    if (cabang != null) {
                        cabangList.add(cabang)
                        Log.d(TAG, "Added cabang: ${cabang.namaLokasiCabang}")
                    }
                }
                cabangList.reverse() // Supaya data terbaru muncul di atas
                adapter.notifyDataSetChanged()

                if (cabangList.isEmpty()) {
                    Toast.makeText(this@DataCabangActivity, (getString(R.string.Datacabangkosong)), Toast.LENGTH_SHORT).show()
                }

                Log.d(TAG, "Loaded ${cabangList.size} cabang items")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataCabangActivity, "Failed to access data: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Firebase Error: ${error.toException()}")
            }
        })
    }

    private fun removeExistingListener() {
        valueEventListener?.let {
            myRef.removeEventListener(it)
            Log.d(TAG, "Removed existing event listener")
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.data_cabang)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Activity resumed, reloading data")
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeExistingListener()
        Log.d(TAG, "Activity destroyed, listeners removed")
    }
}