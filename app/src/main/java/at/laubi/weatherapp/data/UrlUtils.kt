package at.laubi.weatherapp.data

import android.net.Uri

const val SCHEME = "http"
const val URL_BASE = "laubi.at:8098"
const val PATH = "api/v1"

const val DATA_POINTS_PATH = "dataPoints"
const val MEASUREMENT_POINTS_KEY = "key"

const val MEASUREMENT_POINTS = "measurementPoints"

private fun basePath(resource: String): Uri.Builder {
    return Uri.Builder()
        .scheme(SCHEME)
        .encodedAuthority(URL_BASE)
        .encodedPath(PATH)
        .appendPath(resource);
}

fun dataPointsPath(key: String): Uri {
    return basePath(DATA_POINTS_PATH)
        .appendQueryParameter(MEASUREMENT_POINTS_KEY, key)
        .build()
}

fun buildMeasurementUrl(): Uri {
    return basePath(MEASUREMENT_POINTS)
        .build()
}