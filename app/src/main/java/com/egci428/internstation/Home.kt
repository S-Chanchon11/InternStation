package com.egci428.internstation


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egci428.internstation.Data.CompanyData
import com.egci428.internstation.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.random.Random


class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    lateinit var dropDown:ImageView
    lateinit var title:TextView
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CompanyAdapter
    lateinit var dataReference: FirebaseFirestore
    lateinit var dataList: MutableList<CompanyData>
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var locationManager: LocationManager?=null
    private var locationListener: LocationListener?=null
    val num = Random.nextInt(1, 20).toString()
    val jsonURL =
        "https://internstation-47c4f-default-rtdb.firebaseio.com/Internship%20Company/Company%20"+num+".json"

    private val client = OkHttpClient()

    private var lat:Double = 0.0
    private var lng:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dropDown = findViewById(R.id.imageView)
        drawerLayout = findViewById(R.id.drawerLayout)
        title = findViewById(R.id.titleText)
        navigationView = findViewById(R.id.navigaionView)
        recyclerView = findViewById(R.id.recyclerView)

        dataList = mutableListOf()
        adapter = CompanyAdapter(dataList)
        /*
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        */
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) {
                lat = location.latitude
                lng = location.longitude
                Log.d("===================",location.latitude.toString() + location.longitude.toString())
            }
        }

        //request_location()
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
        } else if(id==R.id.location){
            val intent = Intent(this,MapsActivity::class.java)
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

    private fun request_location() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET), 10)
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), 10)
            }
            return
        }

        locationManager!!.requestLocationUpdates("gps",1000, 0F, locationListener!!)


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==10){
            request_location()

        } else
            Log.d("Permission result","Fail")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        /*
        marker.setOnClickListener {
            val cur = LatLng(lat,lng)
            mMap.addMarker(MarkerOptions().position(cur).title("Marker at Current Location"))
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cur,8F))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cur,8F))
        }

         */
    }

    fun fetchJson(){
        val request = Request.Builder()
            .url(jsonURL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }
                    val body = response.body!!.string()

                    if(body == null) return@use
                    val gson = GsonBuilder().create()
                    val Data = gson.fromJson(body,
                        CompanyData::class.java)
                }
            }
        })
    }
}
