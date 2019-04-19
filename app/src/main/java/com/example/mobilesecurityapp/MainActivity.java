package com.example.mobilesecurityapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String MAPREQUEST_API_KEY = "RuL9yoj7RFxY48DmI4xhhkwaBv7cWF5y";
    public static final String BREEZOMETER_API_KEY = "8c666930d6914fd092a826bbb854ed3b";

    private EditText inputFieldCity;
    private Button buttonFetchData;

    String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputFieldCity = findViewById(R.id.editTextCity);
        buttonFetchData = findViewById(R.id.buttonFetchData);
    }

    public void requestCityInformation(View view) {
        String userInput = inputFieldCity.getText().toString().trim().toLowerCase();

        if (userInput.equals("")){
            Toast.makeText(this, "Please enter a city name",Toast.LENGTH_SHORT).show();
            return;
        }
        // show a message on screen if the device has no internet connection
        if (!isDeviceOnline()) {
            Toast.makeText(this, "No Internet Connection",Toast.LENGTH_SHORT).show();
            return;
        }
        // url encoding for spaces
        userInput = userInput.replace(" ", "%20");
        // Construct the URL
        String url = MessageFormat.format("http://open.mapquestapi.com/geocoding/v1/address?key={0}&location={1}"
                , MAPREQUEST_API_KEY, userInput);
        Log.d("userInput" , userInput);
        Log.d("url" , url);
        //RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = RequestQueueSingleton.getInstance(this);
        /**
         * JsonObjectRequest takes in five paramaters
         * Request Type - This specifies the type of the request eg: GET,POST
         * URL          - This String param specifies the Request URL
         * JSONObject   - This parameter takes in the POST parameters."null" in
         *                  case of GET request.
         * Listener     -This parameter takes in a implementation of Response.Listener()
         *                 interface which is invoked if the request is successful
         * Listener     -This parameter takes in a implementation of Error.Listener()
         *               interface which is invoked if any error is encountered while processing
         *               the request
         **/
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        if (StringUtils.isNotBlank(stringResponse)) {
                            parseCityCoordinates(stringResponse);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Oops, something went wrong.", Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjReq);
//        closeKeyboard();
    }

    private void parseCityCoordinates(String jsonString) {
        Log.d("response", jsonString);

        try{
            JSONArray jsonArrayAllData = new JSONObject(jsonString).getJSONArray("results");
            JSONArray jsonArrayLocations = jsonArrayAllData.getJSONObject(0).getJSONArray("locations");
            ArrayList<String> resultList = new ArrayList<>();

            Map<String, Double[]> myMap = new HashMap<>();



            for (int i = 0; i < jsonArrayLocations.length(); i++) {
                JSONObject currentObject = jsonArrayLocations.getJSONObject(i);
                String countryCode = currentObject.getString("adminArea1");
                Log.e("countryCode" , countryCode);
                if (!myMap.containsKey(countryCode)) {
                    Double lat = currentObject.getJSONObject("latLng").getDouble("lat");
                    Double lng = currentObject.getJSONObject("latLng").getDouble("lng");
                    Double[] coordinations = {lat, lng};
//                    resultList.add(countryCode);
                    myMap.put(countryCode, coordinations);
                }
            }

            for (String s : resultList) {
                Log.d("adminArea1", s);
            }

            myMap.forEach((k, v) -> Log.d("k,v", k + ": {" + v[0] + ", " + v[1] + "}"));
            // TODO display options, when user clicks on one fetch the data
            getWeatherData(myMap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWeatherData(Map<String, Double[]> myMap) {

        Double[] arr = myMap.get("DE");

        String url = MessageFormat.format("https://api.breezometer.com/weather/v1/current-conditions?lat={0}&lon={1}&key={2}"
                , arr[0], arr[1], BREEZOMETER_API_KEY);


        RequestQueue queue = RequestQueueSingleton.getInstance(this);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        if (StringUtils.isNotBlank(stringResponse)) {
                            Log.d("weather", stringResponse);
                            fetchWeatherData(stringResponse);
//                            parseCityCoordinates(stringResponse);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Oops, something went wrong.", Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(jsonObjReq);
    }


    private void fetchWeatherData(String jsonString) {

        Log.e("a", "inside weather data");

        try{
            JSONObject jsonObjectData = new JSONObject(jsonString).getJSONObject("data");

            String weatherText = jsonObjectData.getString("weather_text");
            Double temparature = jsonObjectData.getJSONObject("temperature").getDouble("value");
            String temparatureUnit = jsonObjectData.getJSONObject("temperature").getString("units");
            Double feelsLikeTemparature = jsonObjectData.getJSONObject("feels_like_temperature").getDouble("value");
            String feelsLikeTemparatureUnit = jsonObjectData.getJSONObject("feels_like_temperature").getString("units");
            Double relativeHumidity = jsonObjectData.getDouble("relative_humidity");
            Double windSpeed = jsonObjectData.getJSONObject("wind").getJSONObject("speed").getDouble("value");
            String windSpeedUnit = jsonObjectData.getJSONObject("wind").getJSONObject("speed").getString("units");


            System.out.println("weatherText: " + weatherText + ", temp: " + String.valueOf(temparature) + " " + temparatureUnit);
//            Log.d("a123", MessageFormat.format("Today it's  {0}  . Temparature  {1} {2}", weatherText , String.valueOf(temparature), temparatureUnit));
            Log.d("-", MessageFormat.format("Feels like {0} {1}. Humidity: {2}",feelsLikeTemparature , feelsLikeTemparatureUnit, relativeHumidity));
            Log.d("-", MessageFormat.format("Wind speed: {0} {1}",windSpeed , windSpeedUnit));

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

// TODO doesn't work?
//    private void closeKeyboard() {
//        // The view that has currently focus
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager inputMethodManager =
//                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }

    protected boolean isDeviceOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            if(netInfo.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }
}
