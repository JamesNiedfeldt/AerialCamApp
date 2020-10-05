package com.example.aerialcamapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.aerialcamapp.services.HttpService
import kotlinx.android.synthetic.main.activity_wifisettings.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class WifiSettingsActivity : AppCompatActivity() {

    private var ipSet = false
    private var toReturn = Intent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifisettings)

        textedit_ip.setText(intent.data.toString())

        button_setip.setOnClickListener { _ ->
            setIp()
        }
    }

    override fun finish() {
        if (ipSet) {
            setResult(Activity.RESULT_OK, toReturn)
        } else {
            setResult(Activity.RESULT_CANCELED, null)
        }
        super.finish()
    }

    private fun setIp() = runBlocking {
        if (textedit_ip.text.toString() != "") {
            val ip = textedit_ip.text.toString()
                val validIp =
                    withContext(Dispatchers.IO) { HttpService.instance.validConnection(ip) }
            if (validIp) {
                    toReturn.data = Uri.parse(ip)
                    ipSet = true
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Could not connect to $ip", Toast.LENGTH_SHORT).show()
                }

        } else {
            Toast.makeText(applicationContext, "IP required", Toast.LENGTH_SHORT).show()
        }
    }
}