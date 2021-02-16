package at.laubi.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import at.laubi.weatherapp.data.MeasurementPoint
import at.laubi.weatherapp.data.VolleyWeatherRestService
import at.laubi.weatherapp.data.TemperatureMeasurement
import java.util.ArrayList

class PointsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points)

        val lv = findViewById<ListView>(R.id.measurementPoints)

        val restService = VolleyWeatherRestService(applicationContext)

        restService.requestLocations {
            val mapped = it.map(MeasurementPoint::location)
            lv.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, mapped)
        }

        lv.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val locationId = parent.getItemAtPosition(position) as String

            restService.requestDataPoints(locationId) {
                startDiagramAction(locationId, it)
            }
        }
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