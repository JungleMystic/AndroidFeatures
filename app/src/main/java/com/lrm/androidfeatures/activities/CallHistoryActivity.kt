package com.lrm.androidfeatures.activities

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.lrm.androidfeatures.adapter.CallLogAdapter
import com.lrm.androidfeatures.databinding.ActivityCallHistoryBinding
import com.lrm.androidfeatures.model.CallLogModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.util.Date

class CallHistoryActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityCallHistoryBinding
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val CALL_LOG_PERMISSION_CODE = 400
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCallHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { onBackPressed() }

        recyclerView = binding.recyclerView

        if (hasPermissions()) {
            readCallLog()
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("Range")
    private fun readCallLog() {

        val callLogList: MutableList<CallLogModel> = ArrayList()

        val callLogs = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null, null,null,
            "${CallLog.Calls.LAST_MODIFIED} DESC"
        )

        if (callLogs !=  null) {
            while (callLogs.moveToNext()) {
                var name = callLogs.getString(callLogs.getColumnIndex(CallLog.Calls.CACHED_NAME))
                val number = callLogs.getString(callLogs.getColumnIndex(CallLog.Calls.NUMBER))
                val callType = callLogs.getString(callLogs.getColumnIndex(CallLog.Calls.TYPE))
                val duration = callLogs.getString(callLogs.getColumnIndex(CallLog.Calls.DURATION))
                val date = callLogs.getString(callLogs.getColumnIndex(CallLog.Calls.DATE))

                if(name == null) {
                    name = ""
                }

                var type = ""

                val fullDate = Date(date.toLong()).toString()

                when (callType.toInt()) {
                    1 -> type = "Incoming"
                    2 -> type = "Outgoing"
                    3 -> type = "Missed Call"
                    5 -> type = "Rejected"
                }

                callLogList.add(CallLogModel(name, number, type, duration, fullDate))
            }
        }

        val adapter = CallLogAdapter(callLogList)
        recyclerView.adapter = adapter
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
        EasyPermissions.hasPermissions(this, Manifest.permission.READ_CALL_LOG)

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "Permission is required to read call log",
            CALL_LOG_PERMISSION_CODE,
            Manifest.permission.READ_CALL_LOG
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