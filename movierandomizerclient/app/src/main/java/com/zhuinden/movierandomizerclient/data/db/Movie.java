package com.zhuinden.movierandomizerclient.data.db;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Zhuinden on 2017.12.28..
 */
public class Movie
        extends RealmObject {
    @PrimaryKey
    private String filmName;
    @Index
    private String genre;
    @Index
    private boolean partOfASeries;
    @Index
    private String seriesName;
    @Index
    private boolean watched;
    @Index
    private Integer seriesNumber;
    @Index
    private boolean isBeingSaved;

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

    public boolean getPartOfASeries() {
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

    public boolean getWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public Integer getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(Integer seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public boolean getIsBeingSaved() {
        return isBeingSaved;
    }

    public void setIsBeingSaved(boolean beingSaved) {
        isBeingSaved = beingSaved;
    }
}
