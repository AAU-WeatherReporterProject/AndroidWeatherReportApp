package at.laubi.weatherapp.data

data class Metadata(val key: String? = null)

data class MeasurementPoint (val location: String? = null)

enum class SkyState(val skyStateCode: String) {
    SUNNY("0"),
    WINDY("1"),
    CLOUDY("2"),
    RAIN("3"),
    CLEAR("4"),
}

class TemperatureData(
    val metadata: Metadata? = null,
    val measurements: List<TemperatureMeasurement>? = null
)

class TemperatureMeasurement(
    val humidity: Int? = null,
    val pressure: Double? = null,
    val skyState: SkyState? = null,
    val timestamp: String? = null,
    val temperature: Double? = null
)