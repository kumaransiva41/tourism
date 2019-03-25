package com.example.admin.pewds_tourism_portal_user;;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.pewds_tourism_portal_user.UserDashboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String,Void,String> {
    Context ctx;
    AlertDialog alertDialog;
    BackgroundTask(Context ctx)
    {
        this.ctx=ctx;
    }
    int count=0;
    @Override
    protected String doInBackground(String... params) {
        String reg_url="https://androidcon.000webhostapp.com/seuser_reg.php";
        String login_url="https://androidcon.000webhostapp.com/seuser_login.php";
        String method=params[0];
        if(method.equals("register"))
        {
            String uname=params[1];
            String pass=params[2];
            String mail=params[3];
            try {
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream OS=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(mail,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(IS,"iso-8859-1"));
                String response=" ";
                String line=" ";
                while((line=bufferedReader.readLine())!=null)
                {
                    response+=line;
                }
                bufferedReader.close();
                IS.close();
                if (response.trim().equals("yes"))
                return "Registration succes";
                else
                    return "Invalid";
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        if(method.equals("login"))
        {
            String uname=params[1];
            String pass=params[2];
            try {
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data=URLEncoder.encode("uid","UTF-8")+"="+URLEncoder.encode(uname,"UTF-8")+"&"+
                        URLEncoder.encode("upass","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response=" ";
                String line=" ";
                while((line=bufferedReader.readLine())!=null)
                {
                    response+=line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(method.equals("write"))
        {
            try {

                SharedPreferences sf = ctx.getSharedPreferences("usernamefile", Context.MODE_PRIVATE);
                String pid,fid,days,hid,fcost,hcost,pcost,uname,tcost;
                uname=sf.getString("username","NA");
                fcost = sf.getString("fcost", "NA");
                hcost = sf.getString("hcost", "NA");
                pcost = sf.getString("pcost", "NA");
                tcost = sf.getString("tcost", "NA");
                pid=sf.getString("pid","NA");
                fid=sf.getString("fid","NA");
                hid=sf.getString("hid","NA");
                days=sf.getString("days","NA");
                String entry_url = "https://androidcon.000webhostapp.com/se_user_presubmit.php";
                URL url1 = new URL(entry_url);
                HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                httpURLConnection1.setRequestMethod("POST");
                httpURLConnection1.setDoOutput(true);
                OutputStream OS = httpURLConnection1.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(pid, "UTF-8") + "&" +
                        URLEncoder.encode("hid", "UTF-8") + "=" + URLEncoder.encode(hid, "UTF-8") + "&" +
                        URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(uname, "UTF-8") + "&" +
                        URLEncoder.encode("fid", "UTF-8") + "=" + URLEncoder.encode(fid, "UTF-8") + "&" +
                        URLEncoder.encode("hcost", "UTF-8") + "=" + URLEncoder.encode(hcost, "UTF-8") + "&" +
                        URLEncoder.encode("fcost", "UTF-8") + "=" + URLEncoder.encode(fcost, "UTF-8") + "&" +
                        URLEncoder.encode("pcost", "UTF-8") + "=" + URLEncoder.encode(pcost, "UTF-8") + "&" +
                        URLEncoder.encode("days", "UTF-8") + "=" + URLEncoder.encode(days, "UTF-8") + "&" +
                        URLEncoder.encode("tcost", "UTF-8") + "=" + URLEncoder.encode(tcost, "UTF-8");
                ;
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection1.getInputStream();
                IS.close();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return method;
        }
        return method;
    }

    @Override
    protected void onPreExecute() {
        alertDialog=new AlertDialog.Builder(ctx).setTitle("login info").create();
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Registration succes"))
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        else if (result.equals("Invalid"))
        {
            Toast.makeText(ctx,"Username or email already in use",Toast.LENGTH_SHORT).show();

        }
        else
        {
            if(count>3)
            {
                Toast.makeText(ctx,"max attempts exceeded",Toast.LENGTH_SHORT).show();
            }
            else {
                if (result.trim().equals("yes"))
                {
                    count=0;
                    ctx.startActivity(new Intent(ctx, UserDashboard.class));
                }

                else {
                    Toast.makeText(ctx, "Username or Password is Incorrect", Toast.LENGTH_SHORT).show();
                    count++;
                    Log.d("count",""+count);
                }
            }
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
