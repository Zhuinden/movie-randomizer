package com.zhuinden.movierandomizerclient.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.birbit.android.jobqueue.JobManager;
import com.zhuinden.simplestack.History;
import com.zhuinden.simplestack.SimpleStateChanger;
import com.zhuinden.simplestack.StateChange;
import com.zhuinden.simplestack.StateChanger;

import com.zhuinden.movierandomizerclient.R;
import com.zhuinden.movierandomizerclient.data.GetMoviesTask;
import com.zhuinden.movierandomizerclient.data.api.MovieApi;
import com.zhuinden.movierandomizerclient.data.db.MovieDao;
import com.zhuinden.movierandomizerclient.features.movies.MoviesKey;
import com.zhuinden.movierandomizerclient.utils.MovieApiHolder;
import com.zhuinden.movierandomizerclient.utils.Toaster;
import com.zhuinden.movierandomizerclient.utils.ViewUtils;
import com.zhuinden.movierandomizerclient.utils.navigation.BaseKey;
import com.zhuinden.movierandomizerclient.utils.navigation.FragmentStateChanger;
import com.zhuinden.simplestack.navigator.Navigator;

import javax.annotation.Nonnull;

import io.realm.Realm;

public class MainActivity
        extends AppCompatActivity
        implements SimpleStateChanger.NavigationHandler {
    private static final String TAG = "MainActivity";

    FragmentStateChanger fragmentStateChanger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // don't pop up on start :(

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStateChanger = new FragmentStateChanger(getSupportFragmentManager(), R.id.container);

        MovieApiHolder movieApiHolder = Injector.get().movieApiHolder();
        MovieApi movieApi = Injector.get().movieApi();
        movieApiHolder.setMovieApi(movieApi);

        Navigator.configure()
                .setStateChanger(new SimpleStateChanger(this))
                .install(this, findViewById(R.id.container), History.single(MoviesKey.create()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private Fragment getCurrentFragment() {
        BaseKey currentKey = Navigator.getBackstack(this).top();
        return getSupportFragmentManager().findFragmentByTag(currentKey.getFragmentTag());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getCurrentFragment();
        switch(item.getItemId()) {
            case R.id.action_title_filter:
                if(fragment instanceof ActionHandler) {
                    ((ActionHandler) fragment).handleAction(item.getItemId());
                }
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_set_ip_address:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                @SuppressLint("InflateParams")
                View view = getLayoutInflater().inflate(R.layout.dialog_set_ip, null, false);
                EditText serverUrl = view.findViewById(R.id.address_text);
                AppConfig appConfig = Injector.get().appConfig();
                builder.setView(view);
                builder.setTitle(R.string.set_server_url);
                builder.setPositiveButton(R.string.set, (dialog, which) -> {
                    setServerUrl(serverUrl.getText().toString());
                });
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                });
                builder.show();
                ViewUtils.waitForMeasure(serverUrl, (view1, width, height) -> {
                    serverUrl.setText(appConfig.getServerUrl());
                });
                return true;
            case R.id.action_filters:
                if(fragment instanceof ActionHandler) {
                    ((ActionHandler) fragment).handleAction(item.getItemId());
                }
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        JobManager jobManager = Injector.get().jobManager();
        jobManager.addJobInBackground(new GetMoviesTask());
    }

    private void setServerUrl(String serverUrl) {
        MovieDao movieDao = Injector.get().movieDao();
        AppConfig appConfig = Injector.get().appConfig();
        boolean success = appConfig.setServerUrl(serverUrl);
        if(!success) {
            Toaster.showToast("The URL [" + serverUrl + "] is not a valid URL.");
            return;
        }
        Toaster.showToast("Server address set to: " + serverUrl);
        MovieApi movieApi = Injector.get().movieApi();
        MovieApiHolder movieApiHolder = Injector.get().movieApiHolder();
        movieApiHolder.setMovieApi(movieApi);
        try(Realm realm = Realm.getDefaultInstance()) {
            if(movieDao.count(realm) <= 0) {
                refresh();
            }
        }
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof ActionHandler) {
            ((ActionHandler) fragment).handleAction(R.id.action_set_ip_address);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getCurrentFragment();
        if(fragment instanceof BackPressHandler) {
            if(!((BackPressHandler) fragment).onBackPressed()) {
                if(!Navigator.onBackPressed(this)) {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public void onNavigationEvent(@Nonnull StateChange stateChange) {
        fragmentStateChanger.handleStateChange(stateChange);
    }

    public interface ActionHandler {
        void handleAction(@IdRes int id);
    }

    public interface BackPressHandler {
        boolean onBackPressed();
    }
}
