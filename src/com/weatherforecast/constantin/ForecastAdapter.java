package com.weatherforecast.constantin;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.weatherforecast.constantin.WeatherActivity.weatherPicture;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastAdapter extends ArrayAdapter<Forecast> {

	private ArrayList<Forecast> items;
	String highCelsius, lowCelsius;
	private ImageView ivIcon;
	
    public ForecastAdapter(Context context, int textViewResourceId, ArrayList<Forecast> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }
    
	public Forecast clickAtPosition(int position) {
		Forecast Forecast = getItem(position);
		return Forecast;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = null;
        if (null == convertView) {// http://stackoverflow.com/questions/4321343/android-getsystemservice-inside-custom-arrayadapter
        	try{
        		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		itemView = vi.inflate(R.layout.row, null);
        	} catch (Exception e) {
        		Log.e("ERROR", "ERROR IN CODE (LayoutInflater) :" + e.toString());
        	}
        }else{
        	itemView = convertView;
        }
        Forecast forecast = items.get(position);
        if (forecast != null) {
        	ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
        	new weatherPicture().execute(forecast.weather.icon);
/*			Bitmap bitmap;
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL("https://s3.amazonaws.com/weatherandroid/pngs/" + forecast.icon + ".png").getContent());
				ivIcon.setImageBitmap(bitmap);	
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/
            TextView tvDayOfWeek = (TextView) itemView.findViewById(R.id.tvDayOfWeek);
            if (tvDayOfWeek != null) tvDayOfWeek.setText(forecast.dayOfWeek);
            
            TextView tvHigh = (TextView) itemView.findViewById(R.id.tvHigh);
            highCelsius = Integer.toString((forecast.high - 32) * 5 / 9);
            if (tvHigh != null) tvHigh.setText (Integer.toString(forecast.high) + " F   " + highCelsius + " C");
            		
            TextView tvLow = (TextView) itemView.findViewById(R.id.tvLow);
            lowCelsius = Integer.toString((forecast.low - 32) * 5 / 9);
            if (tvLow != null) tvLow.setText (Integer.toString(forecast.low) + " F   " + lowCelsius + " C");
           
            TextView tvCondition = (TextView) itemView.findViewById(R.id.tvCondition);
            if (tvCondition != null) tvCondition.setText(forecast.weather.condition);
        }
        return itemView;
    }
   
	class weatherPicture extends AsyncTask<String, Void, String> {
		ProgressDialog postDialog;
		Bitmap bitmap;
		@Override
		protected String doInBackground(String... params) {
			try {
				bitmap = BitmapFactory.decodeStream((InputStream) new URL("https://s3.amazonaws.com/weatherandroid/pngs/" + params[0] + ".png").getContent());
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