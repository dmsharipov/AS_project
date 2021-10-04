package com.example.wfscan

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsManager(private val activity: Activity, private val permissionRequestCode : Int)
{
    fun isGranted(permission : String) : Boolean
    {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun areGranted(permissions : List<String>) : Boolean
    {
        for( permission in permissions)
        {
            if(!this.isGranted(permission))
            {
                return false
            }
        }

        return true
    }

    fun findDenied(permissions : List<String>) : String?
    {
        for( permission in permissions)
        {
            if(!this.isGranted(permission))
            {
                return permission
            }
        }
        return null
    }

    fun requestPermission(permission : String)
    {
        if(this.isGranted(permission))
        {
            return
        }
        ActivityCompat.requestPermissions(activity, arrayOf(permission), this.permissionRequestCode)
    }

    fun requestPermissions(permissions : List<String>)
    {
        if(this.areGranted(permissions))
        {
            return
        }
        ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), this.permissionRequestCode)
    }

    fun showAlertDialogWithPermissionsRequest(permissions : List<String>)
    {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle("Permissions request")
        alertDialogBuilder.setMessage("Permissions are required for work.")
        alertDialogBuilder.setPositiveButton("Ok") { _, _ -> requestPermissions(permissions) }
        alertDialogBuilder.setNeutralButton("Cancel", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}