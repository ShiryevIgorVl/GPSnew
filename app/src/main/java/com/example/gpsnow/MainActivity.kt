package com.example.gpsnow

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.gpsnow.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), LocListenerInterfase {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private lateinit var myLocation: MyLocation
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var sharedPreferences: SharedPreferences

    val SHARED_NAME = "sharedName"
    val SHARED_LATITUDE = "sharedLatitude"
    val SHARED_LONGITUDE = "sharedLongitude"

    var _latitude: Double = 0.0
    var _longitude: Double = 0.0
    
    val TAG = "MyTag"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initGPSServis()
        initSharedPref()

        requestPermissionListener()
        chekRequestPemission()

        onClickButtonSave()
        onClickButtonCalculate()

    }

    private fun initSharedPref() {
        sharedPreferences = getSharedPreferences(SHARED_NAME, MODE_PRIVATE)
    }

    //Инициализируем менеджер локациии и подключаем setLocListenerInterface у классу MyLocatiion
    private fun initGPSServis() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        myLocation = MyLocation()
        myLocation.setLocListenerInterface(this)
    }


    private fun requestPermissionListener() {

        pLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    Toast.makeText(this, "разрениние на GPS есть", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "разрениние на GPS нет", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun chekRequestPemission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    2f,
                    myLocation
                )
            }
            else -> {
                pLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    override fun onGetLocation(location: Location) {
        binding.latitude.text = location.latitude.toString()
        binding.longitude.text = location.longitude.toString()
        binding.speed.text = location.speed.toString()
        binding.accuracy.text = location.accuracy.toString()

        _latitude = location.latitude
        _longitude = location.longitude
    }

    fun setLatitudeSP(d: Double) {
        val spEditor = sharedPreferences.edit()
        spEditor.putFloat(SHARED_LATITUDE, d.toFloat())
        spEditor.apply()
    }

    fun setLongitudeSP(d: Double) {
        val spEditor = sharedPreferences.edit()
        spEditor.putFloat(SHARED_LONGITUDE, d.toFloat())
        spEditor.apply()
    }

    private fun getLatitudeSP(): Float {
        return sharedPreferences.getFloat(SHARED_LATITUDE, 0f)
    }

    private fun getLongitudeSP(): Float {
        return sharedPreferences.getFloat(SHARED_LONGITUDE, 0f)
    }


    fun onClickButtonSave() {
        binding.send.setOnClickListener {
            Log.d(TAG, "onClickButtonSave: lat = ${_latitude}")
            Log.d(TAG, "onClickButtonSave: lot = ${_longitude}")

            setLatitudeSP(_latitude)
            setLongitudeSP(_longitude)

            Log.d(TAG, "onClickButtonSave: ")
            getLatitudeSP()
            getLongitudeSP()

            Log.d(TAG, "onClickButtonSave: getLatitudeSP() = ${getLatitudeSP()}")
            Log.d(TAG, "onClickButtonSave: getLongitudeSP() = ${getLongitudeSP()}")

        }
    }

    fun onClickButtonCalculate() {
        binding.calculate.setOnClickListener {
            val arrayDistance: FloatArray = floatArrayOf(0f)
            Log.d(TAG, "onClickButtonCalculate: завелось")
            Location.distanceBetween(
                getLatitudeSP().toDouble(),
                getLongitudeSP().toDouble(),
                _latitude,
                _longitude,
                arrayDistance
            )
            Log.d(TAG, "onClickButtonCalculate: ${arrayDistance[0]}")
            binding.tvDistanceSet.text = arrayDistance[0].toString()
            setLatitudeSP(_latitude)
            setLongitudeSP(_longitude)
        }
    }

}