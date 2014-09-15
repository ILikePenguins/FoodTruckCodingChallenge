package directions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;

import com.example.foodtruckcodingchallenge.MapActivity;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Directions 
{
	private MapActivity activity;
	public Directions(MapActivity a, LatLng userCoords, LatLng truckCoords, String mode )
	{
		activity=a;
		new UrlParser(makeURL(userCoords.latitude, userCoords.longitude, truckCoords.latitude, truckCoords.longitude,mode), true)
		.execute();
	}
	private void drawPath(String result, boolean withSteps) 
	{

		try 
		{
			// transform the string into a json object
			final JSONObject json = new JSONObject(result);
			JSONArray routeArray = json.getJSONArray("routes");
			JSONObject routes = routeArray.getJSONObject(0);
			JSONObject overviewPolylines = routes
					.getJSONObject("overview_polyline");
			
			String encodedString = overviewPolylines.getString("points");
			List<LatLng> list = decodePoly(encodedString);
			
			for (int z = 0; z < list.size() - 1; z++)
			{
				LatLng src = list.get(z);
				LatLng dest = list.get(z + 1);
				//draw the blue lines on the map
				Polyline line = activity.getMap().addPolyline(new PolylineOptions()
						.add(new LatLng(src.latitude, src.longitude),
								new LatLng(dest.latitude, dest.longitude))
						.width(10).color(Color.BLUE).geodesic(true));
				
			}

			if (withSteps) //option to do steps or not, they're always on for now
			{
				JSONArray arrayLegs = routes.getJSONArray("legs");
				JSONObject legs = arrayLegs.getJSONObject(0);
				JSONArray stepsArray = legs.getJSONArray("steps");
				// put initial point

				for (int i = 0; i < stepsArray.length(); i++)
				{
					//get the directions with steps
					//put marker at each waypoint
					Step step = new Step(stepsArray.getJSONObject(i));
					activity.getMap().addMarker(new MarkerOptions()
							.position(step.location)
							.title(step.distance)
							.snippet(step.instructions)
							.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
				}
			}

		} catch (JSONException e) {}
	}

	private List<LatLng> decodePoly(String encoded) 
	{
		//get all the points for drawing the lines
		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) 
		{
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	private class Step 
	{
		public String distance;
		public LatLng location;
		public String instructions;

		Step(JSONObject stepJSON)
		{
			JSONObject startLocation;
			try 
			{
				//retrieve direction steps from xml file
				distance = stepJSON.getJSONObject("distance").getString("text");
				startLocation = stepJSON.getJSONObject("start_location");
				location = new LatLng(startLocation.getDouble("lat"),
						startLocation.getDouble("lng"));
				try {
					instructions = URLDecoder.decode(
							Html.fromHtml(
									stepJSON.getString("html_instructions"))
									.toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				;

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private class UrlParser extends AsyncTask<Void, Void, String> 
	{
		private ProgressDialog progressDialog;
		String url;
		boolean steps;

		UrlParser(String urlPass, boolean withSteps) 
		{
			url = urlPass;
			steps = withSteps;
		}

		protected void onPreExecute() 
		{
			//show the user that the data is loading
			super.onPreExecute();
			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage("Fetching route, Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		protected String doInBackground(Void... params)
		{
			//start parsing
			JSONParser jParser = new JSONParser();
			String json = jParser.getJSONFromUrl(url);
			return json;
		}

		protected void onPostExecute(String result) 
		{
			//directions are retrieved, draw the path
			super.onPostExecute(result);
			progressDialog.hide();
			if (result != null) {
				drawPath(result, steps);
			}
		}
	}
	public String makeURL(double sourcelat, double sourcelog, double destlat,
			double destlog, String mode)
	{ 
		//build the url to retreive directions from google
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.googleapis.com/maps/api/directions/json");
		urlString.append("?origin=");
		urlString.append(Double.toString(sourcelat));
		urlString.append(",");
		urlString.append(Double.toString(sourcelog));
		urlString.append("&destination=");
		urlString.append(Double.toString(destlat));
		urlString.append(",");
		urlString.append(Double.toString(destlog));
		urlString.append("&sensor=false&mode="+mode+"&alternatives=true");

		return urlString.toString();
	}

}
