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
    lateinit var imageProgress:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        university = findViewById(R.id.university)
        Dob = findViewById(R.id.Dob)
        ProfileTitle = findViewById(R.id.profileTitle)
        name = findViewById(R.id.nameTxt)
        userImage = findViewById(R.id.userImage)
        imageProgress = findViewById(R.id.imageProgress)

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
                    Log.d("Profile","BEGIN REQUEST DATA")

                    for(dataObj in userObj){
                        dataList.add(dataObj)

                        val firebase_username = dataObj.id
                        Log.d("Profile",firebase_username)
                        if(firebase_username==userID){

                            Log.d("Profile","ID IS MATCH")
                            Log.d("Profile", "Load to Adapter")
                            name.setText(dataObj.fullname)
                            university.setText(dataObj.university)
                            Dob.setText(dataObj.Dob)

                            Toast.makeText(baseContext, "Get userID success", Toast.LENGTH_SHORT).show()
                            break
                        }else if(userObj.lastIndex==userObj.size){
                            Log.d("LOGIN","ID IS NOT MATCH")
                            Toast.makeText(baseContext, "Incorrect Username/Password", Toast.LENGTH_LONG).show()
                        }
                    }
                    Log.d("LOGIN","END OF GET DATA")
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed", Toast.LENGTH_SHORT).show()
            }

    }
    private fun loadImage(){
        Log.d("IMAGE","try to load the image")
        var db = dataReference.collection("imageData")
        db.orderBy("id").get()
            .addOnSuccessListener { snapshot ->
                if(snapshot!=null){
                    Log.d("IMAGE","try to load the image 2")
                    val userImg = snapshot.toObjects(ImageData::class.java)
                    for(imgObj in userImg){
                        val firebase_username = imgObj.id
                        Log.d("IMAGE",imgObj.id)
                        Log.d("IMAGE",userID)
                        Log.d("IMAGE",firebase_username)
                        if(firebase_username==userID){
                            Log.d("IMAGE","ID IS MATCH")
                            Log.d("IMAGE",imgObj.uri)
                            val upload = Picasso.get().load(imgObj.uri).fit().into(userImage)
                            imageProgress.setVisibility(View.VISIBLE)
                            if(upload.equals(userImg)){
                                imageProgress.setVisibility(View.INVISIBLE)
                            }

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