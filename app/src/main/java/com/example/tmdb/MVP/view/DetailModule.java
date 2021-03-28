package com.example.tmdb.MVP.view;

import com.example.tmdb.ActivityScope;
import com.example.tmdb.MVP.presenter.DetailContract;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailModule {
    private final DetailContract.View DetailView;

    public DetailModule(DetailContract.View DetailView) {
        this.DetailView = DetailView;
    }

    @Provides
    @ActivityScope
    DetailContract.View provideDetailView() {
        return DetailView;
    }

}
