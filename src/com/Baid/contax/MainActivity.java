package com.Baid.contax;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class MainActivity extends Activity implements View.OnClickListener {

	ImageView pro;
	TabHost th; 
	TextView welcome;
	EditText field;
	Button search;


	boolean init;
	LinearLayout view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		SharedPreferences myPrefs = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
		if(savedInstanceState == null){

			SharedPreferences.Editor editor = myPrefs.edit();

			editor.commit();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		init = myPrefs.getBoolean("init",false);
		if(!init){
			
			facebook();
		}
		
		tabs();

		

		pro = (ImageView)findViewById(R.id.imageView1);
		field = (EditText)findViewById(R.id.field);
		search = (Button)findViewById(R.id.search);
		search.setOnClickListener(this);

		view = (LinearLayout)findViewById(R.id.tab2);
		view.setOnClickListener(this);


	}


	private void tabs(){

		th= (TabHost)findViewById(R.id.tabhost); 
		th.setup(); 

		//first tab 
		TabSpec specs=th.newTabSpec("tag1"); 
		specs.setContent(R.id.tab1); 
		specs.setIndicator("Dashboard"); 
		th.addTab(specs); 
		TextView tv = (TextView) th.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv.setTextColor(Color.WHITE);

		//second tab 
		TabSpec specs2=th.newTabSpec("Contacts"); 
		specs2.setContent(R.id.tab2); 
		specs2.setIndicator("Contacts"); 
		th.addTab(specs2);
		TextView tv2 = (TextView) th.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tv2.setTextColor(Color.WHITE);
	}
	private class MyNetworkTask extends AsyncTask<URL, Void, Bitmap>{

		ImageView tIV;

		public MyNetworkTask(ImageView iv){
			tIV = iv;
		}

		@Override
		protected Bitmap doInBackground(URL... urls) {

			Bitmap networkBitmap = null;

			URL networkUrl = urls[0]; //Load the first element
			try {
				networkBitmap = BitmapFactory.decodeStream(
						networkUrl.openConnection().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			return networkBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			tIV.setImageBitmap(result);
		}

	}



	private void facebook(){
		// start Facebook Login

		//THIS is a BAD solution...fix later if you can
		//http://stackoverflow.com/questions/6343166/android-os-networkonmainthreadexception
		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		//StrictMode.setThreadPolicy(policy); 
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {

				if (session.isOpened()) {

					// make request to the /me API
					// Request user data and show the results
					Request.newMeRequest(session, new Request.GraphUserCallback() {

						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {

							if (user != null) {
								welcome = (TextView) findViewById(R.id.welcome);
								welcome.setText("Hello " + user.getName() + "!");
								//Load bitmap from internet
								String onLineImage = "http://graph.facebook.com/"+user.getId()+"/picture?type=large";
								URL onLineURL;

								try {
									onLineURL = new URL(onLineImage);
									new MyNetworkTask(pro).execute(onLineURL);
								} catch (MalformedURLException e) {
									e.printStackTrace();
								}

								/*
								URL img_value = null;
								try {
									img_value = new URL("http://graph.facebook.com/"+user.getId()+"/picture?type=large");
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								Bitmap mIcon1;
								try {
									mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
									pro.setImageBitmap(mIcon1);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}*/

								SharedPreferences settings = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
								SharedPreferences.Editor edit = settings.edit();

								boolean notNull = user.getFirstName()!=null && user.getLastName()!= null;
								edit.putBoolean("needName", notNull);
								edit.putString("first", user.getFirstName());
								edit.putString("last", user.getLastName());

								edit.commit();

							}
							else{
								Class ourClass;
								try {

									ourClass = Class.forName("com.Baid.contax.EditSettings");

									Intent ourIntent= new Intent(MainActivity.this, ourClass);
									SharedPreferences settings = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
									SharedPreferences.Editor edit = settings.edit();
									edit.putBoolean("needName", true); 
									edit.commit(); 
									startActivity(ourIntent);

								} catch (ClassNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}
					}).executeAsync();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		int iid = item.getItemId();
		if(iid == R.id.profile){

			//check if first log in-->editsettings else -->settings
			Class ourClass;
			try {


				ourClass = Class.forName("com.Baid.contax.EditSettings");


				Intent ourIntent= new Intent(MainActivity.this, ourClass);
				startActivity(ourIntent);

			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if(iid == R.id.vprofile){
			Class ourClass;
			try{
				if(init){

					//I havent written this class yet
					ourClass = Class.forName("com.Baid.contax.Profile");
				}
				else{

					ourClass = Class.forName("com.Baid.contax.EditSettings");
				}


				Intent ourIntent = new Intent(MainActivity.this, ourClass);
				startActivity(ourIntent);

			}catch(ClassNotFoundException e){

				e.printStackTrace();
			}

		}
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int vid = v.getId();
		if(vid == R.id.search){

			String user = field.getText().toString();
			field.setText("");
			ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
			query.whereEqualTo("Username", user);
			query.getFirstInBackground(new GetCallback<ParseObject>() {


				@Override
				public void done(ParseObject object, ParseException e) {
					// TODO Auto-generated method stub
					if (object == null) {
						//no result found
						AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();   
						alertDialog.setTitle("Alert ");   
						alertDialog.setMessage("Did not find result"); 
						alertDialog.setCanceledOnTouchOutside(true); 
						alertDialog.show(); 
					} else {

						Class ourClass;
						try {


							ourClass = Class.forName("com.Baid.contax.otherProfile");


							Intent ourIntent= new Intent(MainActivity.this, ourClass);



							object.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
								public void done(ParseObject object, ParseException e) {
									// all fields of the object will now be available here.
									String f1 = object.getString("FirstName");
									String l1 = object.getString("LastName");
									String p1 = object.getString("PhoneNumber");
									String e1 = object.getString("Email");
									String t1 = object.getString("Twitter");
									String y1 = object.getString("Youtube");
									String i1 = object.getString("Instagram");								  
									String s1 = object.getString("Snapchat");

									SharedPreferences settings = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
									SharedPreferences.Editor edit = settings.edit();

									edit.putString("ofirst", f1);
									edit.putString("olast", l1);

									edit.putString("ophone", p1);
									edit.putString("oemail", e1);
									edit.putString("otwitter", t1);
									edit.putString("oyoutube", y1);
									edit.putString("oinstagram", i1);
									edit.putString("osnapchat", s1);

									edit.commit();
								}
							});


							startActivity(ourIntent);

						} catch (ClassNotFoundException c) {
							// TODO Auto-generated catch block
							c.printStackTrace();
						}
					}
				}
			});
		}
		else if(vid == R.id.tab2){

			InputMethodManager inputManager = (InputMethodManager)
					getSystemService(MainActivity.this.INPUT_METHOD_SERVICE); 

			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		
		
		

	}

}
