package com.filmnot.data.model

import com.google.gson.annotations.SerializedName

// TMDB Response Models
data class TmdbMovieResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<TmdbMovie>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class TmdbMovie(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("first_air_date") val firstAirDate: String? = null,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("media_type") val mediaType: String? = null,
    @SerializedName("popularity") val popularity: Double = 0.0
) {
    fun displayTitle() = title ?: name ?: "Unknown"
    fun displayDate() = releaseDate ?: firstAirDate ?: ""
    fun posterUrl() = if (posterPath != null) "https://image.tmdb.org/t/p/w500$posterPath" else null
    fun backdropUrl() = if (backdropPath != null) "https://image.tmdb.org/t/p/w780$backdropPath" else null
    fun type() = when {
        mediaType == "tv" -> MediaType.TV
        mediaType == "movie" -> MediaType.MOVIE
        name != null -> MediaType.TV
        else -> MediaType.MOVIE
    }
}

data class TmdbMovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("first_air_date") val firstAirDate: String? = null,
    @SerializedName("runtime") val runtime: Int? = null,
    @SerializedName("episode_run_time") val episodeRunTime: List<Int>? = null,
    @SerializedName("genres") val genres: List<Genre>?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("imdb_id") val imdbId: String? = null,
    @SerializedName("external_ids") val externalIds: ExternalIds? = null,
    @SerializedName("videos") val videos: VideoResponse?,
    @SerializedName("credits") val credits: Credits?,
    @SerializedName("similar") val similar: TmdbMovieResponse?,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int? = null,
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int? = null
) {
    fun displayTitle() = title ?: name ?: "Unknown"
    fun displayDate() = releaseDate ?: firstAirDate ?: ""
    fun posterUrl() = if (posterPath != null) "https://image.tmdb.org/t/p/w500$posterPath" else null
    fun backdropUrl() = if (backdropPath != null) "https://image.tmdb.org/t/p/w780$backdropPath" else null
    fun trailerKey(): String? = videos?.results?.firstOrNull {
        it.site == "YouTube" && (it.type == "Trailer" || it.type == "Teaser")
    }?.key
    fun imdbUrl() = imdbId?.let { "https://www.imdb.com/title/$it" }
        ?: externalIds?.imdbId?.let { "https://www.imdb.com/title/$it" }
}

data class ExternalIds(
    @SerializedName("imdb_id") val imdbId: String?
)

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)

data class VideoResponse(
    @SerializedName("results") val results: List<Video>
)

data class Video(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String
)

data class Credits(
    @SerializedName("cast") val cast: List<CastMember>,
    @SerializedName("crew") val crew: List<CrewMember>
)

data class CastMember(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("character") val character: String,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("order") val order: Int
) {
    fun profileUrl() = if (profilePath != null) "https://image.tmdb.org/t/p/w185$profilePath" else null
}

data class CrewMember(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("job") val job: String,
    @SerializedName("profile_path") val profilePath: String?
)

data class TmdbSearchResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<TmdbMovie>,
    @SerializedName("total_pages") val totalPages: Int
)

enum class MediaType { MOVIE, TV }
