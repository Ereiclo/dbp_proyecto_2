package com.utec.proyecto2.bectec;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText EtDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EtDate = findViewById(R.id.fecha);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int mes = calendar.get(Calendar.MONTH);
        final int dia = calendar.get(Calendar.DAY_OF_MONTH);

        EtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year2, int month, int day) {
                        month = month +1;
                        if(month < 10 && day<10){
                            String date = year2 + "-" + "0"+ month + "-"+ "0"+day ;
                            EtDate.setText(date);


                        }else if (month<10){
                            String date = year2 + "-" + "0"+ month + "-"+ day ;
                            EtDate.setText(date);

                        }else if(day<10){
                            String date = year2 + "-" + month + "-"+ "0"+day ;
                            EtDate.setText(date);

                        }else{
                            String date = year2 + "-" + month + "-"+day ;
                            EtDate.setText(date);

                        }


                    }
                },year,mes,dia);
                datePickerDialog.show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

    }

    public void cancel(View v){
        Intent i = new Intent(this, MainActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);

    }


    public void register(View v){
        Intent j = new Intent(this, MainActivity.class);

        j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TextView error1 = findViewById(R.id.error_username_register);
        TextView error2 = findViewById(R.id.error_password_register);
        TextView error3 = findViewById(R.id.error_birth_day_register);


        EditText e = findViewById(R.id.user);
        String u = e.getText().toString();

        EditText c = findViewById(R.id.usern);
        String c2 = c.getText().toString();

        EditText uc = findViewById(R.id.usern_conf);
        String uc2= uc.getText().toString();

        EditText em = findViewById(R.id.em);

        String em2 = em.getText().toString();

        EditText name = findViewById(R.id.name);
        String name2 = name.getText().toString();

        EditText ln = findViewById(R.id.lastn);
        String ln2 = ln.getText().toString();

        EditText f = findViewById(R.id.fecha);
        String f2 = f.getText().toString();


        BufferedReader reader;

        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Map<String,String> m = new HashMap<String, String>();


        try{
            URL url = new URL("http://192.168.1.34:5002/register-verify-android?usuario=" + u + "&apellidos=" + ln2 +
                    "&nombre=" + name2 + "&email=" + em2 + "&password=" + c2 + "&confirm-password="+uc2 + "&fecha_nacimiento=" + f2);

            HttpURLConnection c4 = (HttpURLConnection) url.openConnection();

            c4.setRequestMethod("GET");

            c4.setConnectTimeout(5000);
            c4.setReadTimeout(5000);

            int status = c4.getResponseCode();

            Log.w(this.getClass().getName(),""+status);

            if(status >299){
                reader = new BufferedReader(new InputStreamReader(c4.getErrorStream()));
            } else{
                reader = new BufferedReader(new InputStreamReader(c4.getInputStream()));

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


            c4.disconnect();

        } catch (IOException a) {
            a.printStackTrace();
        }

        switch (m.get("resultado")){
            case "success":


                startActivity(j);
                break;

            case "username_already_exists":

                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                uc.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                f.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));

                e.setText("");
                error1.setText(getText(R.string.text_error_username_register));
                error1.setVisibility(View.VISIBLE);
                error2.setVisibility(View.GONE);
                error3.setVisibility(View.GONE);


                break;


            case "different_password":
                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                uc.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                f.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));


                c.setText("");

                uc.setText("");

                error2.setText(getText(R.string.error_pwdm));
                error1.setVisibility(View.GONE);
                error2.setVisibility(View.VISIBLE);
                error3.setVisibility(View.GONE);





                break;
            case "password_invalid_length":

                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                uc.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                f.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));


                c.setText("");

                uc.setText("");

                error2.setText(getString(R.string.error_pw_invalid_length));
                error1.setVisibility(View.GONE);
                error2.setVisibility(View.VISIBLE);
                error3.setVisibility(View.GONE);





                break;

            case "password_missing_digits":

                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                uc.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                f.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));


                c.setText("");

                uc.setText("");


                error2.setText(getText(R.string.error_pw_mc));

                error1.setVisibility(View.GONE);
                error2.setVisibility(View.VISIBLE);
                error3.setVisibility(View.GONE);



                break;
            case "invalid_birthdate":


                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                uc.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                f.setBackgroundTintList(getColorStateList(R.color.manejo_errores));


                error3.setText(getText(R.string.error_invalid_birth_day));

                error1.setVisibility(View.GONE);
                error2.setVisibility(View.GONE);
                error3.setVisibility(View.VISIBLE);


                break;
            case "null_birthdate":

                e.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                uc.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                f.setBackgroundTintList(getColorStateList(R.color.manejo_errores));

                error3.setText(getText(R.string.error_null_birthday));

                error1.setVisibility(View.GONE);
                error2.setVisibility(View.GONE);
                error3.setVisibility(View.VISIBLE);




                break;

        }

        }






}