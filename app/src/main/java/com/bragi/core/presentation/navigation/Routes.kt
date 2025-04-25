package com.bragi.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data object Movies : Routes

    @Serializable
    data object Filter : Routes
}