package foodtruck;

import java.util.ArrayList;

import com.example.foodtruckcodingchallenge.R;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FoodTruckAdapter extends BaseAdapter{

		 private ArrayList<FoodTruck> truckList = new ArrayList<FoodTruck>();
	     
	     public FoodTruckAdapter(ArrayList<FoodTruck> truckList ) 
	     {
	    	 this.truckList=truckList;
	     }

	     public int getCount() {
	             return truckList.size();
	     }

	     public Object getItem(int index) {
	             return truckList.get(index);
	     }

	     public long getItemId(int position) {
	             return position;
	     }

	     public View getView(int position, View convertView, ViewGroup parent) 
	     {
	             if (convertView == null) 
	             {
	                     LayoutInflater inflater = LayoutInflater.from(parent.getContext());
	                     convertView = inflater.inflate(R.layout.list_item, parent, false);
	             }
	             
	             //get foodtruck item and display its information in listview
	             FoodTruck truck = (FoodTruck) getItem(position);
	             //truck name
	             TextView nameTextView = (TextView) convertView.findViewById(R.id.name_view);
	             nameTextView.setText(truck.getName());
	             //food items
	             TextView foodTextView = (TextView) convertView.findViewById(R.id.food_view);
	             foodTextView.setText(truck.getFoodItems());
	           //distance
	             TextView distanceTextView = (TextView) convertView.findViewById(R.id.distance_view);
	             distanceTextView.setText(truck.getDistanceFromUser()+"km away\n");
	             //latitude
	             TextView latTextView = (TextView) convertView.findViewById(R.id.lat_view);
	             latTextView.setText(truck.getLat()+"");
	             //longtitude
	             TextView lngTextView = (TextView) convertView.findViewById(R.id.lng_view);
	             lngTextView.setText(truck.getLng()+"");
	             //address
	             TextView addressTextView = (TextView) convertView.findViewById(R.id.address_view);
	             addressTextView.setText("Address: "+truck.getAddress());
	             
	             
	             return convertView;
	     }
	     

	}
