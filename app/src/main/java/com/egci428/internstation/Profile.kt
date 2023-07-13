package com.egci428.internstation


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.View

import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar

import android.widget.TextView
import android.widget.Toast
import com.egci428.internstation.Data.ImageData

import com.egci428.internstation.Data.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.security.AccessController.getContext


class Profile : AppCompatActivity() {
    lateinit var dataReference: FirebaseFirestore
    lateinit var university: TextView
    lateinit var Dob: TextView
    lateinit var ProfileTitle: TextView
    lateinit var name: EditText
    lateinit var dataList: MutableList<UserData>
    lateinit var userID:String
    lateinit var userImage:ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        university = findViewById(R.id.university)
        Dob = findViewById(R.id.Dob)
        ProfileTitle = findViewById(R.id.profileTitle)
        name = findViewById(R.id.nameTxt)
        userImage = findViewById(R.id.userImage)


        dataReference = FirebaseFirestore.getInstance()
        dataList = mutableListOf()
        userID = intent.getStringExtra("userID").toString()
        Log.d("APPLIED",userID)

        readFirestore()
        loadImage()

    }
    private fun readFirestore(){
        var db = dataReference.collection("userData")
        db.orderBy("fullname").get()
            .addOnSuccessListener { snapshot ->
                if(snapshot!=null){
                    dataList.clear()
                    val userObj = snapshot.toObjects(UserData::class.java)
                    for(dataObj in userObj){
                        dataList.add(dataObj)
                        val firebase_username = dataObj.id
                        if(firebase_username==userID){
                            name.setText(dataObj.fullname)
                            university.setText(dataObj.university)
                            Dob.setText(dataObj.Dob)

                            break
                        }else if(userObj.lastIndex==userObj.size){
                            Toast.makeText(baseContext, "Incorrect Username/Password", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed", Toast.LENGTH_SHORT).show()
            }

    }
    private fun loadImage(){
        var db = dataReference.collection("imageData")
        db.orderBy("id").get()
            .addOnSuccessListener { snapshot ->
                if(snapshot!=null){
                    val userImg = snapshot.toObjects(ImageData::class.java)
                    for(imgObj in userImg){
                        val firebase_username = imgObj.id
                        if(firebase_username==userID){
                            Picasso.get().load(imgObj.uri).fit().into(userImage)
                        } else
                            Log.d("IMAGE","ID IS NOT MATCH")
                    }
                }
            }
            .addOnFailureListener {
                Log.d("IMAGE","FAIL")
            }
    }
}