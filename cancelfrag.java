package com.example.admin.pewds_tourism_portal_user;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class cancelfrag extends Fragment {
    Spinner Sp_Cancel;
    Button Btn_Cancel;
    ArrayList<String> alist,alist1;
    ArrayAdapter<String> adap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cancelfrag, container, false);
        Btn_Cancel=v.findViewById(R.id.btn_cancel);
        Sp_Cancel=v.findViewById(R.id.Sp_cancel);
        new BG().execute("getpks");


        Btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //      Toast.makeText(getApplicationContext(),"Button clicked",Toast.LENGTH_SHORT).show();
                    if(Sp_Cancel.getCount()>0) {
                        new BG().execute("del");

                    }
                    else
                    {
                        Toast.makeText(getContext(),"No package to be deleted",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(getContext(),"Please Wait.Content is Loading",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //B.setOnClickListener();
        return v;
    }

    public class BG extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String method=strings[0];
            SharedPreferences sf = getActivity().getSharedPreferences("usernamefile", Context.MODE_PRIVATE);
            String uname=sf.getString("username","NA");
            if(method.equals("getpks")) {
                try {

                    String login_url = "https://androidcon.000webhostapp.com/se_cancel_pnames.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    //     String response=" ";
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS1 = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter1 = new BufferedWriter(new OutputStreamWriter(OS1, "UTF-8"));
                    String data = URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(uname, "UTF-8");
                    bufferedWriter1.write(data);
                    bufferedWriter1.flush();
                    bufferedWriter1.close();
                    OS1.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String line = " ";
                    StringBuilder response = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line + '\n');
                    }
                    bufferedReader.close();
                    inputStream.close();
                    //    JOptionPane.showMessageDialog(null,response);

                    String locname, id;
                    alist = new ArrayList<String>();
                    alist1 = new ArrayList<String>();
                    JSONObject job = new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for (int i = 0; i < jar1.length(); i++) {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject) jar1.get(i);
                        locname = jval.get("pname").toString();
                        id = jval.get("pid").toString();
                        alist.add(locname);
                        alist1.add(id);
                        Log.d("val", "doInBackground: " + locname);
                    }
                } catch (Exception e) {
                    Log.d("error", "doInBackground: " + e.toString());
                }
            }

            if(method.equals("del"))
            {
                String val=alist1.get(alist.indexOf(Sp_Cancel.getSelectedItem().toString()));
                int val1=Integer.parseInt(val);
                Log.d("value", "PID: "+val);
                try{
                    if(val1<100){
                        String url2 = "https://androidcon.000webhostapp.com/se_cancel_del.php";
                        URL url=new URL(url2);
                        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                        httpURLConnection.setDoOutput(true);
                        OutputStream OS=httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                        String data= URLEncoder.encode("uname","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                                URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(val,"UTF-8");
                        bufferedWriter.write(data);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        OS.close();
                        InputStream IS=httpURLConnection.getInputStream();
                        IS.close();}




                    if(val1>100)
                    {

                        String urls1 = "https://androidcon.000webhostapp.com/se_cancel_del_cust.php";
                        URL url1=new URL(urls1);
                        HttpURLConnection httpURLConnection1=(HttpURLConnection)url1.openConnection();
                        httpURLConnection1.setDoOutput(true);
                        OutputStream OS1=httpURLConnection1.getOutputStream();
                        BufferedWriter bufferedWriter1=new BufferedWriter(new OutputStreamWriter(OS1,"UTF-8"));
                        String data1= URLEncoder.encode("uname","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                                URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(val,"UTF-8");
                        bufferedWriter1.write(data1);
                        bufferedWriter1.flush();
                        bufferedWriter1.close();
                        OS1.close();
                        InputStream IS1=httpURLConnection1.getInputStream();
                        IS1.close();

                    }

                }
                catch (Exception ex)
                {
                    Log.d("ERROR", "doInBackground: "+ex.toString());
                }
            }



            return method;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
            if(s.equals("getpks"))
            {
                adap=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_checked,alist);
                Sp_Cancel.setAdapter(adap);
            }
            if(s.equals("del"))
            {
                Toast.makeText(getContext(),"Package Deleted",Toast.LENGTH_SHORT).show();
                alist1.remove(alist.indexOf(Sp_Cancel.getSelectedItem().toString()));
                alist.remove(Sp_Cancel.getSelectedItem().toString());
                adap.notifyDataSetChanged();
            }}
            catch (Exception e)
            {
                Toast.makeText(getContext(),"Loading...",Toast.LENGTH_SHORT).show();
            }

        }
    }



}
