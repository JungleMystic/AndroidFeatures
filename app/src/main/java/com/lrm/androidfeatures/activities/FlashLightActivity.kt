package com.lrm.androidfeatures.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lrm.androidfeatures.R
import com.lrm.androidfeatures.databinding.ActivityFlashLightBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.EasyPermissions.hasPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class FlashLightActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityFlashLightBinding

    companion object {
        const val CAMERA_PERMISSION_CODE = 700
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlashLightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (hasPermissions()) {
            switchFlashLight()
        } else {
            requestPermissions()
        }
    }

    private fun switchFlashLight() {
        var light: Boolean
        var switch = "On"

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        binding.flashImage.setOnClickListener {
            if (hasPermissions()) {
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    if (switch == "On") {
                        light = true
                        switch = "Off"
                        binding.flashImage.setImageResource(R.drawable.flash_on)
                    } else {
                        light = false
                        switch = "On"
                        binding.flashImage.setImageResource(R.drawable.flash_off)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraManager.cameraIdList[0], light)
                    }
                }

            } else {
                requestPermissions()
            }
        }
    }

    private fun hasPermissions() = EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "Permission is required to turn on flashlight",
            CAMERA_PERMISSION_CODE,
            Manifest.permission.CAMERA
        )
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

        EasyPermissions
    }
}