package com.example.deanolight;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    String mainLightStatus;
    String secondaryLightStatus;
    public static String wifiModuleIp = "192.168.1.112";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zjistiStatus();
        mainLightImg = (ImageView) findViewById(R.id.mainLightImage);
        secondaryLightImage = (ImageView) findViewById(R.id.secondaryLightImage);

        mainLightImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                zmenStav("mainLight");
            }
        });

        secondaryLightImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                zmenStav("secondaryLight");
            }
        });
    }

    private void zjistiStatus(){
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        String url= "http://"+wifiModuleIp+"/rest_status";
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
            String mainValue = jObject.getString("mainLight");
            Log.i("Main: ",mainValue);
            String secondaryValue = jObject.getString("secondaryLight");
            Log.i("Secondary: ",secondaryValue);

            if(mainValue.equals("0")){
                mainLightImg.setImageResource(R.drawable.on);
                mainLightStatus = "off";
            }
            else {mainLightImg.setImageResource(R.drawable.off);
                mainLightStatus = "on";
            }

        if(secondaryValue.equals("0")){
            secondaryLightImage.setImageResource(R.drawable.on);
            secondaryLightStatus = "off";
        }
        else {secondaryLightImage.setImageResource(R.drawable.off);
              secondaryLightStatus = "on";
        }
     }

     private void zmenStav(String svetlo){
         RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
         String status = zjistiStatusRychly(svetlo);
         String url= "http://"+wifiModuleIp+"/"+svetlo+"/"+status;
         Log.i("URL: ",url);
         StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
             zjistiStatus();
             }
     }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("error",error.toString());
        }
    });
      queue.add(stringRequest);
     }

     private String zjistiStatusRychly(String svetlo){
        if(svetlo.equals("mainLight")){
            return mainLightStatus ;
        }
        else if(svetlo.equals("secondaryLight")){
            return secondaryLightStatus;
        }
        else {
            return "error";
        }
        }



}
