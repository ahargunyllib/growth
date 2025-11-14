package com.ahargunyllib.growth.presentation.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahargunyllib.growth.model.TPS
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MapsViewModel : ViewModel() {

    private val _tpsList = MutableStateFlow<List<TPS>>(emptyList())
    val tpsList: StateFlow<List<TPS>> = _tpsList

    private val _polylinePoints = MutableStateFlow<List<LatLng>>(emptyList())
    val polylinePoints: StateFlow<List<LatLng>> = _polylinePoints

    init {
        fetchTpsLocations()
    }

    private fun fetchTpsLocations() {
        viewModelScope.launch {
            val db = Firebase.firestore
            db.collection("partners")
                .get()
                .addOnSuccessListener { result ->
                    val list = result.documents.mapNotNull { document ->
                        val data = document.data
                        val name = data?.get("name") as? String ?: return@mapNotNull null
                        val address = data["address"] as? String ?: ""
                        val latitude = data["latitude"] as? Double
                        val longitude = data["longitude"] as? Double

                        if (latitude == null || longitude == null) {
                            return@mapNotNull null
                        }
                        val location = LatLng(latitude, longitude)

                        TPS(name, address, "", location)
                    }
                    _tpsList.value = list
                }
                .addOnFailureListener {
                    // Handle error
                }
        }
    }

    fun calculateDistances(userLocation: LatLng) {
        val updatedList = _tpsList.value.map { tps ->
            val tpsLocation = Location("TPS")
            tpsLocation.latitude = tps.location.latitude
            tpsLocation.longitude = tps.location.longitude

            val userLoc = Location("User")
            userLoc.latitude = userLocation.latitude
            userLoc.longitude = userLocation.longitude

            val distanceInMeters = userLoc.distanceTo(tpsLocation)
            val distanceInKm = distanceInMeters / 1000

            tps.copy(distance = String.format("%.2f km", distanceInKm))
        }
        _tpsList.value = updatedList.sortedBy { it.distance }
    }

    fun getDirections(apiKey: String, origin: LatLng, destination: LatLng) {
        viewModelScope.launch {
            val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=${origin.latitude},${origin.longitude}&" +
                    "destination=${destination.latitude},${destination.longitude}&" +
                    "key=$apiKey"
            Log.d("MapsViewModel", "Directions URL: $url")

            try {
                val result = withContext(Dispatchers.IO) {
                    URL(url).readText()
                }
                Log.d("MapsViewModel", "Directions Response: $result")
                val jsonObject = JSONObject(result)

                val status = jsonObject.getString("status")
                if (status != "OK") {
                    Log.e("MapsViewModel", "Directions API Error: $status - Make sure the API key is valid and the Directions API is enabled.")
                    _polylinePoints.value = emptyList()
                    return@launch
                }

                val routes = jsonObject.getJSONArray("routes")
                if (routes.length() > 0) {
                    val points = routes.getJSONObject(0).getJSONObject("overview_polyline").getString("points")
                    _polylinePoints.value = decodePolyline(points)
                } else {
                    _polylinePoints.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("MapsViewModel", "Error fetching directions", e)
                _polylinePoints.value = emptyList()
            }
        }
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(p)
        }
        return poly
    }
}
