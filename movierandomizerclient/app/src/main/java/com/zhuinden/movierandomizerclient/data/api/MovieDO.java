package com.zhuinden.movierandomizerclient.data.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieDO {
    @SerializedName("filmName")
    @Expose
    private String filmName;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("partOfASeries")
    @Expose
    private Boolean partOfASeries;
    @SerializedName("seriesName")
    @Expose
    private String seriesName;
    @SerializedName("watched")
    @Expose
    private Boolean watched;
    @SerializedName("seriesNumber")
    @Expose
    private Integer seriesNumber;

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

    public Boolean getPartOfASeries() {
        return partOfASeries;
    }

    public void setPartOfASeries(Boolean partOfASeries) {
        this.partOfASeries = partOfASeries;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public Boolean getWatched() {
        return watched;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }

    public Integer getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(Integer seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

}