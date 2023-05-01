package com.lrm.androidfeatures.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.lrm.androidfeatures.adapter.ContactsAdapter
import com.lrm.androidfeatures.databinding.ActivityContactsBinding
import com.lrm.androidfeatures.model.Contacts
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.EasyPermissions.hasPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class ContactsActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityContactsBinding
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val CONTACTS_PERMISSION_CODE = 300
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView

        if (hasPermissions()) {
            readContacts()
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("Range")
    private fun readContacts() {

        val contactsList: MutableList<Contacts> = ArrayList()

        var contacts = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )

        if (contacts != null) {
            while (contacts.moveToNext()) {
                val name =
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                contactsList.add(Contacts(name, number))

            }
            contacts.close()

            var adapter = ContactsAdapter(this@ContactsActivity, contactsList)

            recyclerView.adapter = adapter

            adapter.setOnItemClickListener(object: ContactsAdapter.onItemClickListener {
                override fun onItmeClick(position: Int) {

                    val contact = contactsList[position]
                    val number = contact.number

                    val clipBoard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Number", number.trim())
                    clipBoard.setPrimaryClip(clip)

                    Toast.makeText(this@ContactsActivity, "Number copied ${number.trim()}", Toast.LENGTH_SHORT).show()
                }
            })

            binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(p0: String?): Boolean {

                    contactsList.clear()

                    contacts = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
                    Array(1) { "%$p0%" },
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                    )

                    if (contacts != null) {
                        while (contacts!!.moveToNext()) {
                            val name =
                                contacts!!.getString(contacts!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            val number =
                                contacts!!.getString(contacts!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            contactsList.add(Contacts(name, number))
                        }
                        contacts!!.close()
                        adapter = ContactsAdapter(this@ContactsActivity, contactsList)
                        recyclerView.adapter = adapter
                        adapter.setOnItemClickListener(object: ContactsAdapter.onItemClickListener {
                            override fun onItmeClick(position: Int) {
                                val contact = contactsList[position]
                                val number = contact.number

                                val clipBoard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Number", number.trim())
                                clipBoard.setPrimaryClip(clip)

                                Toast.makeText(this@ContactsActivity, "Number copied ${number.trim()}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    return true
                }

            })
        }
    }

    private fun hasPermissions() =
        EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS)

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "Permission is required to read contacts",
            CONTACTS_PERMISSION_CODE,
            Manifest.permission.READ_CONTACTS
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

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}