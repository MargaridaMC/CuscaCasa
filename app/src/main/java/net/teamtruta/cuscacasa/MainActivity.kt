package net.teamtruta.cuscacasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import net.teamtruta.cuscacasa.db.AzureSQLDatabaseConnection

class MainActivity : AppCompatActivity() {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    private lateinit var connection : AzureSQLDatabaseConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connection = AzureSQLDatabaseConnection()

        uiScope.launch {
            getReadings()
        }

        // To get instance of database
        /*val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        ).build()
*/
    }

    suspend fun getReadings(){
        val connectionString = getString(R.string.connection_string)
        val readings = withContext(Dispatchers.IO){
             connection.getNReadings(connectionString, 10)
        }

        text_view.text = if(readings.isEmpty()){
            "Something went wrong..."
        } else {
            readings.map { it.toString() }.toString()
        }

    }
}