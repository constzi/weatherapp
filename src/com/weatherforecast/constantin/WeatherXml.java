package com.weatherforecast.constantin;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WeatherXml extends DefaultHandler {
	
	// fyi: http://stackoverflow.com/questions/5540998/how-does-this-java-program-run
	// SAX is event-based. So, each time it sees a start tag, attribute, characters within a tag, end tag, and so on from up to down... 
	private Weather weather = new Weather();
	private Forecast forecast = null;
	ArrayList<Forecast> forecasts = new ArrayList<Forecast>();
	boolean forecastCondition = false;
	int i = 0;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException { 		
		if (localName.equals("temp_f")){  								 		 // local names = keys of Xml tags & attributes = values/data
			weather.temperature = Integer.parseInt(attributes.getValue("data")); // data comes from Xml see below where city name has data value	
		}else if (localName.equals("temp_c")){ 
			weather.temperatureCelsius = Integer.parseInt(attributes.getValue("data"));
		}else if (localName.equals("wind_condition")){			
			weather.windCondition = attributes.getValue("data"); 	
		}else if (localName.equals("humidity")){ 			
			weather.humidity = attributes.getValue("data"); 				
		}else if (localName.equals("forecast_conditions")){ 					// 1/2 on FIRST condition - open new class!
			forecast = new Forecast(); //weather.location.area += "\n" +  attributes.getValue("data") + String.valueOf(i); // TEST CODE
			forecastCondition = true;
		}else if (localName.equals("day_of_week")){
			forecast.dayOfWeek = attributes.getValue("data"); 		
		}else if (localName.equals("high")){
			forecast.high = Integer.parseInt(attributes.getValue("data"));
		}else if (localName.equals("low")){
			forecast.low = Integer.parseInt(attributes.getValue("data"));
		}else if (localName.equals("condition")){ 	
			if (!forecastCondition)
				weather.condition = attributes.getValue("data");				// condition under current_conditions	
			else
				forecast.weather.condition = attributes.getValue("data");  		// condition under forecast_conditions		
		} else if (localName.equals("icon")){	
			if (!forecastCondition)			
				weather.icon = attributes.getValue("data");
			else
				forecast.weather.icon = attributes.getValue("data"); 				
		}
	}
	      
	@Override
    public void endElement(String uri, String localName, String qName) throws SAXException {       
		if (localName.equals("forecast_conditions")){ 							// 2/2 on LAST condition - add to class array!
			forecasts.add(forecast); 
			forecastCondition = false;
			i ++;
		}
     }
	
	public Weather getInfo() {
		weather.forecasts = forecasts;
		return weather;
	}
}

/*
	CALL API: http://www.google.com/ig/api?weather=lincoln,nebraska
	RETRUNS: 
	<xml_api_reply version="1">
	<weather module_id="0" tab_id="0" mobile_row="0" mobile_zipped="1" row="0" section="0">
	<forecast_information>
	<city data="Lincoln, NE"/>
	<postal_code data="lincoln,nebraska"/>
	<latitude_e6 data=""/>
	<longitude_e6 data=""/>
	<forecast_date data="2012-04-03"/>
	<current_date_time data="2012-04-03 20:54:00 +0000"/>
	<unit_system data="US"/>
	</forecast_information>
	<current_conditions>
	<condition data="Overcast"/>
	<temp_f data="66"/>
	<temp_c data="19"/>
	<humidity data="Humidity: 52%"/>
	<icon data="/ig/images/weather/cloudy.gif"/>
	<wind_condition data="Wind: N at 16 mph"/>
	</current_conditions>
...
*/