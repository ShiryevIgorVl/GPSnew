package com.example.gpsnow

import android.location.Location
import android.location.LocationListener

class MyLocation: LocationListener {
    private lateinit var locListenerInterfase: LocListenerInterfase
    override fun onLocationChanged(location: Location) {
        locListenerInterfase.onGetLocation(location)
//        locListenerInterfase.setLatitudeSP(location)
//        locListenerInterfase.setLongitudeSP(location)

    }

    fun setLocListenerInterface(locListenerInterfase: LocListenerInterfase) {
        this.locListenerInterfase = locListenerInterfase
    }

}