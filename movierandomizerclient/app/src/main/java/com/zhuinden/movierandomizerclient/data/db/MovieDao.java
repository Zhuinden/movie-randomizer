package com.zhuinden.movierandomizerclient.data.db;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Zhuinden on 2017.12.28..
 */

@Singleton
public class MovieDao {
    private final Class<Movie> clazz = Movie.class;

    @Inject
    public MovieDao() {
    }

    public RealmResults<Movie> findAll(Realm realm) {
        return query(realm).findAll();
    }

    public void insertOrUpdate(Realm realm, Movie movie) {
        realm.insertOrUpdate(movie);
    }

    public void insertOrUpdate(Realm realm, Collection<Movie> collection) {
        realm.insertOrUpdate(collection);
    }

    public Movie saveOrUpdate(Realm realm, Movie movie) {
        return realm.copyToRealmOrUpdate(movie);
    }

    public List<Movie> saveOrUpdate(Realm realm, RealmList<Movie> list) {
        return realm.copyToRealmOrUpdate(list);
    }

    public RealmQuery<Movie> query(Realm realm) {
        return realm.where(clazz);
    }

    public void deleteEveryObject(Realm realm) {
        realm.delete(clazz);
    }

    public void delete(Realm realm, Movie movie) {
        movie.deleteFromRealm();
    }

    public void deleteAll(Realm realm, RealmResults<Movie> realmResults) {
        realmResults.deleteAllFromRealm();
    }

    public long count(Realm realm) {
        return query(realm).count();
    }

    private RealmQuery<Movie> createDefaultQuery(Realm realm) {
        return query(realm).sort(new String[]{MovieFields.GENRE, MovieFields.SERIES_NAME, MovieFields.SERIES_NUMBER, MovieFields.FILM_NAME},
                                 new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING});
    }

    public RealmQuery<Movie> findAllNoSearchText(Realm realm) {
        return createDefaultQuery(realm);
    }

    public RealmQuery<Movie> findAllWithSearchText(Realm realm, String searchText) {
        return createDefaultQuery(realm).beginGroup().contains(MovieFields.FILM_NAME,
                                                               searchText,
                                                               Case.INSENSITIVE).or().contains(MovieFields.SERIES_NAME,
                                                                                               searchText,
                                                                                               Case.INSENSITIVE).endGroup();
    }
}
