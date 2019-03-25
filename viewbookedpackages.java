package com.example.admin.pewds_tourism_portal_user;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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


public class viewbookedpackages extends Fragment {
    TextView hotel;
    TextView flight;
    TextView total,pcost;
    Spinner booked;
    ListView act;
    ArrayList<String> flightcosts, hotelcosts,packcost, totalcosts, bookedpackages,bpnames,anamelist;
    ArrayAdapter<String> bookedpackagesadap,actadap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_viewbookedpackages, container, false);
        flightcosts=new ArrayList<String>();
        hotelcosts=new ArrayList<String>();
        anamelist=new ArrayList<String>();
        packcost=new ArrayList<String>();
        totalcosts=new ArrayList<String>();
        bookedpackages=new ArrayList<String>();
        bpnames=new ArrayList<String>();
        hotel = v.findViewById(R.id.bhcost);
        flight = v.findViewById(R.id.bfcost);
        pcost = v.findViewById(R.id.bpcost);
        total = v.findViewById(R.id.tcost);
        booked = v.findViewById(R.id.bookedpackagepackagespinner);
        act = v.findViewById(R.id.lvactivities);
        actadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, anamelist);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            new viewbookedpackages.BG().execute("view");
            booked.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    anamelist.clear();

                new BG().execute("loadlv");
                    actadap.notifyDataSetChanged();
                    hotel.setText(hotelcosts.get(bpnames.indexOf(booked.getSelectedItem().toString())));
                    flight.setText(flightcosts.get(bpnames.indexOf(booked.getSelectedItem().toString())));
                    total.setText(totalcosts.get(bpnames.indexOf(booked.getSelectedItem().toString())));
                    pcost.setText(packcost.get(bpnames.indexOf(booked.getSelectedItem().toString())));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(getActivity(),"please choose a package to view details",Toast.LENGTH_LONG).show();
                }
            });

        }catch(Exception e)
        {
            Toast.makeText(getActivity(),"please choose a package to view details",Toast.LENGTH_LONG).show();
        }

    }

    public class BG extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... voids) {
            String method = voids[0];
            if (method.equals("loadlv")) {
                try {

                    String login_url;
                    int val=Integer.parseInt(bookedpackages.get(bpnames.indexOf(booked.getSelectedItem().toString())));
                    if(val<=100)
                    login_url = "https://androidcon.000webhostapp.com/viewnames.php";
                    else login_url = "https://androidcon.000webhostapp.com/viewnames2.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(Integer.toString(val),"UTF-8");
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

                    String aname;

                    JSONObject job = new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for (int i = 0; i < jar1.length(); i++) {
                        JSONObject jval = (JSONObject) jar1.get(i);
                        aname = jval.get("aname").toString();
                        anamelist.add(aname);
                        Log.d("anames", "doInBackground: "+aname);



                    }


                } catch (Exception ex) {
                    Log.d("Error79", ex.toString());
                }
                return method;
            }
            if (method.equals("view")) {
                try {
                    SharedPreferences sf = getActivity().getSharedPreferences("usernamefile", Context.MODE_PRIVATE);
                    String uname=sf.getString("username","NA");
                    String login_url = "https://androidcon.000webhostapp.com/view1.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("uname","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8");
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

                    String hotelcost, flightcost, totalcost,pkcost;
                    String pid;
                    JSONObject job = new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for (int i = 0; i < jar1.length(); i++) {
                        JSONObject jval = (JSONObject) jar1.get(i);
                        hotelcost = jval.get("hcost").toString();
                        flightcost = jval.get("fcost").toString();
                        totalcost = jval.get("tcost").toString();
                        pkcost= jval.getString("pcost");
                        pid = jval.get("pid").toString();
                        hotelcosts.add(hotelcost);
                        packcost.add(pkcost);
                        flightcosts.add(flightcost);
                        totalcosts.add(totalcost);
                        bookedpackages.add(pid);


                    }
                    for(int i=0;i<bookedpackages.size();i++)
                    {
                        int num=Integer.parseInt(bookedpackages.get(i));
                        String login_url1;
                        if(num<=100)
                         login_url1= "https://androidcon.000webhostapp.com/vpname.php";
                        else login_url1= "https://androidcon.000webhostapp.com/vpname2.php";
                        URL url1 = new URL(login_url1);
                        HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                        httpURLConnection1.setDoOutput(true);
                        OutputStream OS1=httpURLConnection1.getOutputStream();
                        BufferedWriter bufferedWriter1=new BufferedWriter(new OutputStreamWriter(OS1,"UTF-8"));
                        String data1= URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(bookedpackages.get(i),"UTF-8");
                        bufferedWriter1.write(data1);
                        bufferedWriter1.flush();
                        bufferedWriter1.close();
                        OS1.close();
                        InputStream inputStream1 = httpURLConnection1.getInputStream();
                        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1, "iso-8859-1"));
                        //     String response=" ";
                        String line1 = " ";
                        StringBuilder response1 = new StringBuilder();
                        while ((line = bufferedReader1.readLine()) != null) {
                            response1.append(line + '\n');
                        }

                        bufferedReader1.close();
                        inputStream1.close();
                        bpnames.add(response1.toString());
                      //  Log.d("poda", "doInBackground: "+bpnames.get(i));
                    }

                } catch (Exception ex) {
                    Log.d("Error79", ex.toString());
                }
                return method;
            }
            return method;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("view"))
            {
                bookedpackagesadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, bpnames);
                booked.setAdapter(bookedpackagesadap);
            }
            if(s.equals("loadlv"))
            {
                actadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, anamelist);
                act.setAdapter(actadap);
            }
        }
    }
}