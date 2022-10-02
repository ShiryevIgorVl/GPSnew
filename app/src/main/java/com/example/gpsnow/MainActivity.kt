package com.example.gpsnow

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.gpsnow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LocListenerInterfase {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private lateinit var myLocation: MyLocation
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initGPSServis()
        requestPermissionListener()
        chekRequestPemission()

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
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
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
    }

}