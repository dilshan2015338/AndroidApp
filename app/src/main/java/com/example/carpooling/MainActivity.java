package com.example.carpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public boolean Diver = false;
    public boolean Passenger = false;

    RadioButton rbDriver;
    RadioButton rbPassenger;
    String Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        rbDriver = findViewById(R.id.rbDriver);
        rbPassenger = findViewById(R.id.rbPassenger);





    }

    public void btnclickMap(View view)
    {
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

    public void btnclickLogin(View view)
    {

        Intent intent = new Intent(this,Login.class);

        if(rbDriver.isChecked()==false && rbPassenger.isChecked()==false)
        {
            Toast.makeText(this, "You must Select one", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(rbDriver.isChecked())
            {
                intent.putExtra(Name,"Driver");
                startActivity(intent);

            }
            if(rbPassenger.isChecked())
            {
                intent.putExtra(Name,"Passenger");
                startActivity(intent);
            }
        }





    }

    public void btnclickSingnUp(View view)
    {

        if(rbDriver.isChecked()==false && rbPassenger.isChecked()==false)
        {
            Toast.makeText(this, "You must Select one", Toast.LENGTH_SHORT).show();
        }

        else
        {
            if(rbDriver.isChecked())
            {
                Intent intent = new Intent(this,Driver.class);
                startActivity(intent);

            }
            if(rbPassenger.isChecked())
            {
                Intent intent = new Intent(this,Passenger.class);
                startActivity(intent);
            }
        }



    }

}
