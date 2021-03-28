package com.example.tmdb.MVP.presenter;

import com.example.tmdb.MVP.model.Images;
import com.example.tmdb.MVP.model.Movie;

import java.util.List;

public interface MainContract {
    interface View {

        void showLoading(boolean isRefresh);

        void showContent(List<Movie> movies, boolean isRefresh);

        void showError();

        void onConfigurationSet(Images images);

    }

    interface Presenter {

        void start();

        void start(String query);

        void onPullToRefresh();

        void onScrollToBottom();

        void finish();

    }

}
