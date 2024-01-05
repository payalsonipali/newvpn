package com.payal.newvpn

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startVpnService()
    }

    private fun startVpnService() {
        val vpnIntent = VpnService.prepare(applicationContext)
        if (vpnIntent != null) {
            startActivityForResult(vpnIntent, 0)
        } else {
            onActivityResult(0, RESULT_OK, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            // User approved the VPN connection
            val serviceIntent = Intent(this, MyVpnService::class.java)
            startService(serviceIntent)
        } else {
            // User denied or canceled the VPN connection
            // Handle accordingly
        }
    }
}

