package com.lrm.androidfeatures.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lrm.androidfeatures.databinding.ActivityCallBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class CallActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityCallBinding

    private var phoneNumber = ""

    companion object {
        const val CALL_PERMISSION_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.callButton.setOnClickListener {

            if (hasPermissions()) {
                phoneNumber = binding.phoneNumber.text.trim().toString()

                if (phoneNumber.isEmpty()) {
                    Toast.makeText(this@CallActivity, "Enter Phone Number", Toast.LENGTH_SHORT).show()
                } else {
                    makeCall(phoneNumber)
                }
            } else {
                requestPermissions()
            }
        }

    }

    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            SettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
    }

    private fun hasPermissions() =
        EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE)

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "Permission is required to make call",
            CALL_PERMISSION_CODE,
            Manifest.permission.CALL_PHONE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //EasyPermissions handles the request result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}