package com.utec.proyecto2.bectec;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Information#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Information extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String id;
    public Information() {
        // Required empty public constructor
    }

    public Information(String id) {
        this.id= id;

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Information.
     */
    // TODO: Rename and change types and number of parameters
    public static Information newInstance(String param1, String param2) {
        Information fragment = new Information();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] d = data();
        View i=inflater.inflate(R.layout.fragment_information, container, false);
        TextView uno = i.findViewById(R.id.user);
        uno.setText(d[0]);
        TextView dos = i.findViewById(R.id.name);
        dos.setText(d[1]);
        TextView tres = i.findViewById(R.id.appe);
        tres.setText(d[2]);
        TextView cuatro = i.findViewById(R.id.date);
        cuatro.setText(d[3]);
        TextView cinco = i.findViewById(R.id.password1);
        cinco.setText(d[5]);
        TextView seis = i.findViewById(R.id.password2);
        seis.setText(d[5]);
        TextView siete = i.findViewById(R.id.email);
        siete.setText(d[6]);

        TextView nueve = i.findViewById(R.id.money_account);
        nueve.setText(getString(R.string.text_account_money) + d[4]);

        Log.v(this.getClass().getName(),data()[4]);
        return i;
    }




    private String[] data(){
        BufferedReader reader;
        String[] respuesta = {"","","","","","",""};
        String line;
        StringBuilder responseContent= new StringBuilder();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        try{
            URL url = new URL("http://192.168.1.34:5002/inf?id="+id);
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



        return responseContent.toString().split(",");

    }



}