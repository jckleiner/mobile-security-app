package com.example.mobilesecurityapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String MAPREQUEST_API_KEY = "RuL9yoj7RFxY48DmI4xhhkwaBv7cWF5y";
    private static final String BREEZOMETER_API_KEY = "8c666930d6914fd092a826bbb854ed3b";

    private EditText inputFieldCity;
    private CheckBox checkBoxOnlyGermany;
    private TextView textViewPreviousSearch;
    private DatabaseHelper database;
    private City previousCity;
    private String userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputFieldCity = findViewById(R.id.editTextCity);
        textViewPreviousSearch = findViewById(R.id.textViewPreviousSearch);
        checkBoxOnlyGermany = findViewById(R.id.checkBoxOnlyGermany);
        database = new DatabaseHelper(this);

        Cursor results = database.getAllData();
        while (results.moveToNext()) {
            // 0:ID  1:neighborhood  2:city  3:state  4:isoCode  5:latitude  6:longitude
            String neighborhood = results.getString(1);
            String city = results.getString(2);
            String state = results.getString(3);
            String isoCode = results.getString(4);
            Double latitude = results.getDouble(5);
            Double longitude = results.getDouble(6);
            previousCity = new City(neighborhood, city, state, isoCode, latitude, longitude);
            requestWeatherData(previousCity);
            updatePreviousCityOnScreen();
        }

    }

    public void requestCityInformation(View view) {
        userInput = inputFieldCity.getText().toString().trim().toLowerCase();

        if (userInput.equals("")){
            Toast.makeText(this, "Please enter a city name",Toast.LENGTH_SHORT).show();
            return;
        }
        // show a message on screen if the device has no internet connection
        if (!isDeviceOnline()) {
            Toast.makeText(this, "No Internet Connection",Toast.LENGTH_SHORT).show();
            return;
        }
        hideKeyboard(this);
        // Construct the URL
        String url = MessageFormat.format("https://open.mapquestapi.com/geocoding/v1/address?key={0}&location={1}"
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
    }

    private void parseCityCoordinates(String jsonString) {
        Log.d("response", jsonString);
        Set<City> citySet =  new HashSet<>();

        try{
            JSONArray jsonArrayAllData = new JSONObject(jsonString).getJSONArray("results");
            JSONArray jsonArrayLocations = jsonArrayAllData.getJSONObject(0).getJSONArray("locations");

            for (int i = 0; i < jsonArrayLocations.length(); i++) {
                JSONObject currentObject = jsonArrayLocations.getJSONObject(i);
                String isoCode = currentObject.getString("adminArea1");
                Log.e("isoCode" , isoCode);

                    String neighborhood = currentObject.getString("adminArea6");
                    String name = currentObject.getString("adminArea5");
                    String state = currentObject.getString("adminArea3");
                    String countryIsoCode = currentObject.getString("adminArea1");
                    Double lat = currentObject.getJSONObject("latLng").getDouble("lat");
                    Double lng = currentObject.getJSONObject("latLng").getDouble("lng");
                    City city = new City(neighborhood, name, state, countryIsoCode, lat, lng);

                    Log.e("name", name);
                    Log.e("user input", userInput);


                    if (!checkBoxOnlyGermany.isChecked() && city.isValid() && StringUtils.equalsIgnoreCase(name, userInput)) {
                        citySet.add(city);
                    }
                    else if (checkBoxOnlyGermany.isChecked()
                            && StringUtils.equalsIgnoreCase("DE", isoCode)
                            && city.isValid()
                            && StringUtils.equalsIgnoreCase(name, userInput)) {
                        citySet.add(city);
                    }

            }
            Log.e("list size", Integer.toString(citySet.size()));
            displayCityListToUser(citySet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePreviousCityOnScreen() {

        if (previousCity == null) {
            Log.e("previousCity", "previousCity ************** NULL *************");
            return;
        }

        if (previousCity.getWeatherInfo() == null) {
            Log.e("getWeatherInfo", "getWeatherInfo ************** NULL *************");
            return;
        }

        if (previousCity != null) {
            Log.e("city found", "previously saved city found");
            String text = previousCity.toString() + "\n\n" + previousCity.getWeatherInfo().toString();
            textViewPreviousSearch.setText(text);
        }
    }

    private void displayCityListToUser(Set<City> citySet) {

        if (citySet.isEmpty()) {
            Toast.makeText(MainActivity.this,"Couldn't find any data for your input", Toast.LENGTH_LONG).show();
            return;
        }
        List<City> cityList = new ArrayList<>(citySet);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Choose");
        int cityCount = cityList.size();

        if (cityCount == 1) {
            requestWeatherData(cityList.get(0));
            return;
        }
        String[] newArr = new String[cityCount];

        for (int i = 0; i < cityCount; i++) {
            newArr[i] = cityList.get(i).toString();
        }
        builder.setItems(newArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                City selectedCity = cityList.get(which);
                requestWeatherData(selectedCity);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void requestWeatherData(City selectedCity) {

        String url = MessageFormat.format("https://api.breezometer.com/weather/v1/current-conditions?lat={0}&lon={1}&key={2}",
                String.valueOf(selectedCity.getLatitude()), String.valueOf(selectedCity.getLongitude()), BREEZOMETER_API_KEY);

        RequestQueue queue = RequestQueueSingleton.getInstance(this);
        StringRequest jsonObjReq = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String stringResponse) {
                        if (StringUtils.isNotBlank(stringResponse)) {
                            Log.e("weather", stringResponse);
                            parseWeatherData(selectedCity, stringResponse);
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


    private void parseWeatherData(City selectedCity, String jsonString) {
        try{
            JSONObject jsonObjectData = new JSONObject(jsonString).getJSONObject("data");

            String weatherCondition = jsonObjectData.getString("weather_text");
            String temperature = jsonObjectData.getJSONObject("temperature").getString("value");
            String feelsLikeTemperature = jsonObjectData.getJSONObject("feels_like_temperature").getString("value");
            String relativeHumidity = jsonObjectData.getString("relative_humidity");
            String windSpeed = jsonObjectData.getJSONObject("wind").getJSONObject("speed").getString("value");

            WeatherInfo weatherInfo = new WeatherInfo(weatherCondition, temperature, feelsLikeTemperature, relativeHumidity, windSpeed);

            selectedCity.setWeatherInfo(weatherInfo);

            if (previousCity == null) {
                boolean isInserted = database.insertData(selectedCity);
                Log.e("isInserted", String.valueOf(isInserted));
            }
            else {
                database.updateData(selectedCity);
                Log.e("isUpdated", "updated city");
            }
            previousCity = selectedCity;
            updatePreviousCityOnScreen();

            System.out.println("weatherText: " + weatherCondition + ", temp: " + String.valueOf(temperature) + " C");
            Log.d("-", MessageFormat.format("Feels like {0} {1}. Humidity: {2}%",feelsLikeTemperature , " C", relativeHumidity));
            Log.d("-", MessageFormat.format("Wind speed: {0} {1}",windSpeed , " km/h"));

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            if(netInfo.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        // Close any open database object.
        database.close();
        super.onDestroy();
    }
}
