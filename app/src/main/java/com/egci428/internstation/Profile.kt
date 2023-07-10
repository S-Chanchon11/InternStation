package com.egci428.internstation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.KeyListener
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.egci428.internstation.Data.AppliedData
import com.egci428.internstation.Data.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Profile : AppCompatActivity() {
    lateinit var dataReference: FirebaseFirestore
    lateinit var university: TextView
    lateinit var Dob: TextView
    lateinit var ProfileTitle: TextView
    lateinit var name: EditText
    lateinit var dataList: MutableList<UserData>
    lateinit var userID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        university = findViewById(R.id.university)
        Dob = findViewById(R.id.Dob)
        ProfileTitle = findViewById(R.id.profileTitle)
        name = findViewById(R.id.nameTxt)
        dataReference = FirebaseFirestore.getInstance()
        dataList = mutableListOf()
        userID = intent.getStringExtra("userID").toString()
        Log.d("APPLIED",userID)

        readFirestore()
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
                        }else{
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
}