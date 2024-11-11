package com.example.auctionapp

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

object CloudinaryManager {

    private var isInitialized = false

    // Настройка Cloudinary
    fun init(context: Context) {
        if (isInitialized) return

        val config: MutableMap<String, String> = HashMap()
        config["cloud_name"] = "dfl24zron" // Замените на ваш cloud_name
        config["api_key"] = "265954644594142"       // Замените на ваш api_key
        config["api_secret"] = "YRT8qOeDHNsTpqa-iiB5K2zMptA" // Замените на ваш api_secret

        MediaManager.init(context, config)
        isInitialized = true
    }

    // Метод для загрузки изображения
    fun uploadImage(uri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val requestId = MediaManager.get().upload(uri)
            .option("folder", "auction_app")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // Начало загрузки
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    // Прогресс загрузки
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val imageUrl = resultData["url"] as String
                    onSuccess(imageUrl)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    Log.e("Cloudinary", "Ошибка загрузки: ${error.description}")
                    onError(error.description)
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    // Повторная попытка загрузки
                }
            })
            .dispatch()
    }
}
