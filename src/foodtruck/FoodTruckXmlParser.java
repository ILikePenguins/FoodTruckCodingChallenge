package foodtruck;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.android.gms.maps.model.LatLng;

public class FoodTruckXmlParser 
{
	   private String urlString = null;
	   public volatile boolean parsingComplete = true;
	   
	   private ArrayList<FoodTruck> truckList = new ArrayList<FoodTruck>();
	   private  String name;
	   private String foodItems;
	   private String address;
	   private double lat;
	   private double lng;
	   public boolean done;
	   
	   public FoodTruckXmlParser(String url)
	   {
	      this.urlString = url;
	      done=false;
	   }


	  public void parse(InputStream stream)
	  {
		  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder;
		try 
		{
			builder = factory.newDocumentBuilder();
		
          Document doc = builder.parse(stream);
          NodeList nodes = doc.getElementsByTagName("row");
           
          for (int i = 0; i < nodes.getLength(); i++)
          {
             Element element = (Element) nodes.item(i);
             
            name= pull("applicant",element);
            foodItems= pull("fooditems",element);
            address= pull("address",element);
            lat=Double.parseDouble(pull("latitude",element));
            lng= Double.parseDouble(pull("longitude",element));
            
            //filter out trucks without coordinates
            if(lat!=0 &&lng!=0)
            	truckList.add(new FoodTruck(name,foodItems,address,lat,lng));
            
          }
          done=true;
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		} catch (SAXException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	  }
	  
	   public void fetchXML()
	   {
	      Thread thread = new Thread(new Runnable()
	      {
	         public void run() 
	         {
	            try 
	            {
	            	//open connection with the url
	               URL url = new URL(urlString);
	               HttpURLConnection conn = (HttpURLConnection) 
	               url.openConnection();
	                  conn.setReadTimeout(10000 /* milliseconds */);
	                  conn.setConnectTimeout(15000 /* milliseconds */);
	                  conn.setRequestMethod("GET");
	                  conn.setDoInput(true);
	                  conn.connect();
		            InputStream stream = conn.getInputStream();
		            
		            parse(stream);
		            
		              
		            stream.close();
	            } 
	            catch (Exception e) 
	            {
	               e.printStackTrace();
	            }
	        }
	    });

	    thread.start(); 


	   }

		public String pull(String s, Element element)
		{
			NodeList title = element.getElementsByTagName(s);
			
			if(title.item(0)!=null)
			{
				Element line = (Element) title.item(0);
				//ensure the string is a double for lat and lng
				if( (s.equals("latitude")||s.equals("longitude")) && !line.getTextContent().matches("-?\\d+(\\.\\d+)?"))
				{
					//System.out.println("lat or lng: "+line.getTextContent());
					return "0";
				}
				return line.getTextContent();
			}
			else
			{
				if(s.equals("latitude")||s.equals("longitude"))
				{
					return "0";
				}
				return "unavailable";
			}
			
		}
		
		public void setDistances(LatLng coords)
		{
			   double earthRadius  =  6378.1;  //  radius of the Earth (km)
			   
			   for(FoodTruck list : truckList)
			   {
				   double dlat = Math.toRadians( list.getLat()-coords.latitude ); // Latitude difference in radians
				   double dlang = Math.toRadians ( list.getLng() - coords.longitude ) ;//longitude difference in radian
				   double distance = earthRadius * Math.sqrt( (dlat *dlat) + (dlang*dlang)  );
				   
				   //round distance to 2 decimal places
				   distance = Math.round(distance*100)/100D;
				   list.setDistanceFromUser(distance);
			   }
		}

		
		public void sort()
		{
			//sort list according to distance from user (closest appear first)
			Collections.sort(truckList, new CustomComparator());
		}

		public class CustomComparator implements Comparator<FoodTruck> {

			public int compare(FoodTruck lhs, FoodTruck rhs) 
			{
				return  Double.compare(lhs.getDistanceFromUser(), rhs.getDistanceFromUser());
			}
		}

	public ArrayList<FoodTruck> getTruckList() {
		return truckList;
	}


	public void setTruckList(ArrayList<FoodTruck> truckList) {
		this.truckList = truckList;
	}

}
