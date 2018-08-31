package com.maycc.location

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient

class MainActivity : AppCompatActivity() {

    private val fineLocation   = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    private val CODE_PERMISSION_LOCATION = 100

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

    private fun getLocation() {

    }
}
