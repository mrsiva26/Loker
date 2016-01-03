package com.example.loker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements LocationListener{
	LocationManager mylocationManager;
	Button btnGPSShowLocation;
	Button btnNWShowLocation;
	AppLocationService appLocationService;
	SharedPreferences sharedpreferences;
	Button convert,revert;
	private static final long MIN_DISTANCE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 1000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appLocationService = new AppLocationService(MainActivity.this);
		mylocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		mylocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE,this);
		sharedpreferences = getSharedPreferences("Loker", Context.MODE_PRIVATE);
		convert = (Button) findViewById(R.id.convert);
		revert = (Button) findViewById(R.id.revert);
		btnGPSShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);
		revert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Open file
				File file = new File(Environment.getExternalStorageDirectory() +"/.Loker/1.txt");
				Toast.makeText(getApplicationContext(), "Found the file", Toast.LENGTH_SHORT).show();


				int len;
				char[] chr = new char[4096];
				final StringBuffer buffer = new StringBuffer();
				FileReader reader = null;
				try {
					reader = new FileReader(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				try {
					while ((len = reader.read(chr)) > 0) {
						buffer.append(chr, 0, len);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				String s=buffer.toString();
				int count=0;
				for(int i=0;i<s.length();i++)
				{
					if(s.charAt(i)==',')
						count++;
				}
				String temchars[]=s.split(",");

				byte bfile[]=new byte[temchars.length];
				Byte oByte[]=new Byte[temchars.length];
				for(int i=0;i<temchars.length;i++)
				{
					oByte[i]=Byte.valueOf(temchars[i]);
				}
				System.out.println(oByte.length);

				for(int i = 0; i < oByte.length; i++){
					bfile[i] = oByte[i].byteValue();
				}

				Bitmap bmp = BitmapFactory.decodeByteArray(bfile, 0, bfile.length);
				ImageView image = (ImageView) findViewById(R.id.imageView1);
				image.setImageBitmap(bmp);

			}
		});

		convert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String whatsappImageFolderName = Environment.getExternalStorageDirectory() +"/WhatsApp/Media/WhatsApp Images/";
				File whatsappImageFolder = new File(whatsappImageFolderName);
				File ourFolder = new File(Environment.getExternalStorageDirectory() +"/.Loker");
				ourFolder.mkdir();
				try {
					if(whatsappImageFolder.exists()){
						Toast.makeText(getApplicationContext(), "I have reached whatsapp folder", Toast.LENGTH_SHORT).show();
						String str[] = whatsappImageFolder.list();
						File tester = new File(Environment.getExternalStorageDirectory() +"/.Loker/villan.txt");
						tester.createNewFile();
						FileWriter fw = new FileWriter(tester);
						for(int i=1;i<str.length;i++){
							Toast.makeText(getApplicationContext(), "Backing up "+str[i], Toast.LENGTH_LONG).show();
							fw.write(str[i]+"\n");
							File pic_file=new File(Environment.getExternalStorageDirectory() +"/WhatsApp/Media/WhatsApp Images/"+str[i]);
							FileInputStream fis = null;
							try {
								fis = new FileInputStream(pic_file);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

							Bitmap bm = BitmapFactory.decodeStream(fis);
							ByteArrayOutputStream baos = new ByteArrayOutputStream();  
							bm.compress(Bitmap.CompressFormat.JPEG, 100 , baos);    
							byte[] imageInByte = baos.toByteArray(); 						

							//							BufferedWriter outputWriter = null;
							//							String pic_name=pic_file.getName().replaceFirst("[.][^.]+$", "");
							//							String pic_format=URL.substring(URL.lastIndexOf(".")).replace(".", "");
							//							//System.out.println(pic_name+" "+pic_format);
							//							String t_filename=pic_name+pic_format+".txt";
							//							System.out.println(t_filename);
							//outputWriter = new BufferedWriter(new FileWriter(t_filename));
							File pictxtfile = new File(Environment.getExternalStorageDirectory() +"/.Loker/"+str[i]+".txt");
							pictxtfile.createNewFile();
							FileWriter nwfw = new FileWriter(pictxtfile);
							for (int j = 0; j < imageInByte.length; j++) {
								nwfw.write(imageInByte[j]+",");
							}
							//							outputWriter.flush();  
							//							outputWriter.close();
							nwfw.close();
							pic_file.delete();
						}

						fw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		});

		btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
				if (gpsLocation != null) {
					double latitude = gpsLocation.getLatitude();
					double longitude = gpsLocation.getLongitude();
					Toast.makeText(getApplicationContext(),"Mobile Location (GPS): \nLatitude: " + latitude + "\nLongitude: " + longitude,Toast.LENGTH_SHORT).show();
					updateHome(latitude,longitude);
				} else {
					showSettingsAlert("GPS");
				}
			}
		});

		btnNWShowLocation = (Button) findViewById(R.id.btnNWShowLocation);
		btnNWShowLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Location nwLocation = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
				if (nwLocation != null) {
					double latitude = nwLocation.getLatitude();
					double longitude = nwLocation.getLongitude();
					Toast.makeText(getApplicationContext(),"Mobile Location (NW): \nLatitude: " + latitude+ "\nLongitude: " + longitude,Toast.LENGTH_LONG).show();
					updateHome(latitude,longitude);
				} else {
					showSettingsAlert("NETWORK");
				}
			}
		});

	}

	protected void updateHome(double latitude, double longitude) {
		Intent i = new Intent(MainActivity.this,UpdateHome.class);
		i.putExtra("lat", latitude+"");
		i.putExtra("long", longitude+"");
		startActivity(i);

	}

	public void showSettingsAlert(String provider) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MainActivity.this);

		alertDialog.setTitle(provider + " SETTINGS");
		alertDialog.setMessage(provider + " is not enabled! Want to go to settings menu?");

		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				MainActivity.this.startActivity(intent);
			}
		});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
		boolean decision = decisionMaker(location.getLatitude(),location.getLongitude());
		if(decision){
			Toast.makeText(getApplicationContext(), "You have moved", Toast.LENGTH_SHORT).show();
		}else{
			//Toast.makeText(getApplicationContext(), "You're at home", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean decisionMaker(double nwLatitude, double nwLongitude) {
		try{
			double homelat = Double.parseDouble(sharedpreferences.getString("splat", "0"));
			double homelong = Double.parseDouble(sharedpreferences.getString("splong", "0"));
			double homeperi = Double.parseDouble(sharedpreferences.getString("spperi", "0"));
			double homelatinm = homelat * 110.54 * 1000;
			double homelonginm = homelong * 111.32 * Math.cos(homelat) * 1000;
			double nwlatinm = nwLatitude * 110.54 * 1000;
			double nwlonginm = nwLongitude * 111.32* Math.cos(nwLatitude) * 1000;

			if( nwlatinm<homelatinm + homeperi && nwlatinm>homelatinm - homeperi && nwlonginm<homelonginm + homeperi && nwlonginm>homelonginm - homeperi && sharedpreferences.getBoolean("athome", true)){

			}
			else{
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
				mBuilder.setContentTitle("Ready to Lock apps");
				mBuilder.setContentText(nwLatitude + " - " + nwLongitude);
				mBuilder.setAutoCancel(true);
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				mNotificationManager.notify(1, mBuilder.build());
				sharedpreferences.edit().putBoolean("athome", false).commit();
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public void onProviderDisabled(String provider) {}
	@Override
	public void onProviderEnabled(String provider) {}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

}
