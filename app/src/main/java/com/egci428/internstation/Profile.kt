package com.egci428.internstation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.KeyListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Profile : AppCompatActivity() {
    lateinit var dataReference: FirebaseFirestore
    lateinit var university: TextView
    lateinit var Dob: TextView
    lateinit var ProfileTitle: TextView
    lateinit var Profile: ImageView
    lateinit var name: EditText
    lateinit var editBtn: Button
    lateinit var dataList: MutableList<UserData>
    lateinit var edit: KeyListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        //dataReference = FirebaseFirestore.getInstance()
        university = findViewById(R.id.universityTxt)
        Dob = findViewById(R.id.dobTxt)
        ProfileTitle = findViewById(R.id.profileTitle)
        Profile = findViewById(R.id.profilePic)
        name = findViewById(R.id.nameTxt)
        editBtn = findViewById(R.id.editBtn)
        //readFirestoreData()

        editBtn.setOnClickListener {

            name.isFocusable = true

        }
    }
    private fun readFirestoreData(){

        var db = dataReference.collection("userData")
        db.orderBy("time").get()
            .addOnSuccessListener { snapshot ->
                if(snapshot!=null){
                    dataList.clear()
                    val Data = snapshot.toObjects(UserData::class.java)
                    for(data in  Data){
                        dataList.add(data)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed", Toast.LENGTH_SHORT).show()
            }
    }
}