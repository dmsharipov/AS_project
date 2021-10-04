package com.example.wfscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_list_item.*

class ListItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_item)

        var arguments = intent.extras
        if (arguments != null)
        {
            Log.d("MY_DEBUG", "ListItemActivity: intent arguments transmitted." )

            var listViewItems = mutableListOf<String>()
            listViewItems.add("SSID = ${arguments["SSID"]}")
            listViewItems.add("BSSID = ${arguments["BSSID"].toString()}")
            listViewItems.add("Signal level = ${arguments["level"]}")
            listViewItems.add("Frequency = ${arguments["frequency"]}")
            listViewItems.add("Capabilities = ${arguments["capabilities"]}")

            var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listViewItems)
            lv_network_detailed.adapter = arrayAdapter
        }
    }
}