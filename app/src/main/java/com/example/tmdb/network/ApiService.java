package com.example.tmdb.network;

import com.example.tmdb.MVP.model.Configuration;
import com.example.tmdb.MVP.model.Movie;
import com.example.tmdb.MVP.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @GET("/3/movie/popular")
    Call<Movies> getMovies(@Query("language") String language, @Query("page") int page);

    @GET("/3/movie/{id}")
    Call<Movie> getMovie(@Path("id") int id);


    @Headers("Cache-Control: public, max-stale=2419200")
    @GET("/3/configuration")
    Call<Configuration> getConfiguration();

    @GET("/3/movie/top_rated")
    Call<Movies> getTopMovies(@Query("language") String language, @Query("page") int page);

    @GET("3/search/movie")
    Call<Movies> getSearchMovies(@Query("query") String query, @Query("page") int page);


}