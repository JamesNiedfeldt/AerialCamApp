package com.example.aerialcamapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.aerialcamapp.services.HttpService
import kotlinx.android.synthetic.main.activity_main.*

const val WIFI_SETTINGS = 111

class MainActivity : AppCompatActivity() {

    private var ip: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_refresh.setOnClickListener { _ ->
            if (ip == "") {
                Toast.makeText(this, "IP required", Toast.LENGTH_SHORT).show()
            } else {
                HttpService.instance.refreshImage(image_recentcapture, ip)
            }
        }

        button_record.setOnClickListener { _ ->
            if (ip == "") {
                Toast.makeText(this, "IP required", Toast.LENGTH_SHORT).show()
            } else {
                HttpService.instance.startRecording(this, ip)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.wifi_settings -> {
                changeWifiSettings()
                true
            }
            R.id.length_settings -> {
                //TODO
                true
            }
            R.id.framesize_settings -> {
                //TODO
                true
            }
            R.id.quality_settings -> {
                //TODO
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            WIFI_SETTINGS -> {
                if (resultCode == RESULT_OK && data != null) {
                    ip = data.data.toString()
                    HttpService.instance.refreshImage(image_recentcapture, ip)
                }
            }
        }
    }

    private fun changeWifiSettings() {
        val intent = Intent(this, WifiSettingsActivity::class.java)
        intent.data = Uri.parse(ip)
        startActivityForResult(intent, WIFI_SETTINGS)
    }
}