package com.example.tmdb.MVP.presenter;

import com.example.tmdb.ActivityScope;
import com.example.tmdb.AppComponent;
import com.example.tmdb.MVP.view.DetailModule;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = DetailModule.class
)
public interface DetailComponent {

    void inject(DetailActivity DetailActivity);

}

