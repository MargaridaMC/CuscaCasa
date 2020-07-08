package net.teamtruta.cuscacasa.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import net.teamtruta.cuscacasa.R
import net.teamtruta.cuscacasa.viewmodels.SensorReadingViewModel


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connected = isNetworkConnected()
        if(!connected){
            text_view.text = getString(R.string.no_connection)
            return
        }

        val sensorReadingViewModel = ViewModelProvider(this).get(SensorReadingViewModel::class.java)

        text_view.text = getString(R.string.fetching_data)
        sensorReadingViewModel.readings?.observe(this, Observer { readings ->
            Log.d(TAG, "Live Data observed")
            text_view.text = readings.map{it.toString()}.toString()
        })
    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }
}