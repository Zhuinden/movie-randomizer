package com.zhuinden.movierandomizerclient.features.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import ca.barrenechea.widget.recyclerview.decoration.DoubleHeaderDecoration;
import com.zhuinden.movierandomizerclient.R;
import com.zhuinden.movierandomizerclient.application.AppConfig;
import com.zhuinden.movierandomizerclient.application.Injector;
import com.zhuinden.movierandomizerclient.application.MainActivity;
import com.zhuinden.movierandomizerclient.data.db.Movie;
import com.zhuinden.movierandomizerclient.data.db.MovieDao;
import com.zhuinden.movierandomizerclient.data.db.MovieFields;
import com.zhuinden.movierandomizerclient.utils.Toaster;
import com.zhuinden.movierandomizerclient.utils.navigation.BaseFragment;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class MoviesFragment
        extends BaseFragment
        implements MainActivity.ActionHandler, MainActivity.BackPressHandler {
    Realm realm;

    @BindView(R.id.no_results_found_view)
    View noResultsFoundView;

    @BindView(R.id.filters_container)
    View filtersContainer;

    @OnClick(R.id.filters_container)
    public void onFilterContainerClicked() {
        filtersContainer.setVisibility(View.GONE);
        isFiltersVisible = false;
    }

    @BindView(R.id.filters_recycler_view)
    RecyclerView filtersRecycler;

    @BindView(R.id.empty_view_server_url_text)
    TextView serverUrlText;

    @BindView(R.id.empty_view)
    View emptyView;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.clear_text)
    View clearText;

    @BindView(R.id.title_search_container)
    View searchContainer;

    @BindView(R.id.search_text)
    TextView searchTextView;

    private String searchText;
    private boolean isSearchVisible = false;
    private HashMap<String, Boolean> genreSelected = new HashMap<>();
    private boolean isFiltersVisible = false;

    @OnClick(R.id.clear_text)
    public void clearText() {
        searchTextView.setText(""); // also updates everything via text change listener
    }

    @OnClick(R.id.pick_film)
    public void pickFilm() {
        if(!movies.isLoaded()) {
            return;
        }
        Movie unseenMovie = null;
        Random random = new Random();
        while(true) {
            RealmResults<Movie> unseenMovies = movies.where().equalTo(MovieFields.WATCHED, false).findAll();
            if(unseenMovies.isEmpty()) {
                Toaster.showToast("No movie found.");
                return;
            }
            unseenMovie = unseenMovies.get(random.nextInt(unseenMovies.size()));
            //noinspection ConstantConditions
            if(!unseenMovie.getPartOfASeries()) {
                break;
            }
            if(unseenMovie.getSeriesNumber() == null || unseenMovie.getSeriesNumber() <= 0 || "0".equals(unseenMovie.getSeriesName())) {
                break; // invalid
            }
            if(unseenMovie.getSeriesNumber() == 1) {
                break;
            }
            RealmResults<Movie> unseenSeries = movieDao.query(realm) //
                    .equalTo(MovieFields.PART_OF_A_SERIES, true)
                    .equalTo(MovieFields.SERIES_NAME, unseenMovie.getSeriesName()) //
                    .equalTo(MovieFields.WATCHED, false) //
                    .sort(MovieFields.SERIES_NUMBER, Sort.ASCENDING) //
                    .findAll();
            if(!unseenSeries.isEmpty()) {
                unseenMovie = unseenSeries.first();
                break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
        });
        builder.setTitle(unseenMovie.getFilmName());
        builder.setMessage((unseenMovie.getPartOfASeries() ? unseenMovie.getSeriesName() + " - " : "") + unseenMovie.getGenre());
        builder.setCancelable(false);
        builder.show();
    }

    @OnTextChanged(R.id.search_text)
    public void onSearchTextChanged(Editable editable) {
        searchText = editable.toString();
        updateSearch(searchText);
    }

    private RealmResults<Movie> movies;

    private void updateMovies(RealmResults<Movie> movies) {
        if(this.movies != null && this.movies.isValid()) {
            this.movies.removeAllChangeListeners();
        }
        this.movies = movies;
        this.movies.addChangeListener(changeListener);
        moviesAdapter.updateData(this.movies);
        updateEmptyView(this.movies);
    }

    private void updateSearch(String searchText) {
        RealmQuery<Movie> movieQuery;
        if(searchText == null || "".equals(searchText)) {
            clearText.setVisibility(View.GONE);
            movieQuery = movieDao.findAllNoSearchText(realm);
        } else {
            clearText.setVisibility(View.VISIBLE);
            movieQuery = movieDao.findAllWithSearchText(realm, searchText);
        }
        if(!genreSelected.isEmpty()) {
            movieQuery.beginGroup();
            int index = 0;
            int size = genreSelected.entrySet().size();
            for(Map.Entry<String, Boolean> entry : genreSelected.entrySet()) {
                if(entry.getValue()) {
                    movieQuery.equalTo(MovieFields.GENRE, entry.getKey());
                    if(index < size - 1) {
                        movieQuery.or();
                    }
                }
                index++;
            }
            movieQuery.endGroup();
        } else {
            movieQuery.beginGroup();
            movieQuery.equalTo(MovieFields.PART_OF_A_SERIES, true);
            movieQuery.equalTo(MovieFields.PART_OF_A_SERIES, false);
            movieQuery.endGroup(); // create Empty results
        }
        updateMovies(movieQuery.findAllAsync());
    }

    MoviesAdapter moviesAdapter;
    MoviesFiltersAdapter moviesFiltersAdapter;

    MovieDao movieDao;

    private void updateEmptyView(RealmResults<Movie> movies) {
        if(movies.isEmpty()) {
            if(movieDao.count(realm) <= 0) {
                emptyView.setVisibility(View.VISIBLE);
                noResultsFoundView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.GONE);
                noResultsFoundView.setVisibility(View.VISIBLE);
            }
        } else {
            emptyView.setVisibility(View.GONE);
            noResultsFoundView.setVisibility(View.GONE);
        }
    }

    private RealmChangeListener<RealmResults<Movie>> changeListener = movies -> {
        updateEmptyView(movies);
        moviesFiltersAdapter.updateData(getGenreList());
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDao = Injector.get().movieDao();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moviesAdapter = new MoviesAdapter();
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DoubleHeaderDecoration(moviesAdapter));
        recyclerView.setHasFixedSize(true);
        searchText = savedInstanceState == null ? null : savedInstanceState.getString("searchText");
        isSearchVisible = savedInstanceState != null && savedInstanceState.getBoolean("isSearchVisible");
        if(isSearchVisible) {
            searchContainer.setVisibility(View.VISIBLE);
        }
        isFiltersVisible = savedInstanceState != null && savedInstanceState.getBoolean("isFiltersVisible");
        if(isFiltersVisible) {
            filtersContainer.setVisibility(View.VISIBLE);
        }

        List<String> genres = getGenreList();
        if(savedInstanceState == null) {
            genreSelected = new HashMap<>();
            for(String genre : genres) {
                genreSelected.put(genre, true);
            }
        } else {
            //noinspection unchecked
            genreSelected = (HashMap<String, Boolean>) savedInstanceState.getSerializable("genreSelected");
        }

        updateSearch(searchText); // must be after setting genre

        updateServerUrlText();

        moviesFiltersAdapter = new MoviesFiltersAdapter(this);
        filtersRecycler.setAdapter(moviesFiltersAdapter);
        filtersRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        moviesFiltersAdapter.updateData(genres);
    }

    private List<String> getGenreList() {
        Set<String> genreSet = new LinkedHashSet<>();
        RealmResults<Movie> distinct = movieDao.query(realm).sort(MovieFields.GENRE).distinctValues(MovieFields.GENRE).findAll();
        for(Movie movie : distinct) {
            genreSet.add(movie.getGenre());
        }
        return new ArrayList<>(genreSet);
    }

    private void updateServerUrlText() {
        AppConfig appConfig = Injector.get().appConfig();
        serverUrlText.setText(appConfig.getServerUrl());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isSearchVisible", isSearchVisible);
        outState.putString("searchText", searchText);
        outState.putSerializable("genreSelected", genreSelected);
        outState.putBoolean("isFiltersVisible", isFiltersVisible);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
        realm = null;
    }

    @Override
    public void handleAction(int id) {
        switch(id) {
            case R.id.action_title_filter:
                if(searchContainer.getVisibility() == View.VISIBLE) {
                    isSearchVisible = false;
                    searchContainer.setVisibility(View.GONE);
                } else {
                    isSearchVisible = true;
                    searchContainer.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.action_set_ip_address:
                updateServerUrlText();
                break;
            case R.id.action_filters:
                toggleFilters();
                break;
            default:
                break;
        }
    }

    private void toggleFilters() {
        if(filtersContainer.getVisibility() == View.GONE) {
            isFiltersVisible = true;
            filtersContainer.setVisibility(View.VISIBLE);
        } else {
            isFiltersVisible = false;
            filtersContainer.setVisibility(View.GONE);
        }
    }

    public boolean isGenreSelected(String genre) {
        return genreSelected.containsKey(genre) && genreSelected.get(genre);
    }

    public void toggleGenreSelected(String genre) {
        boolean genreSelected = isGenreSelected(genre);
        if(!genreSelected) {
            this.genreSelected.put(genre, true);
        } else {
            this.genreSelected.remove(genre); // Do not put it into the Map if it is not selected.
        }
        updateSearch(searchText);
    }

    @Override
    public boolean onBackPressed() {
        if(isFiltersVisible) {
            filtersContainer.setVisibility(View.GONE);
            isFiltersVisible = false;
            return true;
        }
        return false;
    }
}
