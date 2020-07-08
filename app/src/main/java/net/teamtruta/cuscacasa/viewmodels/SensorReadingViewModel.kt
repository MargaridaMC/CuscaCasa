package net.teamtruta.cuscacasa.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.teamtruta.cuscacasa.R
import net.teamtruta.cuscacasa.db.SensorReading
import net.teamtruta.cuscacasa.repository.SensorReadingRepository
import net.teamtruta.cuscacasa.db.AppDatabase
import java.io.IOException

class SensorReadingViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = SensorReadingViewModel::class.simpleName

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob)
    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val repository : SensorReadingRepository
    var readings : LiveData<List<SensorReading>>? = null

    init {
        val sensorReadingDao = AppDatabase.getDatabase(application).sensorReadingDao()
        repository =
            SensorReadingRepository(
                sensorReadingDao
            )

        if(repository.readings == null || repository.readings?.value.isNullOrEmpty()){
            viewModelScope.launch {
                try{
                    repository.refreshReadings(application.resources.getString(R.string.connection_string))
                } catch (e : Exception){
                    e.printStackTrace()
                    Log.e(TAG , "Couldn't get readings from cloud.")
                }
            }

        }

        readings = repository.readings
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}