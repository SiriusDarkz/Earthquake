/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ListView;

import android.app.LoaderManager.LoaderCallbacks;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.quakereport.adapter.SimpleAdapter;
import com.example.android.quakereport.loaders.EarthquakeAsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import entity.Earthquake;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private ListView earthquakeListView;
    private SimpleAdapter simpleAdapter;
    private TextView emptyTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        earthquakeListView  =  findViewById(R.id.list);
        emptyTextView = findViewById(R.id.noItem);
        progressBar = findViewById(R.id.progress_circular);

        earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {

            Intent magWebView = new Intent(Intent.ACTION_VIEW, Uri.parse(simpleAdapter.getEarthquakes().get(position).getUrl()));
            startActivity(magWebView);
        });

        simpleAdapter = new SimpleAdapter(this,0, new ArrayList<>());

        earthquakeListView.setAdapter(simpleAdapter);
        earthquakeListView.setEmptyView(emptyTextView);

        if(isConnectedToInternet())
            getLoaderManager().initLoader(1,null,this);

        else {
            progressBar.setVisibility(View.GONE);
            emptyTextView.setText("No Internet Connection");
        }

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeAsyncTaskLoader(EarthquakeActivity.this);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        progressBar.setVisibility(View.GONE);

        emptyTextView.setText("No Earthquakes founds");

        if(earthquakes != null && !earthquakes.isEmpty())
            simpleAdapter.setEarthquakes(earthquakes);

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        simpleAdapter.clear();


    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

         return  activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
