package com.utec.proyecto2.bectec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class pago extends AppCompatActivity {

    private TextView texto;
    private TextView cantidad;
    private TextView pais;
    private String id_partido;
    private String id_persona;
    private String ps;
    private float p;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);
        pais = findViewById(R.id.apostador);
        cantidad = findViewById(R.id.cantidad);
        id_partido = getIntent().getStringExtra("id_partido");
        p = getIntent().getFloatExtra("dato", 0);
        ps = getIntent().getStringExtra("pais");
        id_persona = getIntent().getStringExtra("id");
        pais.setText("" + ps);
        cantidad.setText("" + p);
        Log.v(this.getClass().getName(),id_partido);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }

    public void bet(View v){
        EditText e = findViewById(R.id.quantity2);
        String mb = e.getText().toString();

        BufferedReader reader;

        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Map<String,String> m = new HashMap<String, String>();


        try{
            URL url = new URL("http://192.168.1.34:5002/monto2?monto=" + mb + "&id_persona=" + id_persona +
                    "&porcentaje=" + String.valueOf(p) + "&id_partido=" + id_partido);
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

        } catch (IOException a) {
            a.printStackTrace();
        }
        Log.v("como?", String.valueOf(m.get("resultado").equals("success")));



        TextView error = findViewById(R.id.error_quantity_deposit2);


        switch (m.get("resultado")){
            case "success":
                Intent i = new Intent(this,BecTec.class);
                float money = Float.parseFloat(getIntent().getStringExtra("m"));
                float flo = (!e.getText().equals("-"))? Float.parseFloat(e.getText().toString()):0;
                i.putExtra("dinero",String.valueOf(money-flo));
                i.putExtra("id",id_persona);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
                break;

            case "incorrect_input":
                e.setText("");
                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.error_incorrect_minput));





                break;
            case "null_bet":
                e.setText("");
                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.null_bet));


                break;
            case "negative_bet":
                e.setText("");
                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.negative_bet));


                break;

            case "insufficient_funds":
                e.setText("");
                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.insufficient_funds));


                break;







        }


        }







    public void cancel_bet(View v){

        onBackPressed();
    }



}
