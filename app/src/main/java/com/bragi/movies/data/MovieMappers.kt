package com.bragi.movies.data

import com.bragi.movies.data.model.MovieApiModel
import com.bragi.movies.domain.model.Movie


fun MovieApiModel.toMovie(): Movie {
    return Movie(
        id = id,
        title = original_title,
        rating = rating,
        revenue = revenue,
        budget = budget,
        posterUrl = poster_path
    )
}