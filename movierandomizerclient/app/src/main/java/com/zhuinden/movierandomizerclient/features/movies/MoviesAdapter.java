package com.zhuinden.movierandomizerclient.features.movies;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.barrenechea.widget.recyclerview.decoration.DoubleHeaderAdapter;
import com.zhuinden.movierandomizerclient.R;
import com.zhuinden.movierandomizerclient.data.db.Movie;
import com.zhuinden.movierandomizerclient.utils.TextIdGenerator;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class MoviesAdapter
        extends RealmRecyclerViewAdapter<Movie, MoviesAdapter.ViewHolder>
        implements DoubleHeaderAdapter<MoviesAdapter.HeaderHolder, MoviesAdapter.SubheaderHolder> {
    public MoviesAdapter() {
        this(null);
    }

    public MoviesAdapter(@Nullable OrderedRealmCollection<Movie> data) {
        super(data, true, true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getData().get(position));
    }

    @Override
    public long getHeaderId(int position) {
        return TextIdGenerator.getTextValue(getData().get(position).getGenre());
    }

    @Override
    public long getSubHeaderId(int position) {
        long headerId = getHeaderId(position);
        Movie movie = getData().get(position);
        return headerId + TextIdGenerator.getTextValue(movie.getSeriesName());
    }

    @Override
    public HeaderHolder onCreateHeaderHolder(ViewGroup parent) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false));
    }

    @Override
    public SubheaderHolder onCreateSubHeaderHolder(ViewGroup parent) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_subheader, parent, false));
    }

    @Override
    public void onBindHeaderHolder(HeaderHolder holder, int position) {
        holder.bind(getData().get(position));
    }

    @Override
    public void onBindSubHeaderHolder(SubheaderHolder holder, int position) {
        holder.bind(getData().get(position));
    }

    public static class HeaderHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView genre;

        public HeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Movie movie) {
            genre.setText(movie.getGenre());
        }
    }

    public static class SubheaderHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView seriesName;

        public SubheaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Movie movie) {
            seriesName.setText(movie.getSeriesName() == null || !movie.getPartOfASeries() || "0".equals(movie.getSeriesName()) ? "No series" : movie.getSeriesName());
        }
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_title)
        TextView title;

        @BindView(R.id.movie_watched)
        CheckBox watched;

        @BindView(R.id.movie_series_title)
        TextView seriesTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Movie movie) {
            title.setText(movie.getFilmName());
            watched.setChecked(movie.getWatched());
            boolean isSeries = movie.getPartOfASeries() && movie.getSeriesName() != null;
            seriesTitle.setVisibility(isSeries ? View.VISIBLE : View.GONE);
            boolean hasSeriesNumber = movie.getSeriesNumber() != null && movie.getSeriesNumber() != 0;
            seriesTitle.setText(movie.getSeriesName() + (hasSeriesNumber ? " (" + movie.getSeriesNumber() + ")" : ""));
        }
    }
}
