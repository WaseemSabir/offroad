package com.example.offroad.data

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}