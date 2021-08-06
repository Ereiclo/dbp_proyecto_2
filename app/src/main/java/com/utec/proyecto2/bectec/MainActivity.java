package com.utec.proyecto2.bectec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void checkCredentials(View view){
        EditText usernameView = findViewById(R.id.username);
        EditText passwordView = findViewById(R.id.password);

        String u = usernameView.getText().toString();
        /*
        if(u.equals("310")) {
            redirect_bectec();
        }

         */

        String p = passwordView.getText().toString();

        Log.w(this.getClass().getName(),":D");

        requestCredentials(u,p);





    }




    private void requestCredentials(String u, String p) {
        BufferedReader reader;

        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Map<String,String> m = new HashMap<String, String>();


        try{
            URL url = new URL("http://192.168.1.34:5002/comprobar-credenciales-android?" + "username=" + u+"&password="+p);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();

            c.setRequestMethod("GET");

            c.setConnectTimeout(5000);
            c.setReadTimeout(5000);

            int status = c.getResponseCode();

            Log.w(this.getClass().getName(),""+status);

            if(status >299){
                reader = new BufferedReader(new InputStreamReader(c.getErrorStream()));
            } else{
                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));

            }
            while((line = reader.readLine()) != null){
                responseContent.append(line);

            }
            reader.close();




            String r = responseContent.toString();


            String[] keyValues = r.split(",");
            int n = keyValues.length;
            for(int i = 0;i<n;i++){
                String keyValue = keyValues[i];
                String[] element = keyValue.split(":");

                m.put(element[0],element[1]);



            }


            Log.w(this.getClass().getName(),r);
            Log.w(this.getClass().getName(),String.valueOf(m.size()));

            c.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }


        EditText us = findViewById(R.id.username);
        TextView us_error = findViewById(R.id.error_username);
        EditText pass = findViewById(R.id.password);
        TextView pass_error = findViewById(R.id.error_password);


        switch (m.get("resultado")) {
            case "success":
                redirect_bectec(m.get("dinero_en_cuenta"), m.get("id"));
                us.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                pass.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                us_error.setVisibility(View.GONE);
                pass_error.setVisibility(View.GONE);

                break;
            case "incorrect_username":
                pass.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                us.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                us_error.setVisibility(View.VISIBLE);
                pass_error.setVisibility(View.GONE);


                break;
            case "incorrect_password":
                us.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));

                pass.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                us_error.setVisibility(View.GONE);
                pass_error.setVisibility(View.VISIBLE);


                break;
        }





    }
    public void redirect_register(View v){
        Intent i= new Intent(this,Register.class);

        startActivity(i);

    }


    private void redirect_bectec(String d,String id){
        Intent i = new Intent(this,BecTec.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("dinero",d);
        i.putExtra("id",id);

        startActivity(i);


    }

    public void redirect_bectec(){

        Intent i = new Intent(this,BecTec.class);
        i.putExtra("dinero","2");

        startActivity(i);


    }

}