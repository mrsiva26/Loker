package com.example.loker;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateHome extends ActionBarActivity {
	TextView tv3,tv4;
	Button update;
	EditText peri;
	SharedPreferences sharedpreferences;	
	String latitude,longitude;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_home);
		latitude = getIntent().getStringExtra("lat");
		longitude = getIntent().getStringExtra("long");
		
		sharedpreferences = getSharedPreferences("Loker", Context.MODE_PRIVATE);
		
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView4);
		update = (Button) findViewById(R.id.updatebutton);
		peri = (EditText)findViewById(R.id.editText1);
		peri.setText(sharedpreferences.getString("spperi", "0"));
		tv3.setText(latitude);
		tv4.setText(longitude);
		
		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int temp=Integer.parseInt(sharedpreferences.getString("spperi", "0"));
				try{
					temp=Integer.parseInt(peri.getText().toString());
				}catch(Exception e){
					e.printStackTrace();
				}
				if(temp>0&&temp<1000){
					Editor editor = sharedpreferences.edit();
				    editor.putString("spperi", temp+"");
				    editor.putString("splat", latitude+"");
				    editor.putString("splong", longitude+"");
				    editor.commit();
				    
				}
				else{
					
					Toast.makeText(getApplicationContext(), "Please enter a number between 0 and 1000 as Perimeter", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}