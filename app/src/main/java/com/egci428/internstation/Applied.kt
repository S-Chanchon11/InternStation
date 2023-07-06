package com.egci428.internstation

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

class Applied : AppCompatActivity() {
    lateinit var title:TextView
    lateinit var backBtn:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applied)
        title = findViewById(R.id.titleText)
        backBtn = findViewById(R.id.backBtn)

        title.setText("Applied")
        backBtn.setOnClickListener{
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
        }

    }
}