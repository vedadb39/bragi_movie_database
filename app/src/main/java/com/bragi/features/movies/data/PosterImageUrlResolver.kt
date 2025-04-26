package com.bragi.features.movies.data

import com.bragi.features.movies.domain.model.PosterImage.Unavailable
import com.bragi.features.movies.domain.model.PosterImage.Url


class PosterImageUrlResolver(private val posterBaseUrl: String) {
    fun resolve(posterPath: String?) =
        if (posterPath == null) Unavailable else Url("${posterBaseUrl}$posterPath")
}
