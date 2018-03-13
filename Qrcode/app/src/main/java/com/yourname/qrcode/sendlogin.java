package com.yourname.qrcode;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.text.style.UpdateLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.StringBuffer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public class sendlogin extends AsyncTask<Void,Void,String> {
    Context cx;
    String urlAddress;
    ProgressDialog pd;
    NotificationCompat.MessagingStyle.Message msg;
    JSONObject userdata;

    public String result="temp",temp="";

    JSONObject res;
    private AlertDialog msgbox(String str)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(cx);
        builder.setMessage(str);
        builder.setCancelable(false);
        builder.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }
        );
        AlertDialog alertDialog= builder.create();
        return  alertDialog;
    }
    public sendlogin(Context c, String urlAddress, JSONObject jo)
    {
        this.cx=c;
        this.urlAddress=urlAddress;
        this.userdata = jo;
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        pd = new ProgressDialog(cx);
        pd.setTitle("Send");
        pd.setMessage("Sending data to server ...please wait");
        pd.show();
    }


    private String send() {
        //CONNECT
        HttpURLConnection con =Connector.connect(urlAddress);

        if (con == null) {
            return null;
        }


        try {
            OutputStream os = con.getOutputStream();

            //WRITE
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(new DataPackager(userdata).packData());

            bw.flush();

            //RELEASE RES
            bw.close();
            os.close();

            //HAS IT BEEN SUCCESSFUL?
            int responseCode = con.getResponseCode();

            if (responseCode == con.HTTP_OK) {
                //GET EXACT RESPONSE
                InputStream is= (InputStream) con.getContent();
                //InputStream
                //BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(is ,"UTF-8"),8);
                StringBuffer response = new StringBuffer();
                StringBuilder sb= new StringBuilder();
                String line="";

                //READ LINE BY LINE
                while ((line = br.readLine()) != null) {
                    //response.append(line);
                    sb.append(line);
                }

                //RELEASE RES
                br.close();
                result = sb.toString();
                //result = response.toString();
                res = new JSONObject(result);
                result=res.getString("result");
                return result;

            } else {
                msgbox("Internet Connection error.\nTry Again");
                return null;

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public String doInBackground(Void... voids) {
        this.send();
        //Toast.makeText(cx,"Hello",Toast.LENGTH_LONG).show();
        return result;
    }

    @Override
    protected void onPostExecute(String response)
    {
        super.onPostExecute(response);
        pd.dismiss();
        //pd = new;
        temp=result;
        //this.temp(result);
        //Toast.makeText(cx,result,Toast.LENGTH_LONG).show();
        if(result.contentEquals("success")) {
            //pd.setTitle("SUCCESS");
            //pd.show();
           // msgbox(result+"\nYou have logged in").show();
        }
        else if(result.contentEquals("failed")){
            //pd.setTitle("Failed");
            //pd.show();
            //msgbox("You are already Registered").show();

        }
        else{
            //msgbox("Internet Connection error.\nTry Again");
        }
    }
    public String getresult()
    {
        return this.temp;
    }


}
