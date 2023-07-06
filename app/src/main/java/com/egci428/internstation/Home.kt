package com.egci428.internstation


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var dropDown:ImageView
    lateinit var title:TextView
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout

    //////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dropDown = findViewById(R.id.imageView)
        drawerLayout = findViewById(R.id.drawerLayout)
        title = findViewById(R.id.titleText)
        navigationView = findViewById(R.id.navigaionView)
        navigationView.setNavigationItemSelectedListener(this)
        //////////////////////////////////////////////////////////////////////////////////////
        title.setText("Main Menu")
        //////////////////////////////////////////////////////////////////////////////////////
        dropDown.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        //////////////////////////////////////////////////////////////////////////////////////
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
        }
        return false
    }

}