package at.laubi.weatherapp.data

import java.io.Serializable

data class MeasurementPoint (val location: String? = null)

enum class SkyState(val skyStateCode: String) {
    SUNNY("0"),
    WINDY("1"),
    CLOUDY("2"),
    RAIN("3"),
    CLEAR("4"),
}

data class TemperatureMeasurement (
    val humidity: Int? = null,
    val pressure: Double? = null,
    val skyState: SkyState? = null,
    val timestamp: String? = null,
    val temperature: Double? = null
): Serializable