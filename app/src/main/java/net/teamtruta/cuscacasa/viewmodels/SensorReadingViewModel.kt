package net.teamtruta.cuscacasa.viewmodels

import android.app.Application
import android.graphics.DiscretePathEffect
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.teamtruta.cuscacasa.R
import net.teamtruta.cuscacasa.db.SensorReading
import net.teamtruta.cuscacasa.repository.SensorReadingRepository
import net.teamtruta.cuscacasa.db.AppDatabase
import net.teamtruta.cuscacasa.repository.DataRefreshError
import org.joda.time.DateTime
import java.sql.SQLException

class SensorReadingViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = SensorReadingViewModel::class.simpleName

    private val viewModelJob = SupervisorJob()
    //private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val repository : SensorReadingRepository = SensorReadingRepository(
        AppDatabase.getDatabase(application)
    )
    var readings : LiveData<List<SensorReading>> = repository.readings
    lateinit var readingDateTime : LiveData<DateTime>
    private val app = application

    fun refreshData(){
        viewModelScope.launch {
            try {
                repository.refreshReadings(app.resources.getString(R.string.connection_string))
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
                readingDateTime = MutableLiveData(DateTime.now())
            } catch (error: DataRefreshError) {
                _eventNetworkError.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "Couldn't get readings from cloud.")
            }
        }
    }


    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

}