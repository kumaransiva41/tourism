package com.example.admin.pewds_tourism_portal_user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;


public class guidefrag extends Fragment {
    ArrayList<String> alist, alist1;
    private ArrayAdapter<String> adapterg;
    Spinner spguide;
    TextView tinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_guidefrag, container, false);
        // Inflate the layout for this fragment
        spguide = view.findViewById(R.id.spguide);
        tinfo = view.findViewById(R.id.tinfo);
        new BG1().execute("getlocs");
        spguide.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                tinfo.setText(alist1.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }
    public class BG1 extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... voids) {
            String method = voids[0];

            if (method.equals("getlocs")) {
                try {
                    String login_url = "https://androidcon.000webhostapp.com/segetloc.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

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

                    String locname, linfo;
                    alist = new ArrayList<String>();
                    alist1 = new ArrayList<String>();
                    JSONObject job = new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for (int i = 0; i < jar1.length(); i++) {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject) jar1.get(i);
                        linfo = jval.get("linfo").toString();

                        locname = jval.get("lname").toString();
                        alist.add(locname);
                        alist1.add(linfo);
                        Log.d("BG1", linfo + "");


                    }


                } catch (Exception ex) {
                    Log.d("Error", ex.toString());
                }
                return method;
            }


            return method;
        }

        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            try {
                // Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                if (response.equals("getlocs")) {
                    adapterg = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, alist);
                    spguide.setAdapter(adapterg);
                }
            }catch (Exception e)
            {
                Toast.makeText(getContext(),"Loading...",Toast.LENGTH_SHORT).show();
            }

        }
    }

}
