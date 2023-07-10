package com.egci428.internstation

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class Register : AppCompatActivity() {

    lateinit var dataReference:FirebaseFirestore
    lateinit var username: EditText
    lateinit var fullname: EditText
    lateinit var Dob: EditText
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dataReference = FirebaseFirestore.getInstance()
        username = findViewById(R.id.username)
        fullname = findViewById(R.id.fullname)
        Dob = findViewById(R.id.DateOfBirth)
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
        val DobText = Dob.text.toString()
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
        if(passwordText!=repasswordText){
            password.error = "Password does not match"
            repassword.error = "Password does not match"

            password.text.clear()
            repassword.text.clear()
            return
        }

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


