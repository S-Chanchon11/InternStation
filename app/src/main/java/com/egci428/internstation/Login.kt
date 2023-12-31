package com.egci428.internstation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.egci428.internstation.Data.UserData
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {
    lateinit var dataReference: FirebaseFirestore
    lateinit var dataList: MutableList<UserData>
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var loginBtn: Button
    lateinit var regisBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dataReference = FirebaseFirestore.getInstance()
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginBtn = findViewById(R.id.button)
        regisBtn = findViewById(R.id.button5)

        dataList = mutableListOf()

        regisBtn.setOnClickListener {
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }
        loginBtn.setOnClickListener {
            val readUsername = username.text.toString()
            val readPassword = password.text.toString()

            var db = dataReference.collection("userData")
            db.orderBy("fullname").get()
                .addOnSuccessListener { snapshot ->
                    if(snapshot!=null){
                        dataList.clear()
                        val dataObj = snapshot.toObjects(UserData::class.java)


                        for(dataObj in dataObj){
                            dataList.add(dataObj)

                            val firebase_username = dataObj.username
                            val firebase_password = dataObj.password
                            if(firebase_username==readUsername && firebase_password==readPassword){

                                val intent = Intent(this, Home::class.java)
                                intent.putExtra("userID",dataObj.id)

                                Toast.makeText(baseContext, "Login Success", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                                break
                            }else if (dataList.equals(dataList.lastIndex)){

                                password.text.clear()
                                Toast.makeText(baseContext, "Incorrect Username/Password", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
                }


        }
    }
}