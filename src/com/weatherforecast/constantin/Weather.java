package com.weatherforecast.constantin;

import java.util.ArrayList;

public class Weather {
	String icon;
	int temperature;
	int temperatureCelsius;
	String condition;
	String windCondition;
	String humidity;
	Location location;
    ArrayList<Forecast> forecasts;
    
	public Weather (){
		icon = "";
		temperature = 0;
		temperatureCelsius = 0;
		condition = "";
		windCondition = "";
		humidity = "";
		location = new Location();
		forecasts = null;
	}
	
	public Weather (String icon, int temperature, int temperatureCelsius, String condition, String windCondition, String humidity,
			Location location, ArrayList<Forecast> forecasts){
		this.icon = icon;
		this.temperature = temperature;
		this.temperatureCelsius = temperatureCelsius;
		this.condition = condition;
		this.windCondition = windCondition;
		this.humidity = humidity;
		this.location = location;
		this.forecasts = forecasts;
	}
	
	public String getIcon(){
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public int getTemperature(){
		return temperature;
	}
	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	public int getTemperatureCelsius(){
		return temperatureCelsius;
	}
	
	public void getTemperatureCelsius(int temperatureCelsius) {
		this.temperatureCelsius = temperatureCelsius;
	}
	
	public String getCondition(){
		return condition;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public String getWindCondition(){
		return windCondition;
	}
	
	public void setWindCondition(String windCondition) {
		this.windCondition = windCondition;
	}
	
	public String getHumidity(){
		return humidity;
	}
	
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}	
	
    public ArrayList<Forecast> getForecasts() {
        return forecasts;
    }
    public void setForecasts(ArrayList<Forecast> forecasts) {
        this.forecasts = forecasts;
    } 
}
