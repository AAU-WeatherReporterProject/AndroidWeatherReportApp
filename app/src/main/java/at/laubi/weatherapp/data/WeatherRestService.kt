package at.laubi.weatherapp.data

interface WeatherRestService {
    fun requestLocations(onSuccess: (dataPoints: List<MeasurementPoint>) -> Unit)
    fun requestDataPoints(key: String, onSuccess: (dataPoints: List<TemperatureMeasurement>) -> Unit)
}