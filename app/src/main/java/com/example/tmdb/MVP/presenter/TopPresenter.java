package com.example.tmdb.MVP.presenter;


import android.annotation.SuppressLint;
import android.util.Log;

import com.example.tmdb.BuilderUtils;
import com.example.tmdb.MVP.model.Configuration;
import com.example.tmdb.MVP.model.Movies;
import com.example.tmdb.network.ApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private ApiService apiService;
    private int page = 1;
    private Configuration configuration;

    @Inject
    TopPresenter(MainContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }

    @Override
    public void start() {
        view.showLoading(false);
        getMovies(true);
        getConfiguration();
    }

    @Override
    public void start(String query) {
    }

    private void getConfiguration() {
        Call<Configuration> call = apiService.getConfiguration();
        call.enqueue(new Callback<Configuration>() {
            @Override
            public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                if (response.isSuccessful()) {
                    view.onConfigurationSet(response.body().images);
                }
            }

            @Override
            public void onFailure(Call<Configuration> call, Throwable t) {
            }
        });
    }

    @Override
    public void onPullToRefresh() {
        page = 1; // reset
        view.showLoading(true);
        getMovies(true);
    }

    @Override
    public void onScrollToBottom() {
        page++;
        view.showLoading(true);
        getMovies(false);
    }

    private void getMovies(final boolean isRefresh) {

        String language = "en-US";
        Call<Movies> call = apiService.getTopMovies(language, page);
        call.enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                if (response.isSuccessful()) {
                    view.showContent(response.body().movies, isRefresh);
                } else {
                    Log.v(BuilderUtils.tag, "it's a failure response");
                    view.showError();
                }
            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

                Log.v(BuilderUtils.tag, "it's a failure");
                view.showError();
            }
        });
    }

    public String getReleaseDate() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        return format1.format(cal.getTime());
    }

    @Override
    public void finish() {

    }

}

