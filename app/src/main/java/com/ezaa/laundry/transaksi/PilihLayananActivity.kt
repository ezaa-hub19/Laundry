package com.ezaa.laundry.transaksi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ezaa.laundry.R
import com.ezaa.laundry.adapter.adapter_pilih_layanan
import com.ezaa.laundry.modeldata.modellayanan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PilihLayananActivity : AppCompatActivity() {

    private val TAG = "PilihLayanan"
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("layanan")

    private lateinit var rvPilihLayanan: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var tvKosong: TextView

    private var idCabang: String = ""
    private lateinit var listLayanan: ArrayList<modellayanan>
    private lateinit var filteredList: ArrayList<modellayanan>
    private lateinit var adapter: adapter_pilih_layanan

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pilih_layanan)

        Log.d(TAG, "Activity created")

        tvKosong = findViewById(R.id.tvKosong)
        rvPilihLayanan = findViewById(R.id.rvPILIH_LAYANAN)
        searchView = findViewById(R.id.searchViewLayanan)

        listLayanan = ArrayList()
        filteredList = ArrayList()

        rvPilihLayanan.layoutManager = LinearLayoutManager(this)

        idCabang = intent.getStringExtra("idCabang") ?: ""
        Log.d(TAG, "idCabang: $idCabang")

        setupSearchView()
        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pilih_layanan)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        Log.d(TAG, "Filtering list with query: $query")
        filteredList.clear()

        if (query.isNullOrEmpty()) {
            filteredList.addAll(listLayanan)
        } else {
            val searchText = query.lowercase().trim()
            for (layanan in listLayanan) {
                if (layanan.namaLayanan?.lowercase()?.contains(searchText) == true ||
                    layanan.hargaLayanan?.lowercase()?.contains(searchText) == true) {
                    filteredList.add(layanan)
                }
            }
        }

        updateRecyclerView()
    }

    private fun updateRecyclerView() {
        Log.d(TAG, "Updating RecyclerView with ${filteredList.size} items")

        if (filteredList.isEmpty()) {
            tvKosong.visibility = View.VISIBLE
            tvKosong.text = getString(R.string.no_matching_services)
        } else {
            tvKosong.visibility = View.GONE
        }

        adapter = adapter_pilih_layanan(filteredList)
        rvPilihLayanan.adapter = adapter
    }

    private fun getData() {
        Log.d(TAG, "getData() called")

        val query = if (idCabang.isEmpty()) {
            myRef.limitToLast(100)
        } else {
            myRef.orderByChild("idCabang").equalTo(idCabang).limitToLast(100)
        }

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: data exists: ${snapshot.exists()}, count: ${snapshot.childrenCount}")

                if (snapshot.exists()) {
                    listLayanan.clear()
                    for (snap in snapshot.children) {
                        try {
                            val layanan = snap.getValue(modellayanan::class.java)
                            layanan?.let {
                                listLayanan.add(it)
                                Log.d(TAG, getString(R.string.log_added_service, it.namaLayanan))
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, getString(R.string.log_error_parsing_service, e.message ?: "Unknown error"))
                        }
                    }

                    filteredList.clear()
                    filteredList.addAll(listLayanan)

                    updateRecyclerView()
                } else {
                    Log.d(TAG, getString(R.string.log_no_data_found))
                    tvKosong.visibility = View.VISIBLE
                    tvKosong.text = getString(R.string.service_data_not_found)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database error: ${error.message}")
                tvKosong.visibility = View.VISIBLE
                tvKosong.text = "Error: ${error.message}"
                Toast.makeText(this@PilihLayananActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}