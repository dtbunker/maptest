package com.assis.redondo.daniel.maptest.api;

import android.content.Context;
import android.util.Log;

import com.assis.redondo.daniel.maptest.Config;
import com.assis.redondo.daniel.maptest.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


public class LoopJAPI {

    private static final int timeOut = 10000;

    private static final String contentType = "application/json";
    private static final String contentTypeMultiPart = "multipart/form-data";

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void get(Context context, String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler ) {
        if(params != null) {
            Log.v("PARAMS:", params.toString());
        }
        client.addHeader(context.getResources().getString(R.string.content_type),
                context.getResources().getString(R.string.content_type_info));

        client.get(getAbsoluteUrl(url), params, jsonHttpResponseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        Log.v("REQUEST URL : ", Config.BASE_API_METHOD_URL + relativeUrl);
        return Config.BASE_API_METHOD_URL + relativeUrl;
    }


}