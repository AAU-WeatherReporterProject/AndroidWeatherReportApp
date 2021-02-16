package at.laubi.weatherapp.data

import java.util.concurrent.CompletableFuture

interface WeatherRestService {
    fun requestLocations(): CompletableFuture<List<MeasurementPoint>>
    fun requestDataPoints(key: String): CompletableFuture<List<TemperatureMeasurement>>
}