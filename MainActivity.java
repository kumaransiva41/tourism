package com.example.admin.pewds_tourism_portal_user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    Button register;
    TextView forgotpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.UserLoginUsernameET);
        password = findViewById(R.id.UserLoginPasswordET);
        login = findViewById(R.id.UserLoginLoginButton);
        register=findViewById(R.id.UserLoginRegisterButton);
        forgotpass=findViewById(R.id.UserLoginForgotPasstv);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().isEmpty()==false)
                {
                    if(password.getText().toString().trim().isEmpty()==false)
                    {
                        SharedPreferences sf=getSharedPreferences("usernamefile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sf.edit();
                        edit.putString("username",username.getText().toString());
                        edit.commit();
                        String method="login";
                        BackgroundTask backgroundTask=new BackgroundTask(MainActivity.this);
                      backgroundTask.execute(method,username.getText().toString(),password.getText().toString());

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"please enter a password",Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this,"please enter a username",Toast.LENGTH_SHORT).show();
                }

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotpass.setText("to be implemented soon!!");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,RegistraionUser.class);
                startActivity(i);
            }
        });
    }
    private class loginasynctask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
