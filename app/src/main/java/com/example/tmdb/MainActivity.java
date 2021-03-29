package com.example.tmdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.tmdb.MVP.model.Images;
import com.example.tmdb.MVP.model.Movie;
import com.example.tmdb.MVP.presenter.DaggerMainComponent;
import com.example.tmdb.MVP.presenter.DetailActivity;
import com.example.tmdb.MVP.presenter.MainContract;
import com.example.tmdb.MVP.presenter.MainModule;
import com.example.tmdb.MVP.presenter.MainPresenter;
import com.example.tmdb.MVP.presenter.SearchPresenter;
import com.example.tmdb.MVP.presenter.TopPresenter;
import com.example.tmdb.MVP.view.EndlessScrollListener;
import com.example.tmdb.adapter.MoviesAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.tmdb.MVP.presenter.DetailActivity.MOVIE_ID;
import static com.example.tmdb.MVP.presenter.DetailActivity.MOVIE_TITLE;


public class MainActivity extends AppCompatActivity implements MainContract.View,
        SwipeRefreshLayout.OnRefreshListener, EndlessScrollListener.ScrollToBottomListener, MoviesAdapter.ItemClickListener {
    private static final String TAG = "Main";

    @Inject
    MainPresenter presenter;

    @Inject
    TopPresenter topPresenter;

    @Inject
    SearchPresenter searchPresenter;

    private Category category;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView contentView;
    @BindView(R.id.textView)
    View errorView;
    @BindView(R.id.progressBar)
    View loadingView;

    private MoviesAdapter moviesAdapter;
    private EndlessScrollListener endlessScrollListener;
    private Images images;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        category = BuilderUtils.category;
        setTitle(getText(BuilderUtils.category));
        setupContentView();
        DaggerMainComponent.builder()
                .appComponent(App.getAppComponent(getApplication()))
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    private void setupContentView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager, this);
        contentView.setLayoutManager(linearLayoutManager);
        contentView.addOnScrollListener(endlessScrollListener);
    }

    private String getText(Category c){
        if(c==Category.Popular)return BuilderUtils.popular;
        return BuilderUtils.topMovies;
    }

    @Override
    public void onRefresh() {
        endlessScrollListener.onRefresh();
        if(category==Category.Popular)presenter.onPullToRefresh();
        else if(category == Category.Top_Rated) topPresenter.onPullToRefresh();
        else searchPresenter.onPullToRefresh();
    }

    @Override
    public void onScrollToBottom() {
        if(category==Category.Popular)presenter.onScrollToBottom();
        else if(category == Category.Top_Rated) topPresenter.onScrollToBottom();
        else searchPresenter.onPullToRefresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(category==Category.Popular)presenter.start();
        else if(category == Category.Top_Rated) topPresenter.start();
        else searchPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setTitle("Search: "+query);
                String q = query;
                searchPresenter.start(q);
                searchView.onActionViewCollapsed();
                Toast.makeText(MainActivity.this, q,Toast.LENGTH_LONG).show();
                category = Category.Search;
                BuilderUtils.category = category;
                return false;

            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.popular:
                category = Category.Popular;
                BuilderUtils.category = category;
                presenter.start();
                setTitle(BuilderUtils.popular);
                return true;
            case R.id.top_rated:
                category = Category.Top_Rated;
                BuilderUtils.category = category;
                topPresenter.start();
                setTitle(BuilderUtils.topMovies);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLoading(boolean isRefresh) {
        if (isRefresh) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        } else {
            loadingView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showContent(List<Movie> movies, boolean isRefresh) {
        if (moviesAdapter == null) {
            moviesAdapter = new MoviesAdapter(movies, this, images, this);
            contentView.setAdapter(moviesAdapter);
        } else {
            if (isRefresh) {
                moviesAdapter.clear();
            }
            moviesAdapter.addAll(movies);
            moviesAdapter.notifyDataSetChanged();
        }

        // Delay SwipeRefreshLayout animation by 1.5 seconds
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);

        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        swipeRefreshLayout.setRefreshing(false);
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConfigurationSet(Images images) {
        this.images = images;

        if (moviesAdapter != null) {
            moviesAdapter.setImages(images);
        }
    }

    @Override
    public void onItemClick(int movieId, String movieTitle) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra(MOVIE_ID, movieId);
        i.putExtra(MOVIE_TITLE, movieTitle);
        startActivity(i);
    }

    @OnClick(R.id.textView)
    void onClickErrorView() {
        if(category==Category.Popular)presenter.start();
        else topPresenter.start();
    }

}
