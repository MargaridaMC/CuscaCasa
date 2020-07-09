package net.teamtruta.cuscacasa.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import net.teamtruta.cuscacasa.R
import net.teamtruta.cuscacasa.viewmodels.SensorReadingViewModel


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.simpleName
    private lateinit var sensorReadingViewModel: SensorReadingViewModel
    private lateinit var activity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //RoomExplorer.show(this, AppDatabase::class.java, "AppDatabase")

        /*val database = AppDatabase.getDatabase(this)
        val repository = SensorReadingRepository(database)
        val readings = repository.readings
        val value = readings.value*/

        activity = this
        sensorReadingViewModel = ViewModelProvider(activity).get(SensorReadingViewModel::class.java)
        text_view.text = getString(R.string.fetching_data)
        sensorReadingViewModel.readings.observe(this, Observer { readings ->
            Log.d(TAG, "Live Data observed")
            text_view.text = readings.map{it.toString()}.toString()
        })

        // Observer for the network error.
        sensorReadingViewModel.eventNetworkError.observe(this, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    /**
     * Method for displaying a Toast error message for network errors.
     */
    private fun onNetworkError() {
        if(!sensorReadingViewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            sensorReadingViewModel.onNetworkErrorShown()
        }
    }

    fun refreshData(view: View) {
        sensorReadingViewModel.refreshData()
    }

}