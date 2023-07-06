package com.egci428.internstation


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egci428.internstation.Data.CompanyData
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var dropDown:ImageView
    lateinit var title:TextView
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CompanyAdapter
    lateinit var dataReference: FirebaseFirestore
    lateinit var dataList: MutableList<CompanyData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dropDown = findViewById(R.id.imageView)
        drawerLayout = findViewById(R.id.drawerLayout)
        title = findViewById(R.id.titleText)
        navigationView = findViewById(R.id.navigaionView)
        recyclerView = findViewById(R.id.recyclerView)
        dataList = mutableListOf()

        dataReference = FirebaseFirestore.getInstance()
        navigationView.setNavigationItemSelectedListener(this)
        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        adapter.notifyDataSetChanged()

        title.setText("Main Menu")

        dropDown.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        readFirestore()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if(id==R.id.applied){
            val intent = Intent(this,Applied::class.java)
            startActivity(intent)
            return true
        } else if(id==R.id.profile){
            val intent = Intent(this,Profile::class.java)
            startActivity(intent)
            return true
        } else if(id==R.id.logout){
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
            return true
        }
        return false
    }

    private fun readFirestore(){
        var db = dataReference.collection("companyData")
        db.orderBy("name").get()
            .addOnSuccessListener { snapshot ->
                if(snapshot!=null){
                    dataList.clear()
                    val dataObj = snapshot.toObjects(CompanyData::class.java)
                    Log.d("LOGIN","BEGIN REQUEST DATA")
                    Log.d("LOGIN",dataObj.toString())

                    for(dataObj in dataObj){
                        dataList.add(dataObj)
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
            }
    }
}