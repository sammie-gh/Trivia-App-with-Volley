package com.gh.sammie.parasingapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    RequestQueue queue;
    private TextView txt1, txt2;
    //https://jsonplaceholder.typicode.com/todos/1
    public static final String dadJokeurls = "https://icanhazdadjoke.com/";
    public static final String programmingJokes  = "https://sv443.net/jokeapi/category/Programming";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = VolleySingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();

        txt1 = findViewById(R.id.txt_1);
        txt2 = findViewById(R.id.txt_2);


        //use to enque and fecth data
        //json Object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, programmingJokes,
                (JSONObject) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("JSONObject", "onResponse: " + response.getString("joke"));
                    txt1.setText(response.getString("joke"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d("JSONObject", "onResponse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSONObject", "onErrorResponse: " + error.getMessage());

            }
        });

        //JSON ARRAY REQUEST
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                "https://jsonplaceholder.typicode.com/todos", (JSONObject) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("JsonArrayRequest", "onResponse: " + response);

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        boolean d = jsonObject.getBoolean("completed");
                        Log.d("JsonArrayRequest", "onResponse: " + jsonObject.getString("id") + " "
                                + jsonObject.getString("title") + " " + d);
                        txt2.append(jsonObject.getString("id") + " "
                                + jsonObject.getString("title") + " " + d);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
        queue.add(jsonObjectRequest);
//        requestQueue.add(jsonArrayRequest);
//        requestQueue.add(jsonObjectRequest);


    }
}
