package com.weatherforecast.constantin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import com.google.ads.AdRequest;
//import com.google.ads.AdView;

public class WeatherActivity extends ListActivity implements OnClickListener,
		OnKeyListener {

	static final String googleWeatherAPI = "http://www.google.com/ig/api?weather=";
	public static String filename = "MyWeatherData";
	static final int UNIQUE_ID = 123456789;
	SharedPreferences someData;
	ImageView ivIcon;
	TextView tvTemperature, tvTemperatureCelsius, tvCondition, tvWindCondition, tvHumidity;
	EditText etLocation;
	Button bView;
	ListView lvMic;
	ImageButton ibMic;
	Location location = new Location();
	String strTemp;
	NotificationManager nm;
	TextToSpeech textToSpeech;
	Weather weather = null;
	Forecast forecast = null;
	ArrayList<Forecast> forecasts = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setVariables();
		new getWeather2().execute();
		// getWeather();
		// showNotification();
	}

	class getWeather2 extends AsyncTask<String, Void, String> {
		ProgressDialog postDialog;
		Bitmap bitmap;
		@Override
		protected String doInBackground(String... params) {
			// cool example: http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
			// below: http://www.vogella.com/articles/AndroidJSON/article.html
			String data = getData();
			try {
				weather = new Weather();
				forecast = null;
				forecasts = new ArrayList<Forecast>();

				// A) for list:
				Log.i("tester", "data: " + data);
				JSONObject jsonObject = new JSONObject(data);
				Log.i("tester", "cod: " + jsonObject.getString("cod"));
				JSONArray jsonArray = jsonObject.getJSONArray("list");
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObjectList = jsonArray.getJSONObject(i);
					Log.i("tester", jsonObjectList.getString("dt_txt"));
					Log.i("tester", jsonObjectList.getJSONObject("main").getString("temp"));
					Log.i("tester", jsonObjectList.getJSONArray("weather").getJSONObject(0).getString("icon"));
					
					// populate class and subclass
					if (i == 0){
						weather.condition = jsonObjectList.getJSONArray("weather").getJSONObject(0).getString("description");
						weather.icon = jsonObjectList.getJSONArray("weather").getJSONObject(0).getString("icon");
						weather.temperature = convertKelvin2Fahrenheit(jsonObjectList.getJSONObject("main").getInt("temp")); 
						weather.temperatureCelsius = (weather.temperature- 32) * 5 / 9;
						weather.humidity = jsonObjectList.getJSONObject("main").getString("humidity"); 
						weather.windCondition = jsonObjectList.getJSONObject("wind").getString("speed"); 
					}
					forecast = new Forecast();
					forecast.weather.condition = jsonObjectList.getJSONArray("weather").getJSONObject(0).getString("description");
					forecast.weather.icon = jsonObjectList.getJSONArray("weather").getJSONObject(0).getString("icon");
					forecast.dayOfWeek = jsonObjectList.getString("dt_txt");		
					forecast.high = convertKelvin2Fahrenheit(jsonObjectList.getJSONObject("main").getInt("temp_max")); 
					forecast.low = convertKelvin2Fahrenheit(jsonObjectList.getJSONObject("main").getInt("temp_min")); 
					forecasts.add(forecast); 
				}
				weather.forecasts = forecasts;
				// B) for array:
			/*  JSONArray jsonArray = new JSONArray(data);
				Log.i("tester", "Number of entries " + jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Log.i("tester", jsonObject.getString("message"));
				}*/
			} catch (Exception e) {
				Log.e("tester", "error: " + e.getMessage());
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			postDialog = ProgressDialog.show(WeatherActivity.this, "please wait, loading...", "weather forecast data", true, false);
		}
		@Override
		protected void onPostExecute(String str) {
			postDialog.dismiss();
			showWeather();
		}
	}
	
	private void getWeatherNew() {
		// cool example: http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
		// below: http://www.vogella.com/articles/AndroidJSON/article.html
		String data = getData();
		try {
			weather = new Weather();
			forecast = null;
			forecasts = new ArrayList<Forecast>();

			// A) for list:
			Log.i("tester", "data: " + data);
			JSONObject jsonObject = new JSONObject(data);
			Log.i("tester", "cod: " + jsonObject.getString("cod"));
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObjectList = jsonArray.getJSONObject(i);
				Log.i("tester", jsonObjectList.getString("dt_txt"));
				Log.i("tester", jsonObjectList.getJSONObject("main").getString("temp"));
				Log.i("tester", jsonObjectList.getJSONArray("weather").getJSONObject(0).getString("icon"));
				
				// populate class and subclass
				if (i == 0){
					weather.icon = jsonObjectList.getJSONArray("weather").getJSONObject(0).getString("icon");
					weather.temperature = convertKelvin2Fahrenheit(jsonObjectList.getJSONObject("main").getInt("temp")); 
					weather.temperatureCelsius = (weather.temperature- 32) * 5 / 9;
					weather.humidity = jsonObjectList.getJSONObject("main").getString("humidity"); 
					weather.windCondition = jsonObjectList.getJSONObject("wind").getString("speed"); 
				}
				forecast = new Forecast();
				forecast.weather.icon = jsonObjectList.getJSONArray("weather").getJSONObject(0).getString("icon");
				forecast.dayOfWeek = jsonObjectList.getString("dt_txt");		
				forecast.high = convertKelvin2Fahrenheit(jsonObjectList.getJSONObject("main").getInt("temp_max")); 
				forecast.low = convertKelvin2Fahrenheit(jsonObjectList.getJSONObject("main").getInt("temp_min")); 
				forecasts.add(forecast); 
			}
			weather.forecasts = forecasts;
			
			// B) for array:
		/*  JSONArray jsonArray = new JSONArray(data);
			Log.i("tester", "Number of entries " + jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Log.i("tester", jsonObject.getString("message"));
			}*/
		} catch (Exception e) {
			Log.e("tester", "error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	public int convertKelvin2Fahrenheit(int kelvin){
		return (int)Math.round(1.8 * (kelvin - 273) + 32);
	}

	public String getData() {
		// data from: http://openweathermap.org/wiki/API/JSON_API#City_list
		StringBuilder URL = new StringBuilder("http://api.openweathermap.org/data/2.1/forecast/city?q=");
		if (etLocation.getText().length() == 0)
			location.area = "Los Angeles";
		else 
			location.area = etLocation.getText().toString(); 
		URL.append(location.area.replace(" ", "%20"));
		String fullURL = URL.toString();		

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();		
		HttpGet httpGet = new HttpGet(fullURL);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e("tester", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	private void speakCondition() {
		textToSpeech.speak(weather.condition + " weather",
				TextToSpeech.QUEUE_FLUSH, null); // flush = get read of other
													// speech if any
	}

	private void getWeather() {
		StringBuilder URL = new StringBuilder(googleWeatherAPI);
		if (etLocation.getText().length() == 0) {
			location.area = "Los Angeles";
		} else {
			location.area = etLocation.getText().toString(); // Toast.makeText(WeatherActivity.this,
																// etLocation.getText().toString(),
																// Toast.LENGTH_SHORT).show();
		}
		URL.append(location.area.replace(" ", "%20"));
		String fullURL = URL.toString();
		try {
			URL website = new URL(fullURL);
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance(); // getting
																				// xmlreader
																				// to
																				// parse
																				// data
																				// -
																				// SAP
																				// (=
																				// simple
																				// api
																				// xml)
			SAXParser saxParser = saxParserFactory.newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			WeatherXml xmlHandler = new WeatherXml();
			xmlReader.setContentHandler(xmlHandler); // xmlreader lookS for
														// things that we want
														// to look for
			InputSource in = new InputSource(website.openStream());
			in.setEncoding("ISO-8859-1"); // to understand foreign characters!
			xmlReader.parse(in); // parse the data from the web
			weather = xmlHandler.getInfo();
			showWeather();
		} catch (Exception e) {
			tvTemperature.setText("error: " + e.getMessage());
		}
	}
	private void showWeather() {
//		Bitmap bitmap;
//		try {
//			bitmap = BitmapFactory.decodeStream((InputStream) new URL("http://openweathermap.org/img/w/" + weather.icon + ".png").getContent());
//			ivIcon.setImageBitmap(bitmap);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		new weatherPicture().execute();
		tvTemperature.setText(Integer.toString(weather.temperature) + " F");
		tvTemperatureCelsius.setText(Integer.toString(weather.temperatureCelsius) + " C");
		tvCondition.setText(weather.condition);
		tvWindCondition.setText("wind: " + weather.windCondition);
		tvHumidity.setText("humidity: " + weather.humidity);

		ForecastAdapter adapter = new ForecastAdapter(WeatherActivity.this, R.layout.row, weather.forecasts);
		setListAdapter(adapter); // for (Forecast forecast : weather.forecasts)
									// strTemp += "\n" + forecast.dayOfWeek +
									// " " + forecast.high;
	}

	private void setVariables() {
		etLocation = (EditText) findViewById(R.id.etLocation);
		etLocation.setOnKeyListener(this);
		bView = (Button) findViewById(R.id.bView);
		bView.setOnClickListener(this);
		ivIcon = (ImageView) findViewById(R.id.ivIcon);
		tvTemperature = (TextView) findViewById(R.id.tvTemperature);
		tvTemperatureCelsius = (TextView) findViewById(R.id.tvTemperatureCelsius);
		tvCondition = (TextView) findViewById(R.id.tvCondition);
		tvWindCondition = (TextView) findViewById(R.id.tvWindCondition);
		tvHumidity = (TextView) findViewById(R.id.tvHumidity);
		lvMic = (ListView) findViewById(R.id.lvMicReturn);
		ibMic = (ImageButton) findViewById(R.id.ibMic);

		// Check to see if a recognition activity is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0); // hide mic when
																// function n/a
		if (activities.size() != 0)
			ibMic.setOnClickListener(this);
		else
			ibMic.setEnabled(false); // ibMic.setVisibility(View.GONE);

		textToSpeech = new TextToSpeech(this,
				new TextToSpeech.OnInitListener() { // speak setup
					public void onInit(int status) {
						if (status != TextToSpeech.ERROR)
							textToSpeech.setLanguage(Locale.US);
					}
				});

		someData = getSharedPreferences(filename, 0); // get location, if saved
														// in preferences
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); // setup
																			// notification
																			// manager
	}

	public void onClick(View v) {
		// refresh an ad
		// AdView adView = (AdView)this.findViewById(R.id.adMain);
		// adView.loadAd(new AdRequest());
		switch (v.getId()) {
		case R.id.bView:
			new getWeather2().execute();
			//getWeatherNew();
			//hideKeypad();
			//speakCondition();
			//showNotification();
			break;

		case R.id.ibMic:
			//hideKeypad();
			startVoiceRecognitionActivity();
			break;
		}
	}

	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM); // which language -
															// required code!
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the location"); // optional
																				// prompt
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1); // Specify how
																// many results
																// you want to
																// receive. The
																// results will
																// be sorted by
																// confidence
		startActivityForResult(intent, UNIQUE_ID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == UNIQUE_ID && resultCode == RESULT_OK) {
			ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			if (results != null && results.size() > 0) {
				etLocation.setText(results.get(0));
				new getWeather2().execute();
				//getWeatherNew();
				//hideKeypad();
				//speakCondition();
				//showNotification();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showNotification() {
		nm.cancel(UNIQUE_ID); // quick fix to clear notification after showing -
								// unique id so it does not cancel other
								// notifications

		Intent intent = new Intent(this, WeatherActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0); // intent
																			// pending
																			// so
																			// when
																			// you
																			// click
																			// on
																			// the
																			// status
																			// bar,
																			// u
																			// see
																			// message
		String title = Integer.toString(weather.temperature) + " ";
		String body = Integer.toString(weather.temperature) + " F "
				+ Integer.toString(weather.temperatureCelsius) + " C" + " "
				+ weather.condition + " - click to update";
		Notification n = new Notification(R.drawable.sun_clouds, title,
				System.currentTimeMillis()); // pic must be 24x24 png, and time
												// to open
		n.setLatestEventInfo(this, title, body, pi); // pending intent!
		// n.defaults = Notification.DEFAULT_ALL; // how to notify user -> need
		// to add VIBRATE PERMISSION!
		nm.notify(UNIQUE_ID, n); // generate new notification with same id
		// finish(); // so when you go back, it does not show the notification
	}

	private void hideKeypad() { // hide keypad after search
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etLocation.getWindowToken(), 0); // gets
																		// windows
																		// token
																		// from
																		// text
																		// box
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) { // on click
																// enter in text
																// box, do a
																// search
		// If the event is a key-down event on the "enter" button
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			new getWeather2().execute();
			//getWeatherNew();
			//hideKeypad();
			return true;
		}
		return false;
	}

	@Override
	protected void onPause() {
		saveMyLocation();
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		saveMyLocation();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		loadMyLocation();
		super.onResume();
	}

	private void saveMyLocation() {
		String data = location.area;
		SharedPreferences.Editor editor = someData.edit();
		editor.putString("sharedString", data);
		editor.commit();
	}

	private void loadMyLocation() {
		someData = getSharedPreferences(filename, 0); // need to get the data
														// first, again, in case
														// was saved before!
		location.area = someData.getString("sharedString",
				"Could not load data");
	}

	class weatherPicture extends AsyncTask<String, Void, String> {
		ProgressDialog postDialog;
		Bitmap bitmap;
		@Override
		protected String doInBackground(String... params) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL("https://s3.amazonaws.com/weatherandroid/pngs/" + weather.icon + ".png").getContent());
				Log.d("tester", "pic get");
			} catch (MalformedURLException e) {
				Log.d("tester", "pic err1: " + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.d("tester", "pic err2: " + e.getMessage());
				e.printStackTrace();
			}
			String result = "";
			return result;
		}
		@Override
		protected void onPostExecute(String str) {
			ivIcon.setImageBitmap(bitmap);
			Log.d("tester", "pic post");
		}
	}
}