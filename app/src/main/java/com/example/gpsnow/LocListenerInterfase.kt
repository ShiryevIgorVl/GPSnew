package com.example.gpsnow

import android.location.Location

interface LocListenerInterfase {
    fun onGetLocation(location: Location)
}