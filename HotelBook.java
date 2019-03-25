package com.example.admin.pewds_tourism_portal_user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class HotelBook extends AppCompatActivity {
Spinner sphotel;
TextView hrate,hcost,htotal,hdays;
    ArrayList<String> alist,alist1,alist2,alist3;
    Button btp,btm,pay;

    private ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_book);
        sphotel=findViewById(R.id.sphotel);
        hrate=findViewById(R.id.hrate);
        hcost=findViewById(R.id.hcost);
        btm=findViewById(R.id.btm);
        btp=findViewById(R.id.btp);
        htotal=findViewById(R.id.htotal);
        hdays=findViewById(R.id.hdays);
        pay=findViewById(R.id.pay);

        SharedPreferences sf = getSharedPreferences("usernamefile", Context.MODE_PRIVATE);

        String lid1 = sf.getString("locationid", "NA");
        new BG().execute("gethotels",lid1);
        sphotel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int x=alist.indexOf(sphotel.getSelectedItem().toString());
                hrate.setText(alist2.get(x));
                hcost.setText(alist3.get(x));
                hdays.setText("1");
                htotal.setText(hcost.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int val = Integer.parseInt(hdays.getText().toString());
                    if (val > 1)
                        val--;
                    hdays.setText(Integer.toString(val));
                    val *= Integer.parseInt(hcost.getText().toString());
                    htotal.setText(Integer.toString(val));
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Please wait while we load.",Toast.LENGTH_SHORT).show();

                }
            }
        });
        btp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              try {
                  int val = Integer.parseInt(hdays.getText().toString());
                  val++;
                  hdays.setText(Integer.toString(val));
                  val *= Integer.parseInt(hcost.getText().toString());
                  htotal.setText(Integer.toString(val));
              }catch (Exception e)
              {
                  Toast.makeText(getApplicationContext(),"Please wait while we load.",Toast.LENGTH_SHORT).show();
              }

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sf = getSharedPreferences("usernamefile", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=sf.edit();
                edit.putString("hcost",hcost.getText().toString());
                edit.putString("days",hdays.getText().toString());
                edit.putString("hid",alist1.get(alist.indexOf(sphotel.getSelectedItem().toString())));

                edit.commit();
                startActivity(new Intent(getApplicationContext(),Flight.class));
            }
        });
    }
    public class BG extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... voids) {
            String method=voids[0];

            if(method.equals("gethotels"))
            {String lid=voids[1];
                try {
                    String login_url = "https://androidcon.000webhostapp.com/segethotels.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("lid", "UTF-8") + "=" + URLEncoder.encode(lid, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    //     String response=" ";
                    String line = " ";
                    StringBuilder response = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line + '\n');
                    }
                    bufferedReader.close();
                    inputStream.close();
                    //    JOptionPane.showMessageDialog(null,response);

                    String hname,hid,rating,cost;
                    alist=new ArrayList<String>();
                    alist1=new ArrayList<String>();
                    alist2=new ArrayList<String>();
                    alist3=new ArrayList<String>();
                    JSONObject job =new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for(int i=0;i<jar1.length();i++)
                    {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject)jar1.get(i);
                        hid=jval.get("hid").toString();

                        hname=jval.get("hname").toString();
                        rating=jval.get("rating").toString();
                        cost=jval.get("cost").toString();
                        alist.add(hname);
                        alist1.add(hid);
                        alist2.add(rating);
                        alist3.add(cost);
                        Log.d("size",alist1.size()+"");



                    }
                   // htotal.setText(alist3.get(alist.indexOf(sphotel.getSelectedItem().toString())));


                }catch (Exception ex)
                {
                    Log.d("Error",ex.toString());
                }
                return method;
            }


            return method;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            // Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
            if(response.equals("gethotels")) {
                adapter2 = new ArrayAdapter<String>(HotelBook.this, android.R.layout.simple_list_item_1, alist);
                sphotel.setAdapter(adapter2);
            }


        }
    }
}
