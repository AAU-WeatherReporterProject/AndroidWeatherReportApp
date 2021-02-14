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
import java.io.Serializable
import java.net.URL

const val URL_BASE = "http://laubi.at:8098/api/v1"
const val MEASUREMENTS_URL = "${URL_BASE}/measurementPoints"
const val DP_URL = "${URL_BASE}/dataPoints?key="

private fun basePath(resource: String): Uri.Builder {

    return Uri.Builder()
        .scheme("http")
        .encodedAuthority("laubi.at:8098")
        .appendPath("api")
        .appendPath("v1")
        .appendPath(resource);
}

private fun dataPointsPath(key: String): Uri {
    return basePath("dataPoints")
        .appendQueryParameter("key", key)
        .build();
}

class RestService(context: Context) {
    private val queue = Volley.newRequestQueue(context)
    private val gson = Gson()

    fun requestLocations(onSuccess: (dataPoints: List<MeasurementPoint>) -> Unit) {
        requestArray(MEASUREMENTS_URL, MeasurementPoint::class.java, onSuccess)
    }

    fun requestDataPoints(key: String, onSuccess: (dataPoints: List<TemperatureMeasurement>) -> Unit) {
        requestArray(dataPointsPath(key).toString(), TemperatureMeasurement::class.java, onSuccess, {println(it)})
    }

    private fun <T> requestArray(url: String, clazz: Class<T>, onSuccess: (results: List<T>) -> Unit, onError: ErrorListener? = null) {
        val request = JsonArrayRequest(Request.Method.GET, url, null, {
            onSuccess(parseArray(it, clazz))
        }, onError)

        queue.add(request)
    }

    private fun <T> parseArray(jsonArray: JSONArray, clazz: Class<T>): List<T> {
        val parsedResults = ArrayList<T>(jsonArray.length())

        for (i in 0 until jsonArray.length()) {
            val parsedObject = parseObject(jsonArray[i] as JSONObject, clazz)

            parsedResults.add(parsedObject)
        }

        return parsedResults
    }

    private fun <T> parseObject(json: JSONObject, clazz: Class<T>): T {
        return gson.fromJson(json.toString(), clazz)
    }
}