package com.example.stockputawayapp

import android.app.Application
import com.example.stockputawayapp.data.AppContainer
import com.example.stockputawayapp.data.AppDataContainer

// AppContainer instance used by the rest of classes to obtain dependencies
class StockPutawayApplication : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}