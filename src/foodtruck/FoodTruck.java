package foodtruck;

public class FoodTruck 
{

	//overloaded constructor
	public FoodTruck(String name, String foodItems, String address, double lat,
			double lng) {
		super();
		this.name = name;
		this.foodItems = foodItems;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
	}

	private String name;
	private String foodItems;
	private String address;
	private double lat;
	private double lng;
	private double distanceFromUser;
	
	
	//getters and setters
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getFoodItems() 
	{
		return foodItems;
	}
	public void setFoodItems(String foodItems)
{
		this.foodItems = foodItems;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}

	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getDistanceFromUser() {
		return distanceFromUser;
	}
	public void setDistanceFromUser(double distanceFromUser) {
		this.distanceFromUser = distanceFromUser;
	}
	
	public String toString() {
		return "FoodTruck [name=" + name + ", foodItems=" + foodItems
				+ ", address=" + address + ", lat=" + lat + ", lng=" + lng
				+ "]";
	}
}
