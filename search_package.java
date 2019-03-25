package com.example.admin.pewds_tourism_portal_user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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


public class search_package extends Fragment {
    RadioGroup rg;
    RadioButton cost, rating;
    Spinner destination;
    Spinner packages;
    ListView activities;
    String package_cost;
    View v;
    ArrayList<String> dest = new ArrayList<>();
    ArrayList<String> packid = new ArrayList<>();
    ArrayList<String> pack = new ArrayList<>();
    ArrayList<String> act = new ArrayList<>();
    ArrayList<String> rate = new ArrayList<>();
    ArrayList<String> destid=new ArrayList<>();
    ArrayList<String> cos = new ArrayList<>();
    ArrayAdapter<String> destadap, packadap, actadap;
    Button bookpack;
     String cost1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_search_package, container, false);
        rg = v.findViewById(R.id.searchpackageradiogroup);
        cost = v.findViewById(R.id.searchpackagecostRB);
        rating = v.findViewById(R.id.searchpackageratingRB);
        destination = v.findViewById(R.id.searchpackagesdestinationspinner);
        packages = v.findViewById(R.id.searchpackagepackagespinner);
        activities = v.findViewById(R.id.searchpackagesactivitieslv);
        bookpack = v.findViewById(R.id.searchpackagebookpackagebutton);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new search_package.searchpackageasync().execute("getdest");
        destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String loc = destination.getSelectedItem().toString();
                new search_package.searchpackageasync().execute("getpackages",loc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(),"nothing selected",Toast.LENGTH_SHORT).show();
            }
        });
        packages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String pac = packages.getSelectedItem().toString();
                new search_package.searchpackageasync().execute("getactivities",pac);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(),"nothing selected",Toast.LENGTH_SHORT).show();
            }
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rating.isChecked())
                {
                   String loc = destination.getSelectedItem().toString();
                    new search_package.searchpackageasync().execute("getpackagesbyrating",loc);
                }
            }
        });
        bookpack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Confirm Booking...");
                    alertDialog.setMessage("Are you sure you want to book package " + packages.getSelectedItem().toString() + "at " + destination.getSelectedItem().toString() + "?");
                    alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences sf = getActivity().getSharedPreferences("usernamefile", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit=sf.edit();
                            edit.putString("locationid",destid.get(dest.indexOf(destination.getSelectedItem().toString())));
                            edit.commit();
                            String name = sf.getString("username", "NA");
                            if (name.equals("NA") == false) {
                                new search_package.searchpackageasync().execute("getcost",packages.getSelectedItem().toString());
                              //  Toast.makeText(getActivity(), "Package Booked!", Toast.LENGTH_SHORT).show();
                                edit.putString("pid",packid.get(pack.indexOf(packages.getSelectedItem().toString())));
                                edit.commit();
                                startActivity(new Intent(getContext(),HotelBook.class));
                            }
                        }
                    });
                    alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getActivity(), "Package not booked..", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.show();
                }catch (Exception e)
                {
                    if (destination.getSelectedItem() == null || packages.getSelectedItem() == null) {
                        Toast.makeText(getActivity(), "please select a package", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private class searchpackageasync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getActivity(), "Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String method = strings[0];
            if (method.equals("getdest")) {
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

                    String locname;
                    String lid;
                    JSONObject job = new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for (int i = 0; i < jar1.length(); i++) {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject) jar1.get(i);
                        locname = jval.get("lname").toString();
                        lid=jval.get("lid").toString();
                        dest.add(locname);
                        destid.add(lid);

                    }

                } catch (Exception ex) {
                    Log.d("Error", ex.toString());
                }
                return method;
            }

            else if(method.equals("getpackages"))
            {
                try {
                   // pack.clear();

                    String login_url = "https://androidcon.000webhostapp.com/se_packnames.php";
                    String lname=strings[1];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("lname","UTF-8")+"="+URLEncoder.encode(lname,"UTF-8");
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

                    String packname,pid;
                    pack=new ArrayList<String>();
                    packid=new ArrayList<String>();
                    JSONObject job =new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for(int i=0;i<jar1.length();i++)
                    {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject)jar1.get(i);
                        packname=jval.get("pname").toString();
                        pid=jval.get("pid").toString();
                        packid.add(pid);
                        pack.add(packname);
                        }

                }catch (Exception ex)
                {
                    Log.d("Error",ex.toString());
                }
                return method;
            }
            else if (method.equals("getactivities"))
            {
                try {
                    // pack.clear();

                    String login_url = "https://androidcon.000webhostapp.com/segetactfrompack.php";
                    String pname=strings[1];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("pname","UTF-8")+"="+URLEncoder.encode(pname,"UTF-8");
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

                    String actname;
                    act=new ArrayList<String>();
                    JSONObject job =new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for(int i=0;i<jar1.length();i++)
                    {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject)jar1.get(i);
                        actname=jval.get("aname").toString();
                        Log.d("Error",actname);
                        act.add(actname);
                    }

                }catch (Exception ex)
                {
                    Log.d("Error",ex.toString());
                }
                return method;
            }
            else if (method.equals("getpackagesbyrating"))
            {
                try {
                    // pack.clear();

                    String login_url = "https://androidcon.000webhostapp.com/se_packnames_sortrate.php";
                    String lname=strings[1];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("lname","UTF-8")+"="+URLEncoder.encode(lname,"UTF-8");
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

                    String packname;
                    String rat;
                    pack=new ArrayList<String>();
                    rate=new ArrayList<String>();
                    JSONObject job =new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for(int i=0;i<jar1.length();i++)
                    {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject)jar1.get(i);
                        packname=jval.get("pname").toString();
                        rat=jval.get("rating").toString();
                        pack.add(packname);
                        rate.add(rat);
                    }

                }catch (Exception ex)
                {
                    Log.d("Error",ex.toString());
                }
                return method;
            }
            else if (method.equals("getcost"))
            {
                try{
                String login_url = "https://androidcon.000webhostapp.com/segetcostofpack.php";
                String pname=strings[1];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("pname","UTF-8")+"="+URLEncoder.encode(pname,"UTF-8");
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
                //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_SHORT).show();
                    cost1=response.toString();
                    Log.d("COST",cost1);

                bufferedReader.close();
                inputStream.close();

                //    JOptionPane.showMessageDialog(null,response);

//                String actname;
//                act=new ArrayList<String>();
//                JSONObject job =new JSONObject(response.toString());
//                JSONArray jar1 = (JSONArray) job.get("values");
//                for(int i=0;i<jar1.length();i++)
//                {
////Store the JSON objects in an array
////Get the index of the JSON object and print the values as per the index
//                    JSONObject jval = (JSONObject)jar1.get(i);
//                    actname=jval.get("aname").toString();
//                    Log.d("Error",actname);
//                    act.add(actname);
                //}

            }catch (Exception ex)
            {
                Log.d("Error",ex.toString());
            }
            return method;
            }
            return method;
        }

            @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s.equals("getdest")) {
                    destadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, dest);
                    destination.setAdapter(destadap);
                } else if (s.equals("getpackages")) {
                    packadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, pack);
                    packages.setAdapter(packadap);
                } else if (s.equals("getactivities")) {
                    actadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, act);
                    activities.setAdapter(actadap);
                } else if (s.equals("getpackagesbyrating")) {
                    packadap = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, pack);
                    packages.setAdapter(packadap);
                }
                else if(s.equals("getcost"))
                {
                    SharedPreferences sf = getActivity().getSharedPreferences("usernamefile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=sf.edit();
                    edit.putString("pcost",cost1.trim());
                    edit.commit();
                }
            }catch (Exception e)
            {
                Toast.makeText(getContext(),"Loading...",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
