package com.filmnot.data.api

import com.filmnot.data.model.TmdbMovie
import com.filmnot.data.model.TmdbMovieDetail
import com.filmnot.data.model.TmdbMovieResponse
import com.filmnot.data.model.TmdbSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApiService {

    @GET("trending/all/week")
    suspend fun getTrending(
        @Query("language") language: String = "en-US"
    ): TmdbMovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): TmdbMovieResponse

    @GET("tv/popular")
    suspend fun getPopularTv(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): TmdbMovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): TmdbMovieResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTv(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): TmdbMovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): TmdbMovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = "videos,credits,similar,external_ids",
        @Query("language") language: String = "en-US"
    ): TmdbMovieDetail

    @GET("tv/{tv_id}")
    suspend fun getTvDetail(
        @Path("tv_id") tvId: Int,
        @Query("append_to_response") appendToResponse: String = "videos,credits,similar,external_ids",
        @Query("language") language: String = "en-US"
    ): TmdbMovieDetail

    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US"
    ): TmdbSearchResponse

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("language") language: String = "en-US"
    ): TmdbMovieResponse
}
