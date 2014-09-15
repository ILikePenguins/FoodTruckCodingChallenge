package com.example.foodtruckcodingchallenge;


import user.MyLocation;
import user.MyLocation.LocationResult;

import com.google.android.gms.maps.model.LatLng;

import foodtruck.FoodTruckAdapter;
import foodtruck.FoodTruckXmlParser;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListActivity  extends Activity
{

    private FoodTruckAdapter fta;
    private ListView listView;
    private FoodTruckXmlParser truckParser;
    private LatLng userLocation;
    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.truck_list);
        
        setUpLocation();
        
        setUpListView();
    }
    
    public void setUpLocation()
    {
    	//obtain user's location
        LocationResult locationResult = new LocationResult()
        {
        	
     	    public void gotLocation(Location location)
     	    {
     	    	userLocation  = new LatLng(location.getLatitude(),location.getLongitude());
     	    }
     	};
     	MyLocation myLocation = new MyLocation();
     	myLocation.getLocation(this, locationResult);
    }
    
    public void setUpListView()
    {
    	 listView= (ListView) findViewById(R.id.truck_list);
    	 String finalUrl = "https://data.sfgov.org/api/views/rqzj-sfat/rows.xml?accessType=DOWNLOAD"; 
         truckParser = new FoodTruckXmlParser(finalUrl);
          //parse the food truck xml page
         truckParser.fetchXML();
          
          while(!truckParser.done);
          
          //if user will not allow access to their location, use san fransisco as a location
          if(userLocation!=null)
        	  truckParser.setDistances(userLocation);
          else
        	  truckParser.setDistances(new LatLng(37.7749300,-122.4194200)); //san fransisco coords
          truckParser.sort(); //sort list according to distance
          fta = new FoodTruckAdapter(truckParser.getTruckList());
          
          listView.setAdapter(fta);
          listView.setOnItemClickListener(new OnItemClickListenerListViewItem());
    }
    
    public class OnItemClickListenerListViewItem implements OnItemClickListener 
    {
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	    {
	    	//obtain values from selected food truck
	        TextView nameViewItem = ((TextView) view.findViewById(R.id.name_view));
	        TextView latViewItem = ((TextView) view.findViewById(R.id.lat_view));
	        TextView lngViewItem = ((TextView) view.findViewById(R.id.lng_view));
	        TextView addressViewItem = ((TextView) view.findViewById(R.id.address_view));
	      
	        //start map view
	        Intent mapIntent = new Intent(ListActivity.this,MapActivity.class);
	        //loads map if it is not loaded already
	        mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        //pass values to the map activity
	        mapIntent.putExtra("name", nameViewItem.getText().toString());
	        mapIntent.putExtra("lat", Double.parseDouble(latViewItem.getText().toString()));
	        mapIntent.putExtra("lng", Double.parseDouble(lngViewItem.getText().toString()));
	        mapIntent.putExtra("address", addressViewItem.getText().toString());
	        mapIntent.putExtra("userLat", userLocation.latitude);
	        mapIntent.putExtra("userLng", userLocation.longitude);
	        
	      	startActivity(mapIntent);
	    }

	}
}
