package com.example.aerialcamapp.services

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.aerialcamapp.MainActivity
import com.github.kittinunf.fuel.Fuel
import kotlinx.coroutines.*

class HttpService {

    private val scope = MainScope()

    companion object {
        val instance = HttpService()
    }

    fun refreshImage(imageView: ImageView, url: String) = scope.launch(Dispatchers.IO) {
        val resp = Fuel.get("http://$url/capture").response().second

        when {
            resp.statusCode == -1 -> {
                displayToast(imageView.context, "An error occurred.")
            }
            resp.statusCode != 200 -> {
                displayToast(imageView.context, resp.responseMessage)
            }
            else -> {
                launch(Dispatchers.Main) {
                    imageView.setImageBitmap(
                        BitmapFactory.decodeByteArray(resp.data, 0, resp.data.size))
                }
            }
        }
    }

    fun startRecording(context: Context, url: String) = scope.launch(Dispatchers.IO) {
        val resp = Fuel.put("http://$url/record").response().second

        if (resp.statusCode == -1) {
            displayToast(context, "An error occurred.")
        } else {
            displayToast(context, String(resp.data))
        }
    }

    private fun displayToast(context: Context, msg: String) = scope.launch(Dispatchers.Main) {
        launch(Dispatchers.Main) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}