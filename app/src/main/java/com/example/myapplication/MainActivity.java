package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//MAIN
public class MainActivity extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private String url_server="";
   ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.ProfList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
                startNewActivity(position);
            }
        });
    }


    public void startNewActivity(int position)
    {
        Intent intent = new Intent(this, NextActivity.class);
        Bundle b=new Bundle();
        b.putInt("key",position);
        intent.putExtras(b);
        startActivity(intent);
    }

    private boolean getLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location=null;
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 11);
                }
                location=null;
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    return true;
                }
                else{
                    location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        return true;
                    }
                }
            }
        }
        catch(Exception ex){

            Toast toast=Toast.makeText(this,"error!",Toast.LENGTH_SHORT);
        }
        return false;
    }

    /**
     * post location and require list number to server
     * @param lat current location of latitude
     * @param lon current location of longitude
     * @param numresult the number of list
     * @return the information of all professors
     */
    private String post(double lat,double lon,int numresult){
        String data="lat:"+lat+",lon:"+lon+",numResults:"+numresult;
        HttpURLConnection connection=null;
        try {
            URL url = new URL(url_server);
            connection=(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Key","Value");
            connection.setDoOutput(true);
            //post message to server
            OutputStream out=connection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();
            //get response from server
            int response=connection.getResponseCode();
            if(response==200){
                InputStream input=connection.getInputStream();
                String results=getStringFormInputStream(input);
                return results;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(connection!=null){
                connection.disconnect();//close the connection
            }
        }
        return null;
    }

    /**
     * get String from inputStream
     * @param inputStream
     * @return return the String
     * @throws IOException
     */
    private String getStringFormInputStream(InputStream inputStream)throws IOException{
        //storage the data with the typ of stream
        ByteArrayOutputStream outputStreams=new ByteArrayOutputStream();
        //get stream from inputStream
        byte[] buffer=new byte[1024];
        int l=-1;
        while((l=inputStream.read(buffer))!=-1){
            outputStreams.write(buffer,0,l);
        }
        inputStream.close();
        String results=outputStreams.toString();
        outputStreams.close();

        return results;
    }
}
