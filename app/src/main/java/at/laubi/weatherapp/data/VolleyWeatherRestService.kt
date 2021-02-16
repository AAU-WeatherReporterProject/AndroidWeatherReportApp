package at.laubi.weatherapp.data

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.Response.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.CompletableFuture

private val GSON = Gson()

class VolleyWeatherRestService(context: Context) : WeatherRestService {
    private val queue = Volley.newRequestQueue(context)

    override fun requestLocations(): CompletableFuture<List<MeasurementPoint>> {
        val future = CompletableFuture<List<MeasurementPoint>>()

        requestArray(buildMeasurementUrl(), MeasurementPoint::class.java, future::complete, future::completeExceptionally)

        return future
    }

    override fun requestDataPoints(key: String): CompletableFuture<List<TemperatureMeasurement>> {
        val future = CompletableFuture<List<TemperatureMeasurement>>()

        requestArray(dataPointsPath(key), TemperatureMeasurement::class.java, future::complete, future::completeExceptionally)

        return future
    }

    private fun <T> requestArray(uri: Uri, clazz: Class<T>, onSuccess: (results: List<T>) -> Unit, onError: ErrorListener? = null) {
        val request = JsonArrayRequest(Request.Method.GET, uri.toString(), null, {
            onSuccess(parseArray(it, clazz))
        }, onError)

        queue.add(request)
    }
}

internal fun <T> parseArray(jsonArray: JSONArray, clazz: Class<T>): List<T> {
    val parsedResults = ArrayList<T>(jsonArray.length())

    for (i in 0 until jsonArray.length()) {
        val parsedObject = parseObject(jsonArray[i] as JSONObject, clazz)

        parsedResults.add(parsedObject)
    }

    return parsedResults
}

internal fun <T> parseObject(json: JSONObject, clazz: Class<T>): T {
    return GSON.fromJson(json.toString(), clazz)
}