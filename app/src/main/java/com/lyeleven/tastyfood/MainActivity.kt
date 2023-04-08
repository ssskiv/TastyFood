package com.lyeleven.tastyfood

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.File.separatorChar
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels


class MainActivity : AppCompatActivity() {

    private var courierNumber: Int = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isGPSActivated = false

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonNavigate = findViewById<Button>(R.id.navigate_button)
        val tv = findViewById<TextView>(R.id.tv)
        val rb = findViewById<Switch>(R.id.rb)

        val permissionManager = PermissionManager()
        permissionManager.showLocationPermissions(this)
        buttonNavigate.isEnabled = rb.isChecked
        rb.setOnClickListener {
            isGPSActivated = rb.isChecked/*buttonNavigate.isEnabled = rb.isChecked*/
        }
        buttonNavigate.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,/*saddr=47.224645,39.776141&*/
                Uri.parse("http://maps.google.com/maps?daddr=47.2251069,39.7746818")
            )
            startActivity(intent)
        }

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        if (sharedPref.getString(getString(R.string.courier_num_key), "0") == "0") {
            enterCourierNumber()
        }

    }

    private fun downloadFile(url: URL, outputFileName: String) {
        url.openStream().use {
            Channels.newChannel(it).use { rbc ->
                FileOutputStream(outputFileName).use { fos ->
                    fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                }
            }
        }
    }

    private fun filterFile(file: File, courierNumber: Int): Order {
        var order:Order=Order(0, 0.toString(),"0",0f,0f,0f,0f,)
        file.forEachLine {
            val list = it.split("&")
            val orderNumber = list[0].toInt()
            val time = list[1]
            val orderStatus = list[2].toString()
            val calt = list[3].toFloat()
            val clong = list[4].toFloat()
            val dalt = list[5].toFloat()
            val dlong = list[6].toFloat()
            val _courierNumber = list[7].toInt()
            if (_courierNumber == courierNumber && orderStatus == "В пути") {
                order = Order(orderNumber, time, orderStatus, calt, clong, dalt, dlong)
                return@forEachLine
            }
        }
        return order
    }

    private fun enterCourierNumber(): Int {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Введите номер курьера")
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        alert.setView(editText)
        var a = "0"
        alert.setPositiveButton("Ok") { _, _ ->
            a = editText.text.toString()
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(getString(com.lyeleven.tastyfood.R.string.courier_num_key), a)
                apply()
            }
        }
        alert.show()
        return a.toInt()
    }
}