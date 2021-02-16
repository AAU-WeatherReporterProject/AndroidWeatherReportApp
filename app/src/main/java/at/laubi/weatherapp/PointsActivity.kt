package at.laubi.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import at.laubi.weatherapp.data.MeasurementPoint
import at.laubi.weatherapp.data.VolleyWeatherRestService
import at.laubi.weatherapp.data.TemperatureMeasurement
import at.laubi.weatherapp.data.WeatherRestService
import java.util.ArrayList
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class PointsActivity : AppCompatActivity() {
    private lateinit var lv: ListView
    private lateinit var weatherService: WeatherRestService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points)

        lv = findViewById(R.id.measurementPoints)
        weatherService = VolleyWeatherRestService(applicationContext)

        requestLocations()

        lv.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            onListViewClick(parent, position)
        }
    }

    private fun requestLocations() {
        weatherService.requestLocations().thenAccept {
            updateListView(it)
        }.exceptionally {
            showMessage("Failed to load locations")
            null
        }
    }

    private fun onListViewClick(parent: AdapterView<*>, position: Int) {
        val locationId = parent.getItemAtPosition(position) as String

        requestMetricsForLocation(locationId)
    }

    private fun requestMetricsForLocation(locationId: String) {
        weatherService
            .requestDataPoints(locationId)
            .thenAccept() {
                startDiagramAction(locationId, it)
            }.exceptionally {
                showMessage("Failed to load metrics for this location")
                null
            }
    }

    private fun showMessage(message: String) =
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)

    private fun updateListView(dataPoints: List<MeasurementPoint>) {
        val mapped = dataPoints.map(MeasurementPoint::location)
        lv.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, mapped)
    }

    private fun startDiagramAction(
        locationId: String,
        items: List<TemperatureMeasurement>
    ) {
        val intent = Intent(applicationContext, DiagramActivity::class.java)
        intent.putExtra("location", locationId)
        intent.putExtra("dataPoints", ArrayList(items))

        startActivity(intent)
    }
}
