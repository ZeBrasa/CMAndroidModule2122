package com.example.invisiblefriend.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.invisiblefriend.R
import com.example.invisiblefriend.databinding.ActivityMapsBinding
import com.example.invisiblefriend.ui.CreateGroup.Markers
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationInfo : LatLng = LatLng(40.6456, -8.6529)
    private lateinit var lastLocation: Location
    private lateinit var markers: HashMap<Int, Marker_data>
    private var uid: Int = 1
    private var uname: String = "Rafael"
    private var marker_cnt : Int = 0
    // Necessário para definir quem vai poder ver o marcador
    private var destUser : Int = 1
    val db = FirebaseFirestore.getInstance()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }




    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get user last position
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    locationInfo = LatLng(location.latitude, location.longitude)
                }
            }
        //locationInfo = LatLng(fusedLocationClient.lastLocation.result.latitude,fusedLocationClient.lastLocation.result.longitude)
        //Hash Map of markers
        markers = HashMap<Int, Marker_data>()      // HashMap every Marker's Data
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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


        // Check for permissions

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            }
        } else {
            mMap.isMyLocationEnabled = true
        }



        mMap.setOnMapClickListener {
            val builder = AlertDialog.Builder(this) // Dialog builder to confirm
            builder.setMessage("Set Location Here?")
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener{
                    dialog, id ->
                val builder2 = AlertDialog.Builder(this)
                builder2.setTitle("What is Your Message?")
                val input = EditText(this)
                input.setHint("Friendly Message")
                input.inputType = InputType.TYPE_CLASS_TEXT
                builder2.setView(input)
                builder2.setPositiveButton("OK", DialogInterface.OnClickListener{
                        dialog, id ->
                    addMarkerWithInfo(it,mMap,input.text.toString(), destUser)
                })
                builder2.show()

            })
            builder.setNegativeButton("No",DialogInterface.OnClickListener{
                    dialog, id -> // else -> do nothing
            })
            builder.show()
        }

        mMap.setOnMarkerClickListener {
            if(it.isInfoWindowShown)
            {
                it.hideInfoWindow()
            }
            else
            {it.showInfoWindow()}
            true
        }


        mMap.setOnInfoWindowClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(it.title)
            builder.setMessage(it.snippet)
            builder.setPositiveButton("OK", DialogInterface.OnClickListener{
                    dialog, id ->
            })
            builder.show()
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationInfo,15.0f))

        setUpMap()
        getRelatedMarkers()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

    }



    private fun focusAt(latLng: LatLng, googleMap: GoogleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    fun getRelatedMarkers()
    {
        val markers = db.collection("markers")

        markers.whereEqualTo("toUser", uid.toString()).get().addOnSuccessListener {
                documents ->
            for (document in documents) {
                var d = document.data
                addMarkerWithInfo(LatLng(d.get("lat").toString().toDouble(),
                    d.get("lon").toString().toDouble()),
                    mMap,
                    d.get("desc").toString(),
                    uid)
            }
        }
    }



    private fun addMarkerWithInfo(latLng: LatLng, googleMap: GoogleMap, desc: String, destUser: Int)
    {
        // Adding Marker To Map
        googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(uname)
                .snippet(desc)
        )

        // Adding Marker to HashMap with all markers
        var tmp = Marker_data(latLng, uname, desc, marker_cnt, uname)
        markers.put(marker_cnt, tmp)

        val mark = hashMapOf(
            "desc" to desc,
            "fromUser" to uid.toString(),
            "lat" to latLng.latitude.toString(),
            "lon" to latLng.longitude.toString(),
            "title" to uname,
            "toUser" to destUser.toString()
        )

        db.collection("markers").add(mark)
            .addOnSuccessListener { Log.d(TAG, "Document Added Successfully") }
            .addOnFailureListener { Log.d(TAG, "Document Couldn't be added")}



        marker_cnt++ // Increment Marker Counter
    }


    class Marker_data(pos: LatLng, title: String , desc: String, id: Int, uname: String)
    {
        val pos : LatLng = LatLng(0.0, 0.0)
        val title : String = ""
        val desc : String = ""
        val id : Int = 0
        val uname : String = ""
    }
}