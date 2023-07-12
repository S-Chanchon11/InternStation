package com.egci428.internstation


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egci428.internstation.Data.CompanyData


import com.google.android.gms.maps.GoogleMap
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request


class Home : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SensorEventListener {

    lateinit var dropDown: ImageView
    lateinit var title: TextView
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var recyclerView: RecyclerView
    lateinit var userID:String
    lateinit var dataReference: FirebaseFirestore
    lateinit var dataList: MutableList<CompanyData>
    lateinit var office: Location
    lateinit var curr: Location
    lateinit var nearbyArray:ArrayList<Float>
    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0
    lateinit var syncBtn: ImageView
    lateinit var progressBar : ProgressBar
    var lat: Double = 0.0
    var long:Double = 0.0
    lateinit var adapter: CompanyAdapter


    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private val client = OkHttpClient()

    private var progress = 0

    val jsonURL =
        "https://internstation-47c4f-default-rtdb.firebaseio.com/.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dataList = mutableListOf()
        nearbyArray = ArrayList()
        progressBar = findViewById(R.id.progressBar)
        dropDown = findViewById(R.id.imageView)
        drawerLayout = findViewById(R.id.drawerLayout)
        title = findViewById(R.id.titleText)
        navigationView = findViewById(R.id.navigaionView)
        recyclerView = findViewById(R.id.recyclerView)
        syncBtn = findViewById(R.id.syncBtn)
        dataReference = FirebaseFirestore.getInstance()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()

        navigationView.setNavigationItemSelectedListener(this)
        title.setText("Main Menu")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val linearLayoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                lat = location.latitude
                long = location.longitude
                Log.d(
                    "Location in listener ",
                    lat.toString() + "," + long.toString()
                )

                if(progressBar.visibility==0){
                    loadJson()
                }
                progressBar.setVisibility(View.INVISIBLE)

            }
        }
        progressBar.setVisibility(View.VISIBLE)

        if(lat==0.0&&long==0.0){
            request_location()
        }



        curr = Location("current")
        office = Location("office")

        userID = intent.getStringExtra("userID").toString()
        Log.d("onCreate of Home",userID)


        dropDown.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        syncBtn.setOnClickListener {
            request_location()
            loadJson()

        }

        if(userID.isNullOrEmpty()){
            userID = intent.getStringExtra("userID").toString()
            Log.d("userID is null",userID)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.applied) {
            val intent = Intent(this, Applied::class.java)
            intent.putExtra("userID",userID)
            startActivity(intent)
            return true
        } else if (id == R.id.profile) {
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("userID",userID)
            startActivity(intent)
            return true
        } else if (id == R.id.logout) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            return true
        }
        return false
    }


    private fun request_location() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET), 10)
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 10
                )
            }
            return
        }

        locationManager!!.requestLocationUpdates("gps", 15000, 0F, locationListener!!)
        Log.d("Location requested",lat.toString())

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            request_location()
        } else
            Log.d("Permission result", "Fail")
    }

    private fun loadJson() {
        Log.d("Tag", "loadJson")
        val loadJsonAsync = object : AsyncTask<String, String, String>() {

            override fun onPreExecute() {
                Toast.makeText(this@Home, "Please wait", Toast.LENGTH_SHORT).show()
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result);

                var companyObj: List<CompanyData>
                companyObj = Gson().fromJson<List<CompanyData>>(result,
                    object : TypeToken<List<CompanyData>>() {}.type)

                calculateDistance(companyObj)
                companyObj = companyObj.sortedBy { it.distance  }

                adapter = CompanyAdapter(companyObj)
                recyclerView.adapter = adapter
                adapter.setOnItemClickListener(object : CompanyAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {

                        val dataPos = companyObj[position]
                        showDetail(dataPos,position)

                        Toast.makeText(this@Home, "Cicked on $position", Toast.LENGTH_SHORT).show()
                    }
                })
                adapter.notifyDataSetChanged()
                Log.d("Tag", "Load to Adapter")
            }

            override fun doInBackground(vararg params: String): String {
                val builder = Request.Builder()
                builder.url(params[0])
                val request = builder.build()
                try {
                    val response = client.newCall(request).execute()
                    return response.body!!.string()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return ""
            }

        }

        val url_get_data = StringBuilder()
        url_get_data.append(jsonURL)
        loadJsonAsync.execute(url_get_data.toString())
    }

    private fun showDetail(company: CompanyData, position: Int){
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("companyName", company.company)
        intent.putExtra("jobOffer", company.job)
        intent.putExtra("companyDes", company.description)
        intent.putExtra("companyQualif", company.qualification)
        intent.putExtra("companyBenefit", company.benefit)
        intent.putExtra("companyDuration",company.duration)
        intent.putExtra("companyLat", company.lat)
        intent.putExtra("companyLong",company.long)
        intent.putExtra("userID",userID)
        Log.d("showdetail()","finished send intent")
        setResult(Activity.RESULT_OK,intent)
        startActivityForResult(intent,101)
    }

    private fun calculateDistance(companyObj: List<CompanyData>){

        Log.d("============", lat.toString() + "  " + long.toString())
        curr.latitude = lat!!.toDouble()
        curr.longitude = long!!.toDouble()
        var i=0
        while (i<companyObj.size){
            office.latitude = companyObj[i].lat
            office.longitude = companyObj[i].long
            val distance = curr.distanceTo(office)/1000
            companyObj[i].distance = distance.toDouble()
            Log.d("calculateDistance",companyObj[i].company +"     "+ distance.toString())
            i++
        }


    }

    private fun getAccelerometer(event: SensorEvent){


        val values = event.values

        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel = (x*x + y*y +z*z)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()

        if(accel >= 1.8){
            if(actualTime-lastUpdate <200){
                return
            }
            lastUpdate = actualTime

            loadJson()

        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onPause(){
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume(){
        super.onResume()
        sensorManager!!.registerListener(this,sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)
    }

}


