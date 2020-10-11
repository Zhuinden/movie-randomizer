package com.zhuinden.movierandomizerclient.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.List;

import com.zhuinden.movierandomizerclient.application.Injector;
import com.zhuinden.movierandomizerclient.data.api.MovieApi;
import com.zhuinden.movierandomizerclient.data.api.MovieDO;
import com.zhuinden.movierandomizerclient.data.db.Movie;
import com.zhuinden.movierandomizerclient.data.db.MovieDao;
import com.zhuinden.movierandomizerclient.data.db.MovieFields;
import com.zhuinden.movierandomizerclient.utils.MovieApiHolder;
import com.zhuinden.movierandomizerclient.utils.Toaster;
import io.realm.Realm;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class GetMoviesTask
        extends Job {
    public GetMoviesTask() {
        super(new Params(1).requireNetwork());
    }

    @Override
    public void onAdded() {
        // whatever
    }

    @Override
    public void onRun()
            throws Throwable {
        MovieDao movieDao = Injector.get().movieDao();
        MovieApiHolder movieApiHolder = Injector.get().movieApiHolder();
        MovieApi movieApi = movieApiHolder.getMovieApi();
        Response<List<MovieDO>> response = movieApi.getMovies().execute();
        if(response.isSuccessful()) {
            List<MovieDO> movies = response.body();
            if(movies == null) {
                ResponseBody errorBody = response.errorBody();
                throw new RuntimeException(errorBody != null ? errorBody.string() : "Unknown error [" + response.code() + "]");
            }
            Movie movie = new Movie();
            try(Realm realm = Realm.getDefaultInstance()) {
                realm.executeTransaction(r -> {
                    for(Movie storedMovie : movieDao.findAll(realm)) {
                        storedMovie.setIsBeingSaved(false);
                    }
                    for(MovieDO movieDO : movies) {
                        movie.setIsBeingSaved(true);
                        movie.setFilmName(movieDO.getFilmName());
                        movie.setGenre(movieDO.getGenre());
                        movie.setPartOfASeries(movieDO.getPartOfASeries());
                        movie.setSeriesName(movieDO.getSeriesName());
                        movie.setWatched(movieDO.getWatched());
                        movie.setSeriesNumber(movieDO.getSeriesNumber() == 0 ? null : movieDO.getSeriesNumber());
                        movieDao.insertOrUpdate(r, movie);
                    }
                    movieDao.query(realm).equalTo(MovieFields.IS_BEING_SAVED, false).findAll().deleteAllFromRealm();
                });
            }
        } else {
            throw new RuntimeException("Downloading movies failed.");
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Toaster.showToast("Could not get movies: [" + (throwable != null ? throwable.getMessage() : "unknown") + "]");
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }
}
