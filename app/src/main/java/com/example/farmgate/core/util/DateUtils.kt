package com.example.farmgate.core.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun String.toReadableDateTime(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date = inputFormat.parse(this) ?: return this

        val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH)
        outputFormat.format(date)
    } catch (e: Exception) {
        try {
            val fallbackInputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            fallbackInputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val date = fallbackInputFormat.parse(this) ?: return this

            val outputFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH)
            outputFormat.format(date)
        } catch (e: Exception) {
            this
        }
    }
}