package com.egci428.internstation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    lateinit var dataReference: FirebaseFirestore
    lateinit var dataList: MutableList<userData>
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var loginBtn: Button
    lateinit var regisBtn: Button
    var flg=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dataReference = FirebaseFirestore.getInstance()
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginBtn = findViewById(R.id.button)
        regisBtn = findViewById(R.id.button5)

        regisBtn.setOnClickListener {
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }
        loginBtn.setOnClickListener {
            //readFirestoreData()
            if(flg==1) {
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
        }
    }
    private fun readFirestoreData(){
        val readUsername = username.text.toString()
        val readPassword = password.text.toString()
        var db = dataReference.collection("userData")
        db.orderBy("time").get()
            .addOnSuccessListener { snapshot ->
                if(snapshot!=null){
                    dataList.clear()
                    val Data = snapshot.toObjects(userData::class.java)
                    for(data in  Data){
                        dataList.add(data)
                        val firebase_username = data.username
                        val firebase_password = data.password
                        if(readUsername==firebase_username && readPassword==firebase_password){
                            flg=1
                            break
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
            }
    }
}