package com.maycc.location

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fineLocation   = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val CODE_PERMISSION_LOCATION = 100

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = FusedLocationProviderClient(this)
        initLocationRequest()
    }

    private fun initLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @SuppressLint("MissingPermission")
    private fun getConstantLocation() {
        locationCallback = object: LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                var latitude: Double
                var longitude: Double

                for (location in locationResult?.locations!!) {
                    latitude = location.latitude
                    longitude = location.longitude

                    Toast.makeText(applicationContext, "$latitude  $longitude", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onStart() {
        super.onStart()

        if (validatePermissions()) {
            // getUltimateLocation()
            getConstantLocation()
        } else {
            askPermissions()
        }
    }

    override fun onPause() {
        super.onPause()

        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun validatePermissions(): Boolean {
        val isPermissionCoarseLocationGranted = ActivityCompat.
                checkSelfPermission(this, coarseLocation) == PackageManager.PERMISSION_GRANTED
        val isPermissionFineLocationGranted   = ActivityCompat.
                checkSelfPermission(this, fineLocation) == PackageManager.PERMISSION_GRANTED

        return isPermissionCoarseLocationGranted && isPermissionFineLocationGranted
    }

    private fun askPermissions() {
        val shouldShowMsj = ActivityCompat.shouldShowRequestPermissionRationale(this, coarseLocation)

        if (shouldShowMsj) {
            showAlertDialogWithExplication("""Necesitamos conocer tu ubicación para que
                sepas en donde te encuentras
            """.trimMargin())
        } else {
            requestThePermissions()
        }
    }

    private fun showAlertDialogWithExplication(msj: String) {
        AlertDialog.Builder(this).apply {
            setMessage(msj)
            setPositiveButton("OK") { _, _ ->
                requestThePermissions()
            }
            setNegativeButton("CANCELAR", null)
            
            show()
        }
    }

    private fun requestThePermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(fineLocation, coarseLocation), CODE_PERMISSION_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (i in grantResults) {
            Log.d("GRANT_RESULTS", i.toString())
        }

        when (requestCode) {
            CODE_PERMISSION_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permiso no otorgado", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUltimateLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener(this, object: OnSuccessListener<Location> {

            override fun onSuccess(location: Location?) {
                if (location != null) {
                    val latitude = location.latitude.toString()
                    val longitude      = location.longitude.toString()

                    tvLocation.text = "$latitude $longitude"
                } else {
                    Toast.makeText(applicationContext, "No se ha podido obtener la última ubicación", Toast.LENGTH_SHORT).show()
                }
            }
        } )
    }
}
