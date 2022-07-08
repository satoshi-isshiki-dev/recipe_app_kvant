package com.example.recipeapp

import android.app.Application
import com.example.recipeapp.db.Database

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Database.build(this)
    }
}