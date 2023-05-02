package com.lrm.androidfeatures.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lrm.androidfeatures.databinding.ActivitySmsBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class SMS_Activity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivitySmsBinding

    companion object {
        const val SMS_PERMISSION_CODE = 200
        const val CONTACT_REQUEST_CODE = 600
    }

    private var phoneNumber = ""
    private var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pickContact.setOnClickListener {
            binding.phoneNumber.text.clear()

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, CONTACT_REQUEST_CODE)
        }


        binding.sendSmsButton.setOnClickListener {

            if (hasPermissions()) {
                phoneNumber = binding.phoneNumber.text.trim().toString()
                message = binding.message.text.toString()

                if (phoneNumber.isEmpty() && message.isEmpty()) {
                    Toast.makeText(this@SMS_Activity, "Enter Phone Number and Message", Toast.LENGTH_SHORT).show()
                } else {
                    sendSMS(phoneNumber, message)
                    binding.message.text.clear()
                }
            } else {
                requestPermissions()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val contactUri = data?.data ?: return
            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val contactNum = contentResolver.query(
                contactUri,projection,null,null,null
            )

            if (contactNum?.moveToFirst()!!) {
                binding.phoneNumber.setText(contactNum.getString(0))
            }
        }
    }

    fun sendSMS(phoneNumber: String, message: String) {
        val smsManager: SmsManager
        if(Build.VERSION.SDK_INT >= 23) {
            smsManager = this.getSystemService(SmsManager::class.java)
        } else {
            smsManager = SmsManager.getDefault()
        }

        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }

    private fun hasPermissions() = EasyPermissions.hasPermissions(this, Manifest.permission.SEND_SMS)

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(this,
            "Permission is required to send sms",
            SMS_PERMISSION_CODE,
            Manifest.permission.SEND_SMS
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

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}