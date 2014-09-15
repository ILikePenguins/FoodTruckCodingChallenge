package com.example.foodtruckcodingchallenge;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import directions.Directions;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

//class that shows google maps
public class MapActivity extends FragmentActivity  
{
	private GoogleMap map; 
	private String mode;
	private LatLng userLocation;
	private LatLng truckCoords;
   protected void onCreate(Bundle savedInstanceState) 
    {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       mode="";
       setUpSpinner();
       
       setUpMapIfNeeded();
       markTruck();
    }
   
public void setUpSpinner()
{
	  Spinner spinner = (Spinner) findViewById(R.id.maps_spinner);
	  //the blank is for unselected state
       String[] items = new String[]{"","driving", "transit", "cycling","walking"};
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
       // Specify the layout to use when the list of choices appears
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    // Apply the adapter to the spinner
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(new ItemSelectedListener());
}
   
public class ItemSelectedListener implements OnItemSelectedListener 
{
	 Directions directions;
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id)
	{
	//gets the drop down item and gives directions based on chosen method
	    switch (position) 
	    {
	        case 1:
	        	if(!mode.equals("driving"))
	        	{
	        		mode="driving";
	        	}
	            break;
	        case 2:
	        	if(!mode.equals("transit"))
	        	{
	        		mode="transit";
	        	}
	            break;
	        case 3:
	        	if(!mode.equals("cycling"))
	        	{
	        		mode="cycling";
	        	}
	            break;
	        case 4:
	        	if(!mode.equals("walking"))
	        	{
	        		mode="walking";
	        	}
	            break;
	    }
	    //only get directions if a case is selected
	    if(position!=0)
	    	directions = new Directions(MapActivity.this,userLocation,truckCoords,mode);

	}

	public void onNothingSelected(AdapterView<?> parent) 
	{
		
	}
}

   protected void onResume() 
    {
        super.onResume();
    }

	public void markTruck()
	{
		//obtain the information on the selected food truck
		String name = getIntent().getExtras().getString("name");
		String address = getIntent().getExtras().getString("address");
		double lat= getIntent().getExtras().getDouble("lat");
		double lng= getIntent().getExtras().getDouble("lng");
		truckCoords = new LatLng(lat,lng);
		
		double userLat =getIntent().getExtras().getDouble("userLat");
		double userLng =getIntent().getExtras().getDouble("userLng");
		userLocation = new LatLng(userLat,userLng);
		
		//make marker for the food truck using truck icon
		MarkerOptions marker=new MarkerOptions()
		 .position(truckCoords)
		 .title(name +address);
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.truck));
		 map.addMarker(marker);
		 
		 map.addMarker(new MarkerOptions().position(userLocation).title("My Location"));
		 //zoom in on the marker
		     map.animateCamera(CameraUpdateFactory.newLatLngZoom(
		     		truckCoords,
		     		(float) 15.0)
		             );
		     						
	}

    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void setUpMapIfNeeded()
    {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
        	
        	int checkGooglePlayServices =    GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
            if (checkGooglePlayServices != ConnectionResult.SUCCESS) 
            {
            // google play services is missing!!!!
            	GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices, this, checkGooglePlayServices);
            }
            else
            // Try to obtain the map from the SupportMapFragment.
            	map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        	
          
        }
    }
    
		public GoogleMap getMap() {
			return map;
		}
		public void setMap(GoogleMap map) {
			this.map = map;
		}
}
