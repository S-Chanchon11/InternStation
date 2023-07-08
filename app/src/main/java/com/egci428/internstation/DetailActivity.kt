package com.egci428.internstation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.egci428.internstation.Data.AppliedData
import com.google.firebase.firestore.FirebaseFirestore


class DetailActivity : AppCompatActivity() {
    lateinit var backBtn:ImageView
    lateinit var titleText:TextView
    lateinit var jobOf:TextView
    lateinit var name:TextView
    lateinit var jobDescription:TextView
    lateinit var benefit:TextView
    lateinit var qualification:TextView
    lateinit var applyBtn:Button
    lateinit var duration: TextView
    lateinit var userID:String
    lateinit var dataReference: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        backBtn = findViewById(R.id.backBtn)
        jobOf = findViewById(R.id.jobOffer)
        name = findViewById(R.id.companyName)
        jobDescription = findViewById(R.id.jobdes)
        benefit = findViewById(R.id.benefit)
        qualification = findViewById(R.id.qualification)
        applyBtn = findViewById(R.id.applyBtn)
        duration = findViewById(R.id.duration)

        backBtn.setOnClickListener {
            val intent = Intent(this,Home::class.java)
            startActivity(intent)
        }

        applyBtn.setOnClickListener{
            //submitAppliedData()
        }

        val companyName = intent.getStringExtra("companyName")
        name.text = companyName
        val companyDes = intent.getStringExtra("companyDes")
        jobDescription.text = companyDes
        val companyQualif = intent.getStringExtra("companyQualif")
        qualification.text = companyQualif
        val jobOffer = intent.getStringExtra("jobOffer")
        jobOf.text = jobOffer
        val companyBenefit = intent.getStringExtra("companyBenefit")
        benefit.text = companyBenefit
        val companyDuration = intent.getStringExtra("companyDuration")
        duration.text = companyDuration
        //val dataID = intent.getStringExtra("userID")

    }

    private fun submitAppliedData(){
        val id = ""
        val nameText = name.text.toString()
        val jobText = jobOf.text.toString()
        val durationText = duration.text.toString()
        val qualificationText = qualification.text.toString()

        var db = dataReference.collection("appliedData")

        val userAppliedData = AppliedData(id, nameText,
            jobText,durationText,qualificationText)
        db.add(userAppliedData)
            .addOnSuccessListener { result ->
                Toast.makeText(applicationContext,"Upload Successfully", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext,"Failed", Toast.LENGTH_SHORT).show()
            }

    }


}