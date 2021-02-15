package at.laubi.weatherapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.laubi.weatherapp.data.TemperatureMeasurement
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

class DiagramActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagram)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)

            intent.extras?.let {
                it.getString("location")?.also { title = it }
            }
        }

        val chart = getConfiguredChart()
        chart.data = LineData(calcDataSet())
        chart.invalidate()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getConfiguredChart(): LineChart {
        val lineChart = findViewById<LineChart>(R.id.chart)

        lineChart.run {
            setDrawGridBackground(false)
            minOffset = 20f
            description.isEnabled = false
        }

        lineChart.axisLeft.run {
            axisMinimum = 0f
            axisMaximum = 1500f
            isEnabled = false
        }

        lineChart.axisRight.run {
            setDrawAxisLine(false)
            setDrawGridLines(false)
            isEnabled = false
        }

        lineChart.xAxis.run {
            isEnabled = false
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setAvoidFirstLastClipping(true)
        }

        return lineChart
    }

    private fun makeTimeseries(): Triple<List<Entry>, List<Entry>, List<Entry>> {
        val dataPoints: List<TemperatureMeasurement> = intent.getSerializableExtra("dataPoints") as List<TemperatureMeasurement>

        val temperatureEntries = ArrayList<Entry>()
        val humidities = ArrayList<Entry>()
        val pressures = ArrayList<Entry>()

        for ((index, sortedDataPoint) in dataPoints.withIndex()) {
            val x = index.toFloat()
            val temperature = sortedDataPoint.temperature!!.toFloat()
            val humidity = sortedDataPoint.humidity!!.toFloat()
            val pressure = sortedDataPoint.pressure!!.toFloat()

            pressures += Entry(x, pressure)
            temperatureEntries += Entry(x, temperature)
            humidities += Entry(x, humidity)
        }

        return Triple(temperatureEntries, humidities, pressures)
    }

    private fun calcDataSet(): List<LineDataSet> {
        val (temperature, humidity, pressure) = makeTimeseries();

        val tempLine = LineDataSet(temperature, "Temperature")
        tempLine.color = Color.RED
        tempLine.axisDependency = YAxis.AxisDependency.RIGHT
        tempLine.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String = "$value °C"
        }

        val humLine = LineDataSet(humidity, "Humidity")
        humLine.color = Color.BLUE
        humLine.axisDependency = YAxis.AxisDependency.RIGHT
        humLine.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String = "$value %"
        }

        val pressureLine = LineDataSet(pressure, "Pressure")
        pressureLine.color = Color.GRAY
        pressureLine.axisDependency = YAxis.AxisDependency.LEFT
        pressureLine.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String = "$value hPa"
        }

        return listOf(tempLine, humLine, pressureLine)
    }
}