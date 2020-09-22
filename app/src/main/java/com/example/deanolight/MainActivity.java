package com.example.deanolight;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    ImageView mainLightImg;
    ImageView secondaryLightImage;
    public static String wifiModuleIp = "";
    public static int wifiModulePort = 0;
    public static String CMD = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendGetRequest();

        mainLightImg = (ImageView) findViewById(R.id.mainLightImage);
        secondaryLightImage = (ImageView) findViewById(R.id.secondaryLightImage);

        mainLightImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //sendGetRequest
            }
        });



    }

    private void sendGetRequest(){
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        String url="http://www.gist.cz/zakaznici/DR/stav.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("Response: ",response);
                try {
                    stavSvetel(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mainLightImg.setImageResource(R.drawable.x);
            }

    });

        queue.add(stringRequest);
    }

    private void stavSvetel(String response) throws JSONException {


            JSONObject jObject = new JSONObject(response);
            String mainValue = jObject.getString("main");
            Log.i("Main: ",mainValue);
            String secondaryValue = jObject.getString("secondary");
            Log.i("Secondary: ",secondaryValue);

            if(mainValue.equals("1")){
                mainLightImg.setImageResource(R.drawable.on);
            }
            else {mainLightImg.setImageResource(R.drawable.off);}

        if(secondaryValue.equals("1")){
            secondaryLightImage.setImageResource(R.drawable.on);
        }
        else {secondaryLightImage.setImageResource(R.drawable.off);}

        }



}
