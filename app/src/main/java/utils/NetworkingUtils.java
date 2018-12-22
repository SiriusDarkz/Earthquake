package utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import entity.Earthquake;

public class NetworkingUtils {

    public static final String USGS_WEBAPI = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    public static List<Earthquake> fetchJsonData(String url) {


        return extractEarthquakesFromJson(makeHttpRequest(createURL(url)));

    }


    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";

        if(url == null) {
            return jsonResponse;
        }


        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200) {

                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {

            try {
                if(inputStream != null)
                    inputStream.close();

                if(httpURLConnection != null)
                    httpURLConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        if(inputStream == null) {
            return "";
        }
        BufferedReader bufferedReader = new BufferedReader(
                                                            new InputStreamReader
                                                                (inputStream,Charset.forName("UTF-8")));
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(bufferedReader != null)
                    bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return output.toString();
    }

    private static List<Earthquake> extractEarthquakesFromJson(String stringJson) {
        List<Earthquake> earthquakes = new ArrayList<>();
        try {

            JSONObject earthquakeJson = new JSONObject(stringJson);
            JSONArray earthquakeJsonArray = earthquakeJson.getJSONArray("features");
            for (int i = 0; i < earthquakeJsonArray.length(); i++) {
                earthquakeJson = earthquakeJsonArray.getJSONObject(i);
                earthquakeJson = earthquakeJson.getJSONObject("properties");
                earthquakes.add(new Earthquake((float) earthquakeJson.getDouble("mag"),
                        earthquakeJson.getString("place"),
                        Instant.ofEpochMilli(earthquakeJson.getLong("time"))
                                .atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        earthquakeJson.getString("url")));
            }


            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;

    }


}
