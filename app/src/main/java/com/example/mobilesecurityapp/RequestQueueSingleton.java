package com.example.mobilesecurityapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

class RequestQueueSingleton {
    private static RequestQueue mRequestQueue;

    private RequestQueueSingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }
    static synchronized RequestQueue getInstance(Context context) {
        if (mRequestQueue == null) {
            new RequestQueueSingleton(context);
            return mRequestQueue;
        }
        return mRequestQueue;
    }
}
