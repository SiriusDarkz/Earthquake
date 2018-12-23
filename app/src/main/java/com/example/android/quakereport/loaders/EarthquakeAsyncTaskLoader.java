package com.example.android.quakereport.loaders;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

import entity.Earthquake;
import utils.NetworkingUtils;

public class EarthquakeAsyncTaskLoader extends AsyncTaskLoader<List<Earthquake>> {


    public EarthquakeAsyncTaskLoader( Context context) {
        super(context);
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public List<Earthquake> loadInBackground() {
        return NetworkingUtils.fetchJsonData(NetworkingUtils.USGS_WEBAPI);
    }


}
