package com.example.tmdb.MVP.presenter;

import android.util.Log;

import com.example.tmdb.BuilderUtils;
import com.example.tmdb.MVP.model.Configuration;
import com.example.tmdb.MVP.model.Images;
import com.example.tmdb.MVP.model.Movie;
import com.example.tmdb.network.ApiService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View view;
    private ApiService apiService;

    private Images images;

    @Inject
    DetailPresenter(DetailContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void start(int movieId) {
        view.showLoading();
        Log.v(BuilderUtils.tag, images+" ");
        if (images == null) {
            getConfiguration(movieId);
        } else {
            Log.v(BuilderUtils.tag, images+ " ");
            view.onConfigurationSet(images);
            getMovie(movieId);
        }
    }

    private void getConfiguration(final int movieId) {
        Call<Configuration> call = apiService.getConfiguration();
        call.enqueue(new Callback<Configuration>() {
            @Override
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                if (response.isSuccessful()) {
                    images = response.body().images;
                    view.onConfigurationSet(images);
//                    Log.v(BuilderUtils.tag, "coming from here "+call.request());
                    getMovie(movieId);
                }
            }

            @Override
            public void onFailure(Call<Configuration> call, Throwable t) {
            }
        });
    }

    private void getMovie(int movieId) {
//        Log.v(BuilderUtils.tag, movieId+" gettting the movie id");
        Call<Movie> call = apiService.getMovie(movieId);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Log.v(BuilderUtils.tag, response.isSuccessful()+" ");
                if (response.isSuccessful()) {
                    view.showContent(response.body());
                } else {
                    view.showError();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                view.showError();
            }
        });
    }

    @Override
    public void finish() {

    }

}
