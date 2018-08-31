package com.maycc.location

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : AppCompatActivity() {

    private val fineLocation   = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = FusedLocationProviderClient(this)
    }

    override fun onStart() {
        super.onStart()

        if (validatePermissions()) {
            getLocation()
        } else {
            askPermissions()
        }
    }

    private fun validatePermissions(): Boolean {
        val isPermissionCoarseLocationGranted = ActivityCompat.
                checkSelfPermission(this, coarseLocation) == PackageManager.PERMISSION_GRANTED
        val isPermissionFineLocationGranted   = ActivityCompat.
                checkSelfPermission(this, fineLocation) == PackageManager.PERMISSION_GRANTED

        return isPermissionCoarseLocationGranted && isPermissionFineLocationGranted
    }

    private fun askPermissions() {
    }

    private fun getLocation() {

    }
}
