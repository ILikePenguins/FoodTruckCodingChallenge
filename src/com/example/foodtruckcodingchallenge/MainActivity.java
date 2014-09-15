package com.example.foodtruckcodingchallenge;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends Activity  
{
    protected void onCreate(Bundle savedInstanceState) 
    {
       super.onCreate(savedInstanceState);
       //check if user has location access enabled

	   LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	  if( !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
	       {
		  //bring user to location services screen
	           Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
	           //display message to user
	           Toast.makeText(getApplicationContext(), "Please enable Network or GPS location services",
	    			   Toast.LENGTH_LONG).show();
	    	   startActivityForResult(myIntent,1);
	    	   
	       }
	       else
	       {
	    	   //start list view intent
	    	   Intent listIntent = new Intent(this,ListActivity.class);
	    	   startActivity(listIntent);
	    	   finish();
	       }
    }

    //should find a way to start loading the list, while location is being asked
    //then stop loading list if it gets to userlocation part.
    protected void onActivityResult(int reqCode,int resultCode,Intent intent)
    {
            if( reqCode == 1)
            {
            	 //start list view intent
            	 Intent listIntent = new Intent(this,ListActivity.class);
        	   	 startActivity(listIntent);
        	   	 finish();
            }
    }
}
