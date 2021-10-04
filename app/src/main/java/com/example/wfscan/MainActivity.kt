package com.example.wfscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE : Int = 77

    private lateinit var wifiManager: WifiManager
    private var scanResultsList = ArrayList<ScanResult>()

    private var wifiBroadCastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent)
        {
            Log.d("MY_DEBUG", "Wi-Fi broad cast receiver got data.")

            // It's advised to unregister receiver to prevent resource consumption.
            unregisterReceiver(this)

            scanResultsList = wifiManager.scanResults as ArrayList<ScanResult>
            var listViewItems = mutableListOf<String>()
            for(scanResult in scanResultsList)
            {
                Log.d("MY_DEBUG", "Scanned network: ${scanResult.SSID}")
                if(scanResult.SSID == "")
                {
                    listViewItems.add("Unknown SSID")
                }
                else
                {
                    listViewItems.add(scanResult.SSID)
                }
            }

            var arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listViewItems)
            lv_wi_fi_networks.adapter = arrayAdapter

            lv_wi_fi_networks.setOnItemClickListener  { parent, view, itemIndex, id ->
                Log.d("MY_DEBUG", "List item $itemIndex on click." )

                val showListItemIntent = Intent(context, ListItemActivity::class.java)
                showListItemIntent.putExtra("SSID", scanResultsList[itemIndex].SSID)
                showListItemIntent.putExtra("BSSID", scanResultsList[itemIndex].BSSID)
                showListItemIntent.putExtra("level", scanResultsList[itemIndex].level)
                showListItemIntent.putExtra("capabilities", scanResultsList[itemIndex].capabilities)
                showListItemIntent.putExtra("frequency", scanResultsList[itemIndex].frequency)
                startActivity(showListItemIntent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.getRequiredPermissions()

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if(!wifiManager.isWifiEnabled)
        {
            wifiManager.isWifiEnabled = true
        }

        registerReceiver(this.wifiBroadCastReceiver, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        wifiManager.startScan()

        Log.d("MY_DEBUG", "Wi-Fi scanning started.")
    }

    private fun requestWifiEnabling()
    {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Wi-Fi enabling request")
        alertDialogBuilder.setMessage("Turn on Wi-Fi, then press Ok.")
        alertDialogBuilder.setPositiveButton("Ok", null)
        alertDialogBuilder.setNeutralButton("Cancel", null)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getRequiredPermissions()
    {
        var permissionManager = PermissionsManager(this, this.PERMISSIONS_REQUEST_CODE)
        var permissions = listOf( Manifest.permission.ACCESS_COARSE_LOCATION,
                                              Manifest.permission.CHANGE_WIFI_STATE,
                                              Manifest.permission.ACCESS_FINE_LOCATION,
                                              Manifest.permission.ACCESS_WIFI_STATE )
        while(!permissionManager.areGranted(permissions))
        {
            permissionManager.showAlertDialogWithPermissionsRequest(permissions)
        }

        Log.d("MY_DEBUG", "Required permissions are granted.")
    }
}