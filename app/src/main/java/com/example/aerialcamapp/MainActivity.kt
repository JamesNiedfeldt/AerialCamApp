package com.example.aerialcamapp

import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.aerialcamapp.services.HttpService
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        button_setip.setOnClickListener { _ ->
            setIp()
        }
    }

    private fun setIp() {
        if (textedit_ip.text.toString() != "") {
            ip = textedit_ip.text.toString()
            Toast.makeText(this, "IP set to $ip", Toast.LENGTH_SHORT).show()
            HttpService.instance.refreshImage(image_recentcapture, ip)
        } else {
            Toast.makeText(this, "IP required", Toast.LENGTH_SHORT).show()
        }
    }
}