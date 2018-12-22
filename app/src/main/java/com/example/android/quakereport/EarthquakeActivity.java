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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.android.quakereport.adapter.SimpleAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Earthquake;
import utils.NetworkingUtils;
import utils.QueryUtils;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    ListView earthquakeListView;
    SimpleAdapter simpleAdapter;
    List<Earthquake> earthquakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        earthquakeListView  = (ListView) findViewById(R.id.list);
        earthquakeListView.setOnItemClickListener((parent, view, position, id) -> {

            Intent magWebView = new Intent(Intent.ACTION_VIEW, Uri.parse(earthquakes.get(position).getUrl()));
            startActivity(magWebView);
        });


        new EarthquakeAsynData().execute(NetworkingUtils.USGS_WEBAPI);


    }

    private void updateUI(List<Earthquake> earthquakes) {

        this.earthquakes = earthquakes;
        simpleAdapter = new SimpleAdapter(this, 0,earthquakes);
        earthquakeListView.setAdapter(simpleAdapter);



    }


    private class EarthquakeAsynData extends AsyncTask<String,Void,List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            return NetworkingUtils.fetchJsonData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            updateUI(earthquakes);
        }
    }

}
