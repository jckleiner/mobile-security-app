package com.example.mobilesecurityapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String MAPREQUEST_API_KEY = "RuL9yoj7RFxY48DmI4xhhkwaBv7cWF5y";
    private static final String BREEZOMETER_API_KEY = "8c666930d6914fd092a826bbb854ed3b";

    private EditText inputFieldCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputFieldCity = findViewById(R.id.editTextCity);

        // TODO make a new weather with the last saved city request when the app starts
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
        String url = MessageFormat.format("https://open.mapquestapi.com/geocoding/v1/address?key={0}&location={1},DE"
                , MAPREQUEST_API_KEY, userInput);

        Log.d("userInput" , userInput);
        Log.d("url" , url);

        RequestQueue queue = RequestQueueSingleton.getInstance(this);
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
        List<City> cityList =  new ArrayList<>();

        try{
            JSONArray jsonArrayAllData = new JSONObject(jsonString).getJSONArray("results");
            JSONArray jsonArrayLocations = jsonArrayAllData.getJSONObject(0).getJSONArray("locations");

            for (int i = 0; i < jsonArrayLocations.length(); i++) {
                JSONObject currentObject = jsonArrayLocations.getJSONObject(i);
                String isoCode = currentObject.getString("adminArea1");
                Log.e("isoCode" , isoCode);

                if (StringUtils.equals("DE", isoCode)) {
                    String neighborhood = currentObject.getString("adminArea6");
                    String name = currentObject.getString("adminArea5");
                    String state = currentObject.getString("adminArea3");
                    String countryIsoCode = currentObject.getString("adminArea1");
                    Double lat = currentObject.getJSONObject("latLng").getDouble("lat");
                    Double lng = currentObject.getJSONObject("latLng").getDouble("lng");
                    City city = new City(neighborhood, name, state, countryIsoCode, lat, lng);

                    if (city.isValid()) {
                        cityList.add(city);
                    }
                }
            }
            Log.e("list size", Integer.toString(cityList.size()));
            displayCityListToUser(cityList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayCityListToUser(List<City> cityList) {

        if (cityList.isEmpty()) {
            Toast.makeText(MainActivity.this,"Couldn't find any data for your input", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Choose a City");
        int cityCount = cityList.size();
        String[] newArr = new String[cityCount];

        for (int i = 0; i < cityCount; i++) {
            newArr[i] = cityList.get(i).toString();
        }
        builder.setItems(newArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestWeatherData(cityList.get(which));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void requestWeatherData(City responseCity) {

        String url = MessageFormat.format("https://api.breezometer.com/weather/v1/current-conditions?lat={0}&lon={1}&key={2}",
                String.valueOf(responseCity.getLatitude()), String.valueOf(responseCity.getLongitude()), BREEZOMETER_API_KEY);

        RequestQueue queue = RequestQueueSingleton.getInstance(this);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        if (StringUtils.isNotBlank(stringResponse)) {
                            Log.d("weather", stringResponse);
                            parseWeatherData(stringResponse);
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


    private void parseWeatherData(String jsonString) {
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

            // TODO display the results to the screen

            // TODO add the last result to the database

            System.out.println("weatherText: " + weatherText + ", temp: " + String.valueOf(temparature) + " " + temparatureUnit);
            Log.d("-", MessageFormat.format("Feels like {0} {1}. Humidity: {2}%",feelsLikeTemparature , feelsLikeTemparatureUnit, relativeHumidity));
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
