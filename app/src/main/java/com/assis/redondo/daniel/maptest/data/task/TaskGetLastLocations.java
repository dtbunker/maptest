package com.assis.redondo.daniel.maptest.data.task;

import android.os.AsyncTask;
import android.util.Log;

import com.assis.redondo.daniel.maptest.Config;
import com.assis.redondo.daniel.maptest.data.vo.LocationVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TaskGetLastLocations extends AsyncTask<String, Void, LocationVO[]> {

    private LastLocationListener mListener;
    public static final double SouhtWestLat = -23.612474 ;
    public static final double SouhtWestLong =  -46.702746;
    public static final double NorthEastLat = -23.589548;
    public static final double NorthEastLong =  46.673392;

    public void setListener(LastLocationListener listener) {
        mListener = listener;
    }

    public interface LastLocationListener {
        public void eventosLoaded(LocationVO[] result);
    }

    @Override
    protected  LocationVO[] doInBackground(String... arg0) {

        InputStream in;

        try {
            in = downloadUrl(
                    Config.BASE_API_METHOD_URL +
                    Config.API_METHOD_GET_LAST_LOCATIONS +
                    Config.RequestKeys.QUERY_STRING +
                    Config.RequestKeys.EXTREME_SOUTH_WEST +
                    Config.RequestKeys.EQUALS +
                    SouhtWestLat +
                    Config.RequestKeys.COMMA +
                    SouhtWestLong +
                    Config.RequestKeys.AND +
                    Config.RequestKeys.EXTREME_NORTH_EAST +
                    Config.RequestKeys.EQUALS +
                    NorthEastLat +
                    Config.RequestKeys.COMMA +
                    NorthEastLong);
            Gson gson = new GsonBuilder().create();
            Reader reader = new InputStreamReader(in);

                try {

                    LocationVO[] locationsResponse = gson.fromJson(reader, LocationVO[].class);

                    Log.v("Drivers size", Integer.toString(locationsResponse.length));

                    return locationsResponse;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    @Override
    protected void onPostExecute(LocationVO[] result) {

        if (mListener != null) {

            if (result != null) {
                mListener.eventosLoaded(result);
            } else {
                mListener.eventosLoaded(null);

            }
        }
    }


    protected InputStream downloadUrl(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(25000);
        conn.setConnectTimeout(20000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }


}

