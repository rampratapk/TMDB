package com.example.tmdb.MVP.presenter;


import com.example.tmdb.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private final MainContract.View mainView;

    public MainModule(MainContract.View mainView) {
        this.mainView = mainView;
    }

    @Provides
    @ActivityScope
    MainContract.View provideMainView() {
        return mainView;
    }

}

