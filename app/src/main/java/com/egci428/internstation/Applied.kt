package com.egci428.internstation

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egci428.internstation.Data.AppliedData
import com.egci428.internstation.Data.CompanyData
import com.egci428.internstation.Data.UserData
import com.google.firebase.firestore.FirebaseFirestore

class Applied : AppCompatActivity() {

    lateinit var backBtn:ImageView
    lateinit var title:TextView
    lateinit var recyclerView: RecyclerView
    lateinit var dataReference: FirebaseFirestore
    lateinit var dataList: MutableList<AppliedData>
    lateinit var userID:String
    lateinit var appliedList: MutableList<AppliedData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_applied)

        dataReference = FirebaseFirestore.getInstance()
        dataList = mutableListOf()
        appliedList = mutableListOf()
        recyclerView = findViewById(R.id.recyclerView)
        val linearLayoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        title = findViewById(R.id.titleText)
        backBtn = findViewById(R.id.backBtn)

        userID = intent.getStringExtra("userID").toString()
        Log.d("APPLIED",userID)

        readFirebase()

        title.setText("Applied")
        backBtn.setOnClickListener{
            val intent = Intent(this,Home::class.java)
            intent.putExtra("userID", userID)
            startActivity(intent)
        }

    }
    private fun readFirebase(){


        var db = dataReference.collection("appliedData")
        db.orderBy("appliedId").get()
            .addOnSuccessListener { snapshot ->
                if(snapshot!=null){
                    dataList.clear()
                    val appliedObj = snapshot.toObjects(AppliedData::class.java)
                    Log.d("APPLIED","BEGIN REQUEST DATA")

                    for(dataObj in appliedObj){


                        val firebase_username = dataObj.appliedId
                        Log.d("APPLIED",firebase_username)

                        if(firebase_username==userID){
                            dataList.add(dataObj)
                            Log.d("APPLIED","ID IS MATCH")
                            var adapter = AppliedAdapter(dataList)
                            recyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                            Log.d("APPLIED", "Load to Adapter")

                            Toast.makeText(baseContext, "Get userID success", Toast.LENGTH_SHORT).show()

                        }else if(appliedObj.lastIndex==appliedObj.size){
                            Log.d("APPLIED","You need to login/sign-up first")
                            Toast.makeText(baseContext, "You need to login/sign-up first", Toast.LENGTH_LONG).show()
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