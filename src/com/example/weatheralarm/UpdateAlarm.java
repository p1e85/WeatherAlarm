package com.example.weatheralarm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class UpdateAlarm extends IntentService {
	SharedPreferences sharedprefs;

	public static final String NOTIFICATION = "com.example.weatheralarm";
	String stringURL = "http://w1.weather.gov/xml/current_obs/KORD.xml";

	public UpdateAlarm() {
		super("UpdateAlarm");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("UpdateAlarm", "alarm called");
		// do you weather fetch here
		
		sharedprefs = PreferenceManager.getDefaultSharedPreferences(this);

		BroadcastReceiver.completeWakefulIntent(intent);

		AsyncTask<Void, Integer, Void> downloadXML = new AsyncTask<Void, Integer, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					URL url = new URL(stringURL);
					HttpURLConnection httpConnection = (HttpURLConnection) url
							.openConnection();
					httpConnection.setRequestMethod("GET");
					httpConnection.connect();
					httpConnection.getContentType();
					Log.i("", "Resposne: " + httpConnection.getResponseCode());
					InputStream is = httpConnection.getInputStream();
					String receivedData = new String();
					int readData = 0;
					byte[] bytes = new byte[512];
					
					while ((readData = is.read(bytes)) > 0) {
						receivedData = receivedData.concat(new String(bytes, 0,
								readData));

					}

					parseWeatherXML(receivedData);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;

			}

		};

		downloadXML.execute();

	}

	public void parseWeatherXML(String data) {

		try {
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			try {
				String path = Environment.getExternalStorageDirectory() + "/"
						+ "KORD.xml";

				File file = new File(path);
				FileInputStream fileInputStream = new FileInputStream(file);
				parser.parse(new InputSource(new StringReader(data)),
						new DefaultHandler() {

							boolean temperature = false;
							boolean id = false;
							boolean time = false;
							boolean weather = false;
							boolean wind = false;

							@Override
							public void startDocument() throws SAXException {
								// TODO Auto-generated method stub
								super.startDocument();
								Log.i("", "Start of document");
							}

							@Override
							public void startElement(String uri,
									String localName, String qName,
									Attributes attributes) throws SAXException {

								for (int i = 0; i < attributes.getLength(); ++i) {
									Log.i("", "Attr: " + attributes.getQName(i)
											+ ", " + attributes.getLocalName(i)
											+ " Val: " + attributes.getValue(i));
								}

								if (qName.equals("station_id")) {
									id = true;
								}
								if (qName.equals("observation_time")) {
									time = true;
								}
								if (qName.equals("weather")) {
									weather = true;
								}
								if (qName.equals("temperature_string")) {
									temperature = true;
								}
								if (qName.equals("wind_string")) {
									wind = true;
								}

							}

							@Override
							public void endElement(String uri,
									String localName, String qName)
									throws SAXException {
								// TODO Auto-generated method stub
								super.endElement(uri, localName, qName);
								Log.i("", "End of element: " + qName + " , "
										+ localName);
								if (qName.equals("station_id")) {
									id = false;
								}
								if (qName.equals("observation_time")) {
									time = false;
								}
								if (qName.equals("weather")) {
									weather = false;
								}
								if (qName.equals("temperature_string")) {
									temperature = false;
								}
								if (qName.equals("wind_string")) {
									wind = false;
								}

							}

							@Override
							public void endDocument() throws SAXException {
								// TODO Auto-generated method stub
								super.endDocument();
								Log.i("", "End of document");
							}

							@Override
							public void characters(char[] ch, int start, int length) throws SAXException {

								if (id) {
									sharedprefs.edit().putString("ID", new String(ch, start, length)).commit();
								}
								if (time) {
									sharedprefs.edit().putString("TIME", new String(ch, start, length)).commit();
								}
								if (weather) {
									sharedprefs.edit().putString("WEATHER", new String(ch, start, length)).commit();
								}
								if (temperature) {
									sharedprefs.edit().putString("TEMPERATURE", new String(ch, start, length)).commit();
								}
								if (wind) {
									sharedprefs.edit().putString("WIND", new String(ch, start, length)).commit();
								}

							}

						});

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
