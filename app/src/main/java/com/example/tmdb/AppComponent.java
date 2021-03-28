package com.example.tmdb;

import android.app.Application;

import com.example.tmdb.network.ApiService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class,
                ApiModule.class
        }
)
public interface AppComponent {

    Application application();

    ApiService apiService();

    void inject(App app);

}
