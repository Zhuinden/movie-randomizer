package com.zhuinden.movierandomizerclient.features.movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.zhuinden.movierandomizerclient.R;

/**
 * Created by Zhuinden on 2017.12.29..
 */

public class MoviesFiltersAdapter extends RecyclerView.Adapter<MoviesFiltersAdapter.ViewHolder> {
    private List<String> genres;

    private MoviesFragment moviesFragment; // TODO: use interface or something else

    public MoviesFiltersAdapter(MoviesFragment moviesFragment) {
        this.moviesFragment = moviesFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(genres.get(position));
    }

    @Override
    public int getItemCount() {
        return genres == null ? 0 : genres.size();
    }

    public void updateData(List<String> genres) {
        this.genres = genres;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.genre_text)
        TextView genreText;

        @BindView(R.id.genre_filter_select)
        CheckBox genreSelected;

        private final CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                moviesFragment.toggleGenreSelected(genre);
            }
        };

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> genreSelected.setChecked(!genreSelected.isChecked()));
        }

        private String genre;

        public void bind(String genre) {
            genreText.setText(genre);
            this.genre = genre;
            genreSelected.setOnCheckedChangeListener(null);
            genreSelected.setChecked(moviesFragment.isGenreSelected(genre));
            genreSelected.setOnCheckedChangeListener(checkedChangeListener);
        }
    }
}
