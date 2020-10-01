package com.example.aerialcamapp

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_refresh.setOnClickListener { _ ->
            RefreshTask(this).execute()
        }

        button_record.setOnClickListener { _ ->
            RecordTask(this).execute()
        }
    }

    private class RefreshTask internal constructor(inContext: MainActivity) :
        AsyncTask<Void?, String?, Pair<Int, ByteArray>?>() {

        private val context = inContext

        override fun doInBackground(vararg p0: Void?): Pair<Int, ByteArray>? {
            var pair: Pair<Int, ByteArray>? = null
            val resp = Fuel.get("http://${this.context.textedit_ip.text}/capture").response()

            if (resp.second.statusCode != -1) {
                return Pair(resp.second.statusCode, resp.second.data)
            }

            return pair
        }

        override fun onPostExecute(results: Pair<Int, ByteArray>?) {
            if (results != null) {
                if (results.first != 200) {
                    Log.i("Bad response", "${results.first}: ${String(results.second)}")
                    Toast.makeText(this.context.applicationContext, String(results.second),
                        Toast.LENGTH_SHORT).show()
                } else {
                    this.context.image_recentcapture.setImageBitmap(
                        BitmapFactory.decodeByteArray(results.second, 0, results.second.size))
                }
            } else {
                Toast.makeText(this.context.applicationContext, "An error occurred.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private class RecordTask internal constructor(inContext: MainActivity) :
        AsyncTask<Void?, String?, Pair<Int, ByteArray>?>() {

        private val context = inContext

        override fun doInBackground(vararg p0: Void?): Pair<Int, ByteArray>? {
            var pair: Pair<Int, ByteArray>? = null
            val resp = Fuel.put("http://${this.context.textedit_ip.text}/record").response()

            if (resp.second.statusCode != -1) {
                return Pair(resp.second.statusCode, resp.second.data)
            }

            return pair
        }

        override fun onPostExecute(results: Pair<Int, ByteArray>?) {
            if (results != null) {
                Toast.makeText(this.context.applicationContext, String(results.second),
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context.applicationContext, "An error occurred.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}