package com.egci428.internstation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.egci428.internstation.Data.UserData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.Month


class Register : AppCompatActivity() {

    lateinit var dataReference:FirebaseFirestore
    lateinit var username: EditText
    lateinit var fullname: EditText

    lateinit var university: EditText
    lateinit var password: EditText
    lateinit var repassword: EditText
    lateinit var submitBtn: Button
    lateinit var image: ImageView
    lateinit var uploadBtn:Button
    private  var filePath: Uri? = null
    internal var storage: FirebaseStorage? = null
    internal var storageReference: StorageReference? = null
    private var filename: String = ""
    lateinit var docID:String
    private var progress: Double = 0.0
    private lateinit var db:CollectionReference
    private lateinit var dataID:String
    lateinit var date : EditText
    lateinit var month : EditText
    lateinit var year : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dataReference = FirebaseFirestore.getInstance()
        username = findViewById(R.id.username)
        fullname = findViewById(R.id.fullname)
        date = findViewById(R.id.Date)
        month = findViewById(R.id.Month)
        year = findViewById(R.id.Year)
        university = findViewById(R.id.university)
        password = findViewById(R.id.password)
        repassword = findViewById(R.id.password2)
        submitBtn = findViewById(R.id.signupBtn)
        image = findViewById(R.id.imageView)
        uploadBtn = findViewById(R.id.uploadBtn)


        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference


        val loadImage = registerForActivityResult(ActivityResultContracts.GetContent()){
                uri: Uri? ->
            image.setImageURI(uri)
            filePath = uri
        }

        uploadBtn.setOnClickListener {
            loadImage.launch("image/*")
        }
        submitBtn.setOnClickListener {
            db = dataReference.collection("userData")
            dataID = db.document().id
            docID = dataID
            filename = docID
            filePath?.let { it1 -> uploadImage(it1) }

            submitRegisData()
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }



    }


    private fun submitRegisData(){
        val usernameText = username.text.toString()
        val fullnameText = fullname.text.toString()
        val DateText = date.text.toString()
        val MonthText = month.text.toString()
        val YearText = year.text.toString()
        val universityText = university.text.toString()
        val passwordText = password.text.toString()
        val repasswordText = repassword.text.toString()


        if(usernameText.isEmpty()){
            username.error = "Please enter your username"
            return
        } else if(passwordText.isEmpty()){
                password.error = "Please enter your password"
            return
        }
            else if(fullnameText.isEmpty()){
                    fullname.error = "Please enter your fullname"
            return
        }
                else if(universityText.isEmpty()){
                        university.error = "Please enter your University"
            return
        }
                    else if(repasswordText.isEmpty()){
                            repassword.error = "Please enter your password again"
            return
        }

        var flg=0
        if(MonthText.equals("0") || MonthText> 12.toString()){
            month.error = "Wrong input"
            return
        } else if(MonthText.equals("1") || MonthText.equals("3")
            || MonthText.equals("5") || MonthText.equals("7")
            || MonthText.equals("8") || MonthText.equals("10")
            || MonthText.equals("12")){
            flg=1
        }

        if(flg==1){
            return
        }else if(flg==0){
            date.error  = "Wrong input"
            return
        }

        if(passwordText!=repasswordText){
            password.error = "Password does not match"
            repassword.error = "Password does not match"

            password.text.clear()
            repassword.text.clear()
            return
        }
        var DobText = DateText+"/"+MonthText+"+"+YearText
        val userInfoData = UserData(dataID, usernameText,
            passwordText,fullnameText,DobText,universityText)
        db.add(userInfoData)
            .addOnSuccessListener { result ->
                Toast.makeText(applicationContext,"Register Successfully",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
            }
    }
    private fun uploadImage(content:Uri){
        if (content != null){
            Toast.makeText(applicationContext, "Uploading...", Toast.LENGTH_SHORT).show()
            val imageRef = storageReference!!.child(filename+"/"+"photo")
            imageRef.putFile(content!!)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Image uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Log.d("URL uploaded", "FAIL TO upload")
                    Toast.makeText(applicationContext, "Fail to upload", Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {taskSnapshot ->
                    progress = 100.0*taskSnapshot.bytesTransferred/taskSnapshot.totalByteCount
                    Toast.makeText(applicationContext, "Uploaded "+ progress.toInt()+"% " , Toast.LENGTH_SHORT).show()
                }
        } else{
            Log.d("Upload image","filePath is null")
        }
    }



}


