package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;


public class NextActivity extends AppCompatActivity {

    private TextView tPhone,tAddress;
    private ImageButton btnDial,btnMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);

        initiText();
        setButton();
    }

    public void returnMainActivity(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initiText(){
        tPhone=(TextView)findViewById(R.id.textPhone);
        tAddress=(TextView)findViewById(R.id.textAddress);
    }
    private void setButton(){
        btnDial=(ImageButton)findViewById(R.id.btnDial);
        btnMap=(ImageButton) findViewById(R.id.btnMap);


        btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialNumber(tPhone.getText().toString());
            }
        });
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(tAddress.getText().toString());
            }
        });
    }

    /**
     * open google map application
     * @param uri
     * @return if open successfully return true, otherwise return false
     */
    private boolean openActivity(String uri){
        try {
            String rout="geo:0,0?q=5+milroy+ave+Kingsinton";
            Uri intentUri = Uri.parse(rout);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, intentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if(mapIntent.resolveActivity(getPackageManager())!=null)
                startActivity(mapIntent);
            return true;
        }
        catch(Exception e){

        }
        return false;
    }

    /**
     * dial phone number of professor.
     * @param phoneNumber the phone number of a professor
     * @return return true if dial successfully, otherwise return false
     */
    private boolean dialNumber(String phoneNumber){
        Intent intentDial=new Intent(Intent.ACTION_DIAL);
        intentDial.setData(Uri.parse("tel:"+phoneNumber));
        if(intentDial.resolveActivity(getPackageManager())!=null){
            startActivity(intentDial);
            return true;
        }
        return false;
    }
}
