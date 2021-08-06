package com.utec.proyecto2.bectec;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BecTec extends AppCompatActivity {
    Bet bet = new Bet();
    RealDeposit real_deposit;
    Deposit deposit = new Deposit();

    Information information;
    float m;
    String id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bec_tec);
        float d = Float.parseFloat(getIntent().getExtras().getString("dinero"));

        m += d;

        id = getIntent().getExtras().getString("id");

        real_deposit = new RealDeposit(id);
        information = new Information(id);

        

        getSupportActionBar().setTitle("BecTec                      "+getString(R.string.header_money,String.valueOf(m)));
        BottomNavigationView n = findViewById(R.id.navigation_bar);
        n.setOnItemSelectedListener(redirect);
        changeFragment(bet);
        Log.v(this.getClass().getName(),id);

    }



    private final NavigationBarView.OnItemSelectedListener redirect = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.firstItem:
                    Log.w(this.getClass().getName(),"aaaa");

                    changeFragment(bet);
                    askMoney();
                    return true;

                /*case R.id.secondItem:
                    Log.w(this.getClass().getName(),"bbbb");
                    changeFragment(bet_history);
                    return true;

                 */
                case R.id.thirdItem:



                    Log.v(this.getClass().getName(), String.valueOf(card_quantity()));
                    if(card_quantity()>0){


                        changeFragment(real_deposit);



                    }else if(card_quantity()==-1) {
                        Log.v(this.getClass().getName(), "Esto no debería pasar");
                    }else {
                        changeFragment(deposit);

                    }
                    askMoney();

                    Log.w(this.getClass().getName(),"cccc");




                    return true;

                case R.id.fourthItem:


                    Log.w(this.getClass().getName(),"dddd");
                    changeFragment(information);
                    askMoney();


                    return true;

            }
            return false;


        }
    };


    public void logOff(View v){
        Intent i = new Intent(this, MainActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(i);



    }

    private void askMoney(){
        BufferedReader reader;

        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String r = "";



        try{
            URL url = new URL("http://192.168.1.34:5002/ask_money?id=" + id);
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




            r = responseContent.toString();



            Log.w(this.getClass().getName(),r);


            c.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        m = Float.parseFloat(r);
        getSupportActionBar().setTitle("BecTec                      "+getString(R.string.header_money,String.valueOf(m)));






    }


    public void changeData(View v){
        BufferedReader reader;
        EditText et1 = findViewById(R.id.email);
        EditText et2 = findViewById(R.id.password1);
        EditText et3 = findViewById(R.id.password2);
        String r = "";
        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Map<String,String> m = new HashMap<String, String>();


        try{
            URL url = new URL("http://192.168.1.34:5002/change_data_android?id="+id+"&email="+ et1.getText().toString() + "&password=" + et2.getText().toString() + "&confirm-password=" + et3.getText().toString() );
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




            r = responseContent.toString();




            Log.w(this.getClass().getName(),r);


            c.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }


        TextView error = findViewById(R.id.error_information);
        
        switch (r){
            case "success":
                et2.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                et3.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                error.setVisibility(View.GONE);



                break;




            case "different_password":
                et2.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                et3.setBackgroundTintList(getColorStateList(R.color.manejo_errores));

                et2.setText("");
                et3.setText("");



                error.setVisibility(View.VISIBLE);
                error.setText(getText(R.string.error_pwdm));




                break;
            case "password_invalid_length":

                et2.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                et3.setBackgroundTintList(getColorStateList(R.color.manejo_errores));

                et2.setText("");
                et3.setText("");



                error.setVisibility(View.VISIBLE);
                error.setText(getText(R.string.error_pw_invalid_length));




                break;

            case "password_missing_digits":

                et2.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                et3.setBackgroundTintList(getColorStateList(R.color.manejo_errores));

                et2.setText("");
                et3.setText("");



                error.setVisibility(View.VISIBLE);
                error.setText(getText(R.string.error_pw_mc));




                break;



        }





    }

    public void add_money1(View v){

        EditText mView = findViewById(R.id.quantity);
        String m2 = mView.getText().toString();
        Boolean r = add_money2(m2) && !m2.equals("-");
        Log.v("ssss",String.valueOf(r));
        if (r) {
            m += Float.parseFloat(m2);
        }

        Log.v("ssss",String.valueOf(m));
        getSupportActionBar().setTitle("BecTec                      "+getString(R.string.header_money,String.valueOf(m)));

        mView.setText("");





    }

    public void deleteCreditCard(android.view.View v){

        BufferedReader reader;

        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        try{
            URL url = new URL("http://192.168.1.34:5002/todos/"+id+"/delete-card");
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




            Log.w(this.getClass().getName(),responseContent.toString());


            c.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }



        if(responseContent.toString().equals("success")){
            changeFragment(deposit);
        }

    }



    private Boolean add_money2(String m){
        BufferedReader reader;

        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        try{
            URL url = new URL("http://192.168.1.34:5002/agregar-dinero-android?id="+id+"&money="+m);
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




            Log.w(this.getClass().getName(),responseContent.toString());


            c.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        EditText mView = findViewById(R.id.quantity);
        TextView error = findViewById(R.id.error_quantity_deposit);

        switch (responseContent.toString()){
            case "success":
                mView.setText("");
                mView.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                error.setVisibility(View.GONE);

                return true;
            case "incorrect_input":
                mView.setText("");
                mView.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.error_incorrect_minput));





                return false;
            case "null_deposit":
                mView.setText("");
                mView.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.error_null_deposit));


                return false;
            case "negative_deposit":
                mView.setText("");
                mView.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                error.setVisibility(View.VISIBLE);
                error.setText(getString(R.string.error_negative_deposit));


                return false;



        }




        return false;
    }




    private int card_quantity(){
        BufferedReader reader;

        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Map<String,String> m = new HashMap<String, String>();


        try{
            URL url = new URL("http://192.168.1.34:5002/cantidad-tarjetas?id="+id);
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




            Log.w(this.getClass().getName(),responseContent.toString());


            c.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Integer.parseInt(responseContent.toString());

    }



    public void add_credit_card(View v){

        EditText p1 = findViewById(R.id.part1_credit_card);
        EditText p2 = findViewById(R.id.part2_credit_card);
        EditText p3 = findViewById(R.id.part3_credit_card);
        EditText c = findViewById(R.id.cvv);
        EditText t = findViewById(R.id.credit_card_owner);

        get_add_credit_card(p1.getText().toString(),p2.getText().toString(),p3.getText().toString(),c.getText().toString(),t.getText().toString(),id);






    }

    public void eleccion1(View view){
        String[] img = view.getTag().toString().split(" ");
        String pais = img[0];
        String m = img[1];
        String id_partido = img[2];
        float codigo = Float.parseFloat(m);
        Intent i = new Intent(this,pago.class);
        i.putExtra("dato",codigo);
        i.putExtra("pais",pais);
        i.putExtra("id",id);
        i.putExtra("id_partido",id_partido);
        i.putExtra("m",String.valueOf(this.m));




        Log.v(this.getClass().getName(),pais+m);
        startActivity(i);
    }






    public void get_add_credit_card(String parte1, String parte2, String parte3,String cvv,String titular,String id){
        BufferedReader reader;

        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Map<String,String> m = new HashMap<String, String>();



        try{
            URL url = new URL("http://192.168.1.34:5002/comprobar-tarjeta-android?" + "parte1=" + parte1+"&parte2="+ parte2+
                    "&parte3="+parte3+"&cvv="+cvv+"&titular="+titular+"&id="+id);
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

        EditText p1 = findViewById(R.id.part1_credit_card);
        EditText p2 = findViewById(R.id.part2_credit_card);
        EditText p3 = findViewById(R.id.part3_credit_card);
        EditText c = findViewById(R.id.cvv);
        EditText t = findViewById(R.id.credit_card_owner);


        TextView m1 = findViewById(R.id.firstMinus);
        TextView m2 = findViewById(R.id.secondMinus);
        TextView error1 = findViewById(R.id.error_credit_card_number);
        TextView error2 = findViewById(R.id.error_cvv);
        TextView error3 = findViewById(R.id.error_owner_name);

        switch (m.get("resultado")){
            case "success":
                changeFragment(real_deposit);

                break;
            case "invalid_card_number":
                p1.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                p2.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                p3.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                m1.setTextColor(getColor(R.color.red));
                m2.setTextColor(getColor(R.color.red));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                t.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));


                p1.setText("");
                p2.setText("");
                p3.setText("");


                error1.setVisibility(View.VISIBLE);
                error2.setVisibility(View.GONE);
                error3.setVisibility(View.GONE);





                break;
            case "invalid_ccv":
                p1.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                p2.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                p3.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                t.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                m1.setTextColor(getColor(R.color.darkGray));
                m2.setTextColor(getColor(R.color.darkGray));

                c.setText("");




                error1.setVisibility(View.GONE);
                error2.setVisibility(View.VISIBLE);
                error3.setVisibility(View.GONE);

                break;
            case "invalid_user":
                p1.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                p2.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                p3.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                c.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
                t.setBackgroundTintList(getColorStateList(R.color.manejo_errores));
                m1.setTextColor(getColor(R.color.darkGray));
                m2.setTextColor(getColor(R.color.darkGray));

                t.setText("");




                error1.setVisibility(View.GONE);
                error2.setVisibility(View.GONE);
                error3.setVisibility(View.VISIBLE);

                break;

        }






    }

    public void credit_card_exists(){
        View view1 = findViewById(R.id.layout_for_null_credit_card);
        View view2= findViewById(R.id.add_credit_card_layout);
        show(view2);
        hide(view1);

    }

    public void add(View v){
        View view1 = findViewById(R.id.layout_for_null_credit_card);
        View view2= findViewById(R.id.credit_card_box);
        show(view2);
        hide(view1);



    }

    public void cancel(View v){
        TextView error1 = findViewById(R.id.error_credit_card_number);
        TextView error2 = findViewById(R.id.error_cvv);
        TextView error3 = findViewById(R.id.error_owner_name);

        EditText p1 = findViewById(R.id.part1_credit_card);
        EditText p2 = findViewById(R.id.part2_credit_card);
        EditText p3 = findViewById(R.id.part3_credit_card);
        EditText c = findViewById(R.id.cvv);
        EditText t = findViewById(R.id.credit_card_owner);
        TextView m1 = findViewById(R.id.firstMinus);
        TextView m2 = findViewById(R.id.secondMinus);


        m1.setTextColor(getColor(R.color.darkGray));
        m2.setTextColor(getColor(R.color.darkGray));
        p1.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
        p2.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
        p3.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
        c.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
        t.setBackgroundTintList(getColorStateList(R.color.manejo_errores2));
        p1.setText("");
        p2.setText("");
        p3.setText("");
        c.setText("");
        t.setText("");
        error1.setVisibility(View.GONE);
        error2.setVisibility(View.GONE);
        error3.setVisibility(View.GONE);

        View view1 = findViewById(R.id.layout_for_null_credit_card);
        View view2= findViewById(R.id.credit_card_box);
        show(view1);
        hide(view2);


    }
    private void hide(View v){


        v.setVisibility(View.GONE);


    }

    private void show(View v){

        v.setVisibility(View.VISIBLE);
    }



    private void changeFragment(Fragment f){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.scroll_view, f);
        ft.commit();


    }






}