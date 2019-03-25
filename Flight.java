package com.example.admin.pewds_tourism_portal_user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Flight extends AppCompatActivity {

    private ArrayAdapter<String> adapterf;
    private ArrayList<String> alist, alist1;
    Spinner spf;
    TextView frate, myloc, fprice;
    private ArrayList<String> alist2, alist3;
    Button cflight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(latitude, longitude, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String cityName = addresses.get(0).getLocality();
//        Log.d("", "onCreate: "+cityName);
//        Toast.makeText(this,cityName,Toast.LENGTH_SHORT).show();
        cflight=findViewById(R.id.cflight);
        spf = findViewById(R.id.fsp);
        fprice = findViewById(R.id.fprice);
        frate = findViewById(R.id.frate);
        myloc = findViewById(R.id.myloc);

        myloc.setText("");
        new BG().execute("getflights");
        spf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fprice.setText(alist3.get(i));
                frate.setText(alist2.get(i));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


cflight.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        SharedPreferences sf = getSharedPreferences("usernamefile", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sf.edit();
        edit.putString("fcost",fprice.getText().toString());

        edit.putString("fid",alist1.get(alist.indexOf(spf.getSelectedItem().toString())));
        edit.commit();
        startActivity(new Intent(getApplicationContext(),Bill.class));
    }
});

    }
        public class BG extends AsyncTask<String,Void,String> {



        @Override
        protected String doInBackground(String... voids) {
            String method=voids[0];


            if(method.equals("getflights"))
            {
                try {
                    Log.d("CHECK", "doInBackground: INSIDE");

                    SharedPreferences sf = getSharedPreferences("usernamefile", Context.MODE_PRIVATE);

                    String lid = sf.getString("locationid", "NA");


                    String login_url = "https://androidcon.000webhostapp.com/segetflights.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("lid","UTF-8")+"="+URLEncoder.encode(lid,"UTF-8");
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

                    String fname,fprice,frate,fid;
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
                        fid=jval.get("fid").toString();

                        fname=jval.get("fname").toString();
                        frate=jval.get("rating").toString();

                        fprice=jval.get("cost").toString();
                        alist.add(fname);
                        alist1.add(fid);
                        alist2.add(frate);
                        alist3.add(fprice);
                        Log.d("size",alist1.size()+"");


                    }


                }catch (Exception ex)
                {
                    Log.d("Error",ex.toString());
                }

            }
            return method;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            // Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
            if(response.equals("getflights")) {
                adapterf = new ArrayAdapter<String>(Flight.this, android.R.layout.simple_list_item_1, alist);
                spf.setAdapter(adapterf);
            }


        }
    }
}
