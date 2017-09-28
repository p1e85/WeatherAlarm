package com.example.weatheralarm;

import java.util.Calendar;

import com.example.weatherdata.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class MainActivity extends Activity {
	String stringURL = "http://w1.weather.gov/xml/current_obs/KORD.xml";
	// String stringURL = "http://w1.weather.gov/xml/current_obs/KCHI.xml";

	TextView station, stationData, observation, observationData, weather,
			weatherData, temperature, temperatureData, wind, windData;
	ProgressDialog pd;

	/*private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				// update UI here
				
			

				int i = bundle.getInt("key");
			}
		}
	};*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);

		initTextViews();
		pd = new ProgressDialog(this);
		pd.setIndeterminate(true);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);
		Intent intent = new Intent(MainActivity.this, UpdateAlarm.class);
		PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0,
				intent, 0);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				3600000, pintent);

		startService(new Intent(getBaseContext(), BroadcastReceiver.class));
		
		stationData.setText(sharedprefs.getString("ID", ""));
		observationData.setText(sharedprefs.getString("TIME", ""));
		weatherData.setText(sharedprefs.getString("WEATHER", ""));
		temperatureData.setText(sharedprefs.getString("TEMPERATURE", ""));
		windData.setText(sharedprefs.getString("WIND", ""));

	}

	/*@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(UpdateAlarm.NOTIFICATION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}*/

	public void initTextViews() {
		station = (TextView) findViewById(R.id.station);
		stationData = (TextView) findViewById(R.id.stationdata);
		observation = (TextView) findViewById(R.id.observation);
		observationData = (TextView) findViewById(R.id.observationdata);
		weather = (TextView) findViewById(R.id.weather);
		weatherData = (TextView) findViewById(R.id.weatherdata);
		temperature = (TextView) findViewById(R.id.tempurature);
		temperatureData = (TextView) findViewById(R.id.tempuraturedata);
		wind = (TextView) findViewById(R.id.wind);
		windData = (TextView) findViewById(R.id.winddata);

	}

	/*
	 * public void downloadXML() {
	 * 
	 * AsyncTask<String, Void, String> task = new AsyncTask<String, Void,
	 * String>() {
	 * 
	 * @Override protected void onPreExecute() { // TODO Auto-generated method
	 * stub super.onPreExecute(); pd.show(); pd.setTitle("Weather Data");
	 * pd.setMessage("getting weather data"); }
	 * 
	 * @Override protected String doInBackground(String... urls) {
	 * 
	 * URL url; HttpURLConnection httpConnection; InputStream is;
	 * 
	 * try { Looper.prepare();
	 * 
	 * url = new URL(stringURL); httpConnection = (HttpURLConnection)
	 * url.openConnection();
	 * 
	 * File SDCardRoot = Environment.getExternalStorageDirectory(); // create a
	 * new file, specifying the path, and the filename // which we want to save
	 * the file as. File file = new File(SDCardRoot, "KORD.xml"); // File file =
	 * new File(SDCardRoot,"KCHI.xml");
	 * 
	 * FileOutputStream fileOutput = new FileOutputStream(file);
	 * 
	 * // this will be used in reading the data from the internet InputStream
	 * inputStream = httpConnection.getInputStream(); // Looper.loop();
	 * 
	 * // this is the total size of the file int totalSize =
	 * httpConnection.getContentLength(); pd.setMax(totalSize);
	 * 
	 * // variable to store total downloaded bytes int downloadedSize = 0;
	 * 
	 * // create a buffer... byte[] buffer = new byte[1024]; int bufferLength =
	 * 0;
	 * 
	 * // now, read through the input buffer and write the contents // to the
	 * file while ((bufferLength = inputStream.read(buffer)) > 0) { // add the
	 * data in the buffer to the file in the file // output stream (the file on
	 * the sd card fileOutput.write(buffer, 0, bufferLength); // add up the size
	 * so we know how much is downloaded downloadedSize += bufferLength;
	 * 
	 * } // close the output stream when done fileOutput.close(); // catch some
	 * possible errors...
	 * 
	 * } catch (MalformedURLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * @Override protected void onPostExecute(String result) { // TODO
	 * Auto-generated method stub super.onPostExecute(result); pd.dismiss();
	 * parseWeatherXML(); }
	 * 
	 * };
	 * 
	 * task.execute(); }
	 * 
	 * public void parseWeatherXML() { String station, observation, weather,
	 * temperature, wind;
	 * 
	 * try { SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
	 * try { String path = Environment.getExternalStorageDirectory() + "/" +
	 * "KORD.xml"; // String path = //
	 * Environment.getExternalStorageDirectory()+"/"+"KCHI.xml";
	 * 
	 * File file = new File(path); FileInputStream fileInputStream = new
	 * FileInputStream(file); // InputStream is = getAssets().open("KORD.xml");
	 * SAXHandler sh = new SAXHandler(); parser.parse(fileInputStream, sh);
	 * 
	 * final Weather mWeather = sh.getWeather(); // populate your view here!
	 * 
	 * String temp = mWeather.obsevationTime;
	 * stationData.setText(mWeather.getStationId());
	 * observationData.setText(mWeather.getObsevationTime());
	 * weatherData.setText(mWeather.getWeather());
	 * temperatureData.setText(mWeather.getTemperature());
	 * windData.setText(mWeather.getWind());
	 * 
	 * 
	 * ///!!! changed here where it saves to SharedPreferences !!!!////
	 * SharedPreferences sp = PreferenceManager
	 * .getDefaultSharedPreferences(this);
	 * 
	 * SharedPreferences.Editor editor = sp.edit(); editor.putString("station",
	 * (mWeather.getStationId())); editor.putString("observation",
	 * (mWeather.getObsevationTime())); editor.putString("weather",
	 * (mWeather.getWeather())); editor.putString("temperature",
	 * (mWeather.getTemperature())); editor.putString("wind",
	 * (mWeather.getWind())); editor.commit();
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * } catch (ParserConfigurationException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } catch (SAXException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * action bar item clicks here. The action bar will // automatically handle
	 * clicks on the Home/Up button, so long // as you specify a parent activity
	 * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
	 * R.id.action_refresh) { downloadXML();
	 * 
	 * return true; } return super.onOptionsItemSelected(item); }
	 * 
	 * public class SAXHandler extends DefaultHandler {
	 * 
	 * boolean stringTagStarted = false; private String tempVal; private Weather
	 * tempWeather;
	 * 
	 * public Weather getWeather() { return tempWeather; }
	 * 
	 * @Override public void startElement(String uri, String localName, String
	 * qName, Attributes attributes) throws SAXException { tempVal = ""; if
	 * (qName.equalsIgnoreCase("current_observation")) {
	 * 
	 * tempWeather = new Weather(); } }
	 * 
	 * @Override public void endElement(String uri, String localName, String
	 * qName) throws SAXException { Log.i("", "End of element: " + qName + " , "
	 * + localName); if (qName.equalsIgnoreCase("station_id")) {
	 * tempWeather.setStationId(tempVal); } else if
	 * (qName.equalsIgnoreCase("observation_time")) {
	 * tempWeather.setObsevationTime(tempVal); } else if
	 * (qName.equalsIgnoreCase("weather")) { tempWeather.setWeather(tempVal); }
	 * else if (qName.equalsIgnoreCase("temperature_string")) {
	 * tempWeather.setTemperature(tempVal); } else if
	 * (qName.equalsIgnoreCase("wind_string")) { tempWeather.setWind(tempVal); }
	 * 
	 * }
	 * 
	 * @Override public void characters(char[] ch, int start, int length) throws
	 * SAXException { Log.i("Characters", new String(ch, start, length));
	 * tempVal = new String(ch, start, length);
	 * 
	 * }
	 * 
	 * }
	 */
}
