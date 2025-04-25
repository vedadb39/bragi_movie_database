package com.bragi.movies.presentation.model

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json

object CustomNavType {

    val GenreState = object : NavType<GenreStateUiModel>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): GenreStateUiModel? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): GenreStateUiModel {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(bundle: Bundle, key: String, value: GenreStateUiModel) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: GenreStateUiModel): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }
}