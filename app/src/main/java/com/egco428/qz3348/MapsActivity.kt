package com.egco428.qz3348

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback , SensorEventListener {
    private val REQUEST_CODE = 101

    lateinit var dataReference: DatabaseReference
    lateinit var msgList: MutableList<Message>
    private lateinit var mMap: GoogleMap
    var name = ""
    private var sensorManager: SensorManager? = null
    private  var lastUpdate: Long = 0
    val mahidol = LatLng(13.7946, 100.3234)
    var newLat = 13.7946
    var newLng = 100.3234
    var isShake = false

    var list = arrayListOf<LatLng>()
    var listOfLat = arrayListOf<Double>()
    var listOfLng = arrayListOf<Double>()
    var nowColor = Color.parseColor("#ff0000")
    val redColor = Color.parseColor("#ff0000")
    val greenColor = Color.parseColor("#33cc33")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        dataReference = FirebaseDatabase.getInstance().getReference("map")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()

        val extras = intent.extras
        name = extras.getString("text")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        add.setOnClickListener {

        }

    }

    private fun SaveData(){

        val messageId = dataReference.push().key
        val messageData = Message(messageId, name, listOfLat,listOfLng, "#ff0000")
        dataReference.child(messageId).setValue(messageData).addOnCompleteListener {
            Toast.makeText(applicationContext,"Message save successfully",Toast.LENGTH_SHORT).show()
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event)
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) , SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    private fun getAccelerometer(event: SensorEvent) {
        val values = event.values
        // Movement
        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()
        if (accel >= 6)
        //
        {
            if (actualTime - lastUpdate < 200) {
                return
            }
            lastUpdate = actualTime
            Toast.makeText(this, "Device was shuffled FAST", Toast.LENGTH_SHORT)
                    .show()
            nowColor = redColor

            newLat = newLat + 5
            newLng = newLng + 5


            isShake= true

        }
        else if(accel < 6 && accel > 3){
            if (actualTime - lastUpdate < 200) {
                return
            }
            lastUpdate = actualTime
            Toast.makeText(this, "Device was shuffled SLOW", Toast.LENGTH_SHORT)
                    .show()
            nowColor = greenColor
            newLat = newLat + 2
            newLng = newLng + 2
            isShake =  true

            //else {
        }
//            Toast.makeText(this, "Device was not shuffled", Toast.LENGTH_SHORT)
//                    .show()
//        }

        if(newLat > 85){
            newLat = -85.0
        }

        if(newLng > 179.999989){
            newLng = -179.999989
        }
        colorText.setBackgroundColor(nowColor)
        lat.setText(newLat.toString())
        lon.setText(newLng.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_add -> {
                //add data to database
                SaveData()
                finish()
            }
            R.id.main_listview -> {

            }
        }

        return super.onOptionsItemSelected(item)

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        list.add(mahidol)
        // Add a marker in Sydney and move the camera
        mMap.addMarker(MarkerOptions().position(mahidol).title("Marker in Mahidol"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mahidol,5f))

        add.setOnClickListener {
            Log.d("add", "add")
            if(isShake){
                list.add(LatLng(newLat, newLng))
                isShake = false
                listOfLat.add(newLat)
                listOfLng.add(newLng)

                Log.d("add", "add new lat long")
                mMap.addMarker(MarkerOptions().position(LatLng(newLat,newLng)).title(LatLng(newLat,newLng).toString()))
                mMap.animateCamera(CameraUpdateFactory.newLatLng(LatLng(newLat,newLng)))
                mMap.addPolyline(PolylineOptions()
                        .add(list.get(list.count() - 2), list.last())
                        .width(8f)
                        .color(nowColor)
                )
                Log.d("add", "add line")

            }



        }


    }
}
