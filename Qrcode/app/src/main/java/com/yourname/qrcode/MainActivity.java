package com.yourname.qrcode;
import com.journeyapps.barcodescanner.CaptureActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.*;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.*;
import android.util.Log;
import android.*;
import java.lang.Object;
import org.apache.http.HttpResponse;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    private Button buttonScan,rstbtn,send_btn;
    private IntentIntegrator qrScan;
    public String urladdress="your url";
    private TextView usrnm,mob,result;
    JSONObject obj;
    ProgressDialog pd;
    public MainActivity() {
        obj = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonScan = (Button) findViewById(R.id.qr_scan);
        qrScan = new IntentIntegrator(this);
        buttonScan.setOnClickListener(this);
        rstbtn=(Button) findViewById(R.id.reset);
        rstbtn.setOnClickListener(this);
        send_btn=(Button)findViewById(R.id.send_btn);
        send_btn.setOnClickListener(this);
        usrnm = (TextView)findViewById(R.id.username);
        mob = (TextView)findViewById(R.id.mobile_number);
        result = (TextView)findViewById(R.id.result1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    usrnm.setText(obj.getString("username"));
                    mob.setText(obj.getString("mobile"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public class CaptureActivityPortrait extends CaptureActivity {
//Nothing in side.
    }

    @Override
    public void onClick (View view)
    {

        switch (view.getId()){
            case R.id.qr_scan:
                qrScan.initiateScan();
                break;
            case R.id.reset:
                usrnm.setText(null);
                mob.setText(null);
                break;
            case  R.id.send_btn:
              //  Toast.makeText(MainActivity.this,"STARTING...",Toast.LENGTH_LONG).show();
                //AsyncT asyncT = new AsyncT();
                //asyncT.execute();
                sender s=new sender(MainActivity.this,urladdress,obj);
                s.execute();
                String s2=s.getresult();
                result.setText(s2);
                break;
        }
    }


}
