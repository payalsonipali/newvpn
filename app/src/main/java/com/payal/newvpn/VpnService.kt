package com.payal.newvpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer

class MyVpnService : VpnService(), Runnable {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startVpn()
        return START_STICKY
    }

    private fun startVpn() {
        if (vpnInterface == null) {
            val builder = Builder()
            builder.addAddress("10.0.0.2", 32)
            builder.addRoute("0.0.0.0", 0)
            builder.setSession("newvpn")

            vpnInterface = builder.establish()

            Thread(this).start()
        }
    }

    override fun run() {
        try {
            val fileDescriptor = vpnInterface?.fileDescriptor
            val fileInputStream = FileInputStream(fileDescriptor)

            val buffer = ByteBuffer.allocate(2048)

            while (true) {
                val bytesRead = fileInputStream.read(buffer.array())

                if (bytesRead > 0) {
                    // Process the packet
                    processPacket(buffer, bytesRead)
                }

                buffer.clear()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun processPacket(buffer: ByteBuffer, bytesRead: Int) {
        // Extract relevant information from the packet
        // Send the information to App2
        Log.d("MyVpnService", "Network packet received: ${String(buffer.array(), 0, bytesRead)}")
        val packetContent = String(buffer.array(), 0, bytesRead)
        Log.d("taggg", "outside request : ${packetContent}")
        if (isHttpRequest(buffer, bytesRead)) {
            Log.d("taggg", "ishttp request : ${buffer.toString()}")
        }
//            sendInfoToApp2(buffer, bytesRead)
    }

    private fun isHttpRequest(buffer: ByteBuffer, bytesRead: Int): Boolean {
        // Sample logic (adjust as needed)
        Log.d("taggg","inside isHttpRequest")
        val requestPattern = "GET /"
        val packetContent = String(buffer.array(), 0, bytesRead)

        Log.d("taggg","packetContent : $packetContent")

        return packetContent.contains(requestPattern)
    }

//    private fun sendInfoToApp2(buffer: ByteBuffer, bytesRead: Int) {
//        val info = extractInfoFromPacket(buffer, bytesRead)
//
//        // Use an interprocess communication (IPC) mechanism to send info to App2
//        val intent = Intent("com.example.ACTION_SEND_INFO")
//        intent.putExtra("info", info)
//        sendBroadcast(intent)
//    }

    private fun extractInfoFromPacket(buffer: ByteBuffer, bytesRead: Int): String {
        // Extract relevant information from the packet
        // For demonstration purposes, let's assume the info is a simple string
        return "Info: " + String(buffer.array(), 0, bytesRead)
    }
}
