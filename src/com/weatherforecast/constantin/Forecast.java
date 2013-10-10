package com.weatherforecast.constantin;

public class Forecast {
	String dayOfWeek = "";
	int high;
	int low;
	Weather weather = null;
	
	public Forecast (){
		this.dayOfWeek = "";
		this.high = 0;
		this.low = 0;
		this.weather = new Weather();
	}
	
	public Forecast (String dayOfWeek, int high, int low, Weather weather){
		this.dayOfWeek = dayOfWeek;
		this.high = high;
		this.low = low;
		this.weather = weather;
	}
	
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	public String getDOfWeek(){
		return dayOfWeek;
	}
	
	public void setHigh(int high) {
		this.high = high;
	}
	
	public int getHigh(){
		return high;
	}
	
	public void setLow(int low) {
		this.low = low;
	}
	
	public int getLow(){
		return low;
	}
	
	public Weather getWeather(){
		return weather;
	}
	
	public void setWeather(Weather weather) {
		this.weather = weather;
	}
}
