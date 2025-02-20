package com.ezaa.laundry

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ezaa.laundry.R
import com.ezaa.laundry.pegawai.DataPegawaiActivity
import com.ezaa.laundry.pelanggan.DataPelangganActivity
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var Pelanggan: ImageView
    lateinit var Pegawai: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        init()
        tekan()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun init() {
        Pelanggan = findViewById(R.id.Pelanggan)
        Pegawai = findViewById(R.id.Pegawai)

    }

    fun tekan() {
        Pelanggan.setOnClickListener {
            val intent = Intent(this, DataPelangganActivity::class.java)
            startActivity(intent)
        }

        Pegawai.setOnClickListener {
            val intent = Intent(this, DataPegawaiActivity::class.java)
            startActivity(intent)
        }

        // Referensi TextView
        val helloTextView = findViewById<View>(R.id.greeting_text) as TextView
        val dateTextView = findViewById<View>(R.id.date_text) as TextView

        // Mengatur tanggal hari ini
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        dateTextView.text = currentDate

        // Mengatur pesan berdasarkan waktu
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour in 5..11 -> "Selamat Pagi, Reza"
            hour in 12..14 -> "Selamat Siang, Reza"
            hour in 15..17 -> "Selamat Sore, Reza"
            else -> "Selamat Malam, Reza"
        }
        helloTextView.text = greeting
    }
}