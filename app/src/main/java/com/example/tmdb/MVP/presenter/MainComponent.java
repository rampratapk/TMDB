package com.example.tmdb.MVP.presenter;

import com.example.tmdb.ActivityScope;
import com.example.tmdb.AppComponent;
import com.example.tmdb.MainActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = MainModule.class
)
public interface MainComponent {

    void inject (MainActivity mainActivity);

}

