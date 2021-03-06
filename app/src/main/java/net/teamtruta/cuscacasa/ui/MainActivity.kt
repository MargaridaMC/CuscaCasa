package net.teamtruta.cuscacasa.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_main.*
import net.teamtruta.cuscacasa.R
import net.teamtruta.cuscacasa.viewmodels.SensorReadingViewModel
import org.joda.time.DateTime

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.simpleName
    private lateinit var sensorReadingViewModel: SensorReadingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // App center
        AppCenter.start(
            application, "05220a0d-354b-45d1-bf3a-2e8eef02d987",
            Analytics::class.java, Crashes::class.java
        )

        sensorReadingViewModel = ViewModelProvider(this).get(SensorReadingViewModel::class.java)
        text_view.text = getString(R.string.fetching_data)
        sensorReadingViewModel.readings.observe(this, Observer {
            Log.d(TAG, "Live Data observed")
            text_view.text = "Readings from: " + DateTime.now().toString()// + readings.map{it.toString()}.toString()
            fillInChart()
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
            Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show()
            sensorReadingViewModel.onNetworkErrorShown()
        }
    }

    fun refreshData(view: View) {
        sensorReadingViewModel.refreshData()
    }

    private fun fillInChart(){

        val colors = listOf(Color.BLUE, Color.RED, Color.BLACK)
        val dataSets = mutableListOf<ILineDataSet>()

        val temperature = sensorReadingViewModel.readings.value?.mapIndexed{
                idx, reading -> Entry(idx.toFloat(), reading.temperature.toFloat())
        }
        val pressure = sensorReadingViewModel.readings.value?.mapIndexed{
                idx, reading -> Entry(idx.toFloat(), reading.pressure.toFloat())
        }
        val humidity = sensorReadingViewModel.readings.value?.mapIndexed{
                idx, reading -> Entry(idx.toFloat(), reading.relativeHumidity.toFloat())
        }

        var dataSet = LineDataSet(temperature, "Temperature")
        dataSet.axisDependency = YAxis.AxisDependency.LEFT
        //dataSet.setDrawValues(false)
        dataSet.color = colors[0]
        dataSets.add(dataSet)

        dataSet = LineDataSet(pressure, "Pressure")
        dataSet.color = colors[1]
        dataSets.add(dataSet)

        dataSet = LineDataSet(humidity, "Relative Humidity")
        dataSet.color = colors[2]
        dataSets.add(dataSet)

        chart.data = LineData(dataSets)
        chart.invalidate()
    }

}