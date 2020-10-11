package com.zhuinden.movierandomizerbackend;

public class Movie {
    private String filmName;
    private String genre;
    private boolean partOfASeries;
    private String seriesName;
    private boolean watched;
    private int seriesNumber;

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isPartOfASeries() {
        return partOfASeries;
    }

    public void setPartOfASeries(boolean partOfASeries) {
        this.partOfASeries = partOfASeries;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public void setSeriesNumber(int seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public int getSeriesNumber() {
        return seriesNumber;
    }
}
