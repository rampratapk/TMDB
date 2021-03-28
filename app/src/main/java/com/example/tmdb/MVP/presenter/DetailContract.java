package com.example.tmdb.MVP.presenter;

import com.example.tmdb.MVP.model.Images;
import com.example.tmdb.MVP.model.Movie;

public interface DetailContract {

    interface View {

        void showLoading();

        void showContent(Movie movie);

        void showError();

        void onConfigurationSet(Images images);

    }

    interface Presenter {

        void start(int movieId);

        void finish();

    }

}