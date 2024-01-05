package com.payal.newvpn1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.payal.newvpn1.ui.theme.Newvpn1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fetchData = findViewById<Button>(R.id.fetch)

        fetchData.setOnClickListener {
            Log.d("taggg", "fetch button clicked")
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    fetchData()
                } catch (e: Exception) {
                    // Handle exceptions
                    Log.e("taggg", "Exception occurred", e)
                }
            }
        }

    }


// Inside your MainActivity class

    private fun fetchData() {
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://dummy.restapiexample.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Creating a Call object
        val call: Call<Any> = apiService.product

        // Asynchronous execution of the request
        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val product = response.body()
                    Log.d("taggg", "product : $product")
//                    if (product != null) {
//                        Log.d("taggg", "Product title: ${product.title}, Price: ${product.price}")
//                    }
                } else {
                    // Handle unsuccessful response
                    Log.d("taggg", "Unsuccessful response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                // Handle failure
                Log.e("taggg", "API call failed", t)
            }
        })
    }


}

interface ApiService {
    @get:GET("api/v1/employees/")
    val product: Call<Any>
}

data class Product(
    val title: String,
    val price: Double
)
