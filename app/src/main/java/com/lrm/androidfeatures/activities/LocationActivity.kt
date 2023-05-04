package com.lrm.androidfeatures.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.lrm.androidfeatures.databinding.ActivityLocationBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.util.Locale

class LocationActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityLocationBinding

    companion object {
        const val LOCATION_PERMISSION_CODE = 900
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { onBackPressed() }

        binding.getLocation.setOnClickListener {
            binding.locationData.text = ""
            if(hasPermission()) {
                getLocationData()
            } else {
                requestPermission()
            }
        }
    }

    private fun hasPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private fun requestPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Permission is required to get location",
            LOCATION_PERMISSION_CODE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    private fun getLocationData() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = object: LocationListener {
            override fun onLocationChanged(p0: Location) {
                getData(p0)
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,100.2f, locationListener)
    }

    fun getData(p0: Location) {
        val geoCoder = Geocoder(this@LocationActivity, Locale.getDefault())
        val addresses = geoCoder.getFromLocation(p0.latitude,p0.longitude,2)
        val address = addresses!!.get(0)

        binding.locationData.setText("${address.getAddressLine(0)} \n${address.locality}")
        openLocation(binding.locationData.text.toString())
    }

    private fun openLocation(location: String) {
        binding.mapView.setOnClickListener {
            if (binding.locationData.text.isEmpty()){
                Toast.makeText(this, "Get the Location first", Toast.LENGTH_SHORT).show()
            } else {
                val uri = Uri.parse("geo:0, 0?q=$location")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }



    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}