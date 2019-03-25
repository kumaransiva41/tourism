package com.example.admin.pewds_tourism_portal_user;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.pewds_tourism_portal_user.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import static com.paypal.android.sdk.cu.p;
import static com.paypal.android.sdk.cu.s;

public class Bill extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7171 ;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    TextView tvp,tvh,tvf,total;
    String amount="",fcost,hcost,pcost;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this,PayPalService.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        //start payment service
        Intent intent=new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);




        btnPayNow=(Button)findViewById(R.id.btnPayNow);
        tvf=findViewById(R.id.tvfcost);
        tvp=findViewById(R.id.tvpcost);
        tvh=findViewById(R.id.tvhcost);
        total=findViewById(R.id.tcost);
     try {
         SharedPreferences sf = getSharedPreferences("usernamefile", Context.MODE_PRIVATE);

         fcost = sf.getString("fcost", "NA");
         hcost = sf.getString("hcost", "NA");
         pcost = sf.getString("pcost", "NA");
         tvf.setText(fcost);
         tvh.setText(hcost);
         tvp.setText(pcost);
         int val = Integer.parseInt(fcost) + Integer.parseInt(hcost) + Integer.parseInt(pcost);
         SharedPreferences.Editor edit=sf.edit();
         edit.putString("tcost",Integer.toString(val));
         edit.commit();
         total.setText(Integer.toString(val));

     }catch (Exception e)
     {
         Toast.makeText(getApplicationContext(),"Data error. Reinstall to fix",Toast.LENGTH_SHORT).show();
     }
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();


              //  startActivity(new Intent(getApplicationContext(),UserDashboard.class));


            }
        });



    }
    private void processPayment(){
        amount=Double.toString(Double.parseDouble(total.getText().toString())/69);

        PayPalPayment payPalPayment =new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD"," Pay for trip",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                      //  String paymentDetails = confirmation.toJSONObject().toString(4);
                        Toast.makeText(getApplicationContext(),"Payment Recieved.Enjoy Your trip.",Toast.LENGTH_SHORT).show();
                      //  startActivity(new Intent(this, PaymentDetails.class).putExtra("PaymentDetails", paymentDetails)
                        //Toast.makeText(getApplicationContext(),"Payment received",Toast.LENGTH_SHORT).show();
                        BackgroundTask bg=new BackgroundTask(getApplicationContext());
                        bg.execute("write");

                        //        .putExtra("PaymentAmount", amount)
                        //);

                        startActivity(new Intent(getApplicationContext(),UserDashboard.class));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if(resultCode== Activity.RESULT_CANCELED) {
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();

            }


        }
        else if(resultCode==PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this,"invalid",Toast.LENGTH_SHORT).show();

    }



}
