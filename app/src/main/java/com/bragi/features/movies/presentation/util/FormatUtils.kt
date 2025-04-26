package com.bragi.features.movies.presentation.util

import kotlin.math.round

fun Long.formatMoney(): String {
    return when {
        this >= 1_000_000_000 -> {
            val billions = this / 1_000_000_000.0
            String.format("%.1fB", billions)
        }

        this >= 1_000_000 -> {
            val millions = this / 1_000_000.0
            String.format("%.1fM", millions)
        }

        this >= 1_000 -> {
            val thousands = this / 1_000.0
            String.format("%.1fK", thousands)
        }

        else -> toString()

    }.replace(".0", "")  // Remove trailing .0 if present
}

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0f
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}