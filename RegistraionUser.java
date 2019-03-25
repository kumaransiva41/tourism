package com.example.admin.pewds_tourism_portal_user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistraionUser extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText email;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registraion_user);
        username=findViewById(R.id.UserRegUsernameET);
        password=findViewById(R.id.UserRegPasswordET);
        email=findViewById(R.id.UserRegEmailET);
        register=findViewById(R.id.UserRegRegisterButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().isEmpty())
                {
                    Toast.makeText(RegistraionUser.this,"enter username!!",Toast.LENGTH_SHORT).show();
                }
                else if(password.getText().toString().isEmpty()||password.getText().toString().length()<6)
                {
                    Toast.makeText(RegistraionUser.this,"enter valid password(length>6)!!",Toast.LENGTH_SHORT).show();
                }
                else if(email.getText().toString().contains("@")==false||email.getText().toString().contains(".com")==false)
                {
                    Toast.makeText(RegistraionUser.this,"enter valid email!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String method="register";
                    BackgroundTask bg=new BackgroundTask(RegistraionUser.this);
                    bg.execute(method,username.getText().toString(),password.getText().toString(),email.getText().toString());
                    startActivity(new Intent(RegistraionUser.this, MainActivity.class));
                }
            }
        });
    }
}
