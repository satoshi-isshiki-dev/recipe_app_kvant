package com.example.recipeapp

import android.content.Context
import android.widget.Toast

fun makeToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}