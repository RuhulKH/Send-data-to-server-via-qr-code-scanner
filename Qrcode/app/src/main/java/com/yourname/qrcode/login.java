package com.yourname.qrcode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class login extends AppCompatActivity implements View.OnClickListener {
    String usr,pass;
    Button lgnbtn;
    EditText user,pwd;
    String urladdress="your url";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        lgnbtn=findViewById(R.id.loginbtn);
        lgnbtn.setOnClickListener(this);

        user=findViewById(R.id.usrname);
        pwd=findViewById(R.id.password);


    }
    @Override
    public void onClick(View view) {
        JSONObject data= new JSONObject();
        usr=user.getText().toString();
        pass=pwd.getText().toString();
        try {
            data.accumulate("usr",usr);
            data.accumulate("pass",pass);
            sendlogin s= (sendlogin) new sendlogin(this,urladdress,data).execute();
            String s1=s.get();
            //String res=s.doInBackground();
            //Toast.makeText(this,s1,Toast.LENGTH_LONG).show();
            if(s1.contentEquals("success"))
            {
                setContentView(R.layout.activity_main);
            }
            //else
                //setContentView(R.layout.activity_main);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //Toast.makeText(this,pass,Toast.LENGTH_LONG).show();
    }

}
