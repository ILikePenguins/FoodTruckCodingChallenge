package com.example.foodtruckcodingchallenge;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends Activity  
{
    protected void onCreate(Bundle savedInstanceState) 
    {
       super.onCreate(savedInstanceState);
       //check if user has location access enabled
	   String locationProviders  = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	       if(locationProviders == null || locationProviders.equals("")) 
	       {
;
	           Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
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
