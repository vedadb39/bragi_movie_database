package com.bragi.features.movies.presentation.model

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json

object CustomNavType {

    val Genre = object : NavType<GenreUi>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): GenreUi? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): GenreUi {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(bundle: Bundle, key: String, value: GenreUi) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: GenreUi): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }
}