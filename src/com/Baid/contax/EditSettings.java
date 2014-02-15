package com.Baid.contax;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;








import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;
public class EditSettings extends Activity implements View.OnClickListener {

	//SO the idea behind this class is to create a profile for the user
	//The first and last name are only necessary iff facebook extraction
	//was not successfull -->look at sharedpreferences

	//after you get the first data set, you need to setcontentview to second set

	//the xml files have backwards name: editsettings2 comes before editsettings

	EditText tw;
	EditText pNum;
	EditText email;
	EditText instagram;
	EditText youtube;
	EditText snapchat;

	EditText first;
	EditText last;
	EditText un;
	EditText pw;
	EditText cpw;


	EditText[]items = new EditText[NUM_ITEMS];
	Button save;
	Button next;

	LinearLayout view;
	LinearLayout view2;
	final static int NUM_ITEMS = 11;

	String fn;
	String ln;
	String userN;
	String passW;
	String cpassW;

	String phone;
	String em;
	String twh;
	String yt;
	String inst;
	String snap;

	boolean needName;
	boolean init;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//Parse.initialize(this, "xbYnaAIJ9KyQfqPEmOjoCV7PnIMhZMvzKnNKuTgD", "JT4KobCCJKZNLTtZKrhyWUpkOBejxUAa6P6Ag0xr");
		Parse.initialize(this, "xbYnaAIJ9KyQfqPEmOjoCV7PnIMhZMvzKnNKuTgD", "JT4KobCCJKZNLTtZKrhyWUpkOBejxUAa6P6Ag0xr"); 

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
		SharedPreferences prefs = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
		init = prefs.getBoolean("init", false);
		String fiName = prefs.getString("first", "");
		String laName = prefs.getString("last", "");
		if(!init || fiName.equals("")|| laName.equals("")){

			firstSet();
		}
		else{

			secondSet();

		}


	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		SharedPreferences prefs = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
		int vid = v.getId();
		if(vid == R.id.save){

			fn = prefs.getString("first", "");
			ln = prefs.getString("last", "");
			userN = prefs.getString("socioUN", "");
			passW = prefs.getString("password", "");
			if(fn == null || ln == null){

				firstSet();
			}
			else{

				phone = pNum.getText().toString();
				em = email.getText().toString();
				twh = tw.getText().toString();
				yt = youtube.getText().toString();
				inst = instagram.getText().toString();
				snap = snapchat.getText().toString();



				ParseObject user = new ParseObject("User");
				user.put("FirstName", fn);
				user.put("LastName", ln);
				user.put("Username", userN);
				user.put("Password", passW);
				user.put("PhoneNumber", phone);
				user.put("Email", em);
				user.put("Twitter", twh);
				user.put("Youtube", yt);
				user.put("Instagram", inst);
				user.put("Snapchat", snap);
				user.saveInBackground();



				try {
					//go to setting instead if account has already saved settings
					Class ourClass = Class.forName("com.Baid.contax.MainActivity");
					Intent ourIntent= new Intent(EditSettings.this, ourClass);

					SharedPreferences settings = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
					SharedPreferences.Editor edit = settings.edit();

					edit.putString("first", fn);
					edit.putString("last", ln);
					edit.putString("socioUN", userN);
					edit.putString("phone", phone);
					edit.putString("email", em);
					edit.putString("twitter", twh);
					edit.putString("youtube", yt);
					edit.putString("instagram", inst);
					edit.putString("snapchat", snap);
					edit.putBoolean("init", true);
					edit.commit();

					startActivity(ourIntent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			//
		}
		else if (vid == R.id.view){

			InputMethodManager inputManager = (InputMethodManager)
					getSystemService(EditSettings.this.INPUT_METHOD_SERVICE); 

			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		else if (vid == R.id.view2){

			InputMethodManager inputManager = (InputMethodManager)
					getSystemService(EditSettings.this.INPUT_METHOD_SERVICE); 

			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
		else if (vid == R.id.next){

			//check empty strings and make sure unique username
			if(needName){

				fn = first.getText().toString();
				ln = last.getText().toString();

			}
			else{


				fn = prefs.getString("first", "");
				ln = prefs.getString("last", "");


			}

			SharedPreferences settings = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = settings.edit();

			userN = un.getText().toString();


			passW = pw.getText().toString();
			cpassW = cpw.getText().toString();
			edit.putString("socioUN", userN);
			edit.putString("password", passW);
			edit.commit();

			boolean same = passW.equals(cpassW);
			if(same){


				secondSet();



			}
			else{

				pw.setText("");
				cpw.setText("");

			}

		}
	}

	private void save(){

		try { 
			//Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE 
			FileOutputStream output = openFileOutput("lines.txt",MODE_PRIVATE); 
			DataOutputStream dout = new DataOutputStream(output); 
			dout.writeInt(NUM_ITEMS); // Save line count 

			// Save lines 
			dout.writeUTF(pNum.getText().toString()); 
			dout.writeUTF(email.getText().toString()); 
			dout.writeUTF(tw.getText().toString());

			dout.flush(); // Flush stream ... 
			dout.close(); // ... and close. 

		} 
		catch (IOException exc) { exc.printStackTrace(); } 
	}

	private void load(){

		String empty = "";

		SharedPreferences prefs = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);

		String p = prefs.getString("phone", empty);
		String e = prefs.getString("email", empty);
		String t = prefs.getString("twitter", empty);
		String y = prefs.getString("youtube", empty);
		String i = prefs.getString("instagram", empty);
		String s = prefs.getString("snapchat", empty);

		if(p!=null){
			pNum.setText(p);
		}
		if(!e.equals("")){

			email.setText(e);			
		}
		if(!t.equals("")){
			tw.setText(t);
		}

		if(!y.equals("")){
			youtube.setText(y);

		}
		if(!i.equals("")){

			instagram.setText(i);
		}
		if(!s.equals("")){
			snapchat.setText(s);
		}





	}

	private void firstSet(){

		SharedPreferences prefs = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
		setContentView(R.layout.editsettings2);

		view = (LinearLayout)findViewById(R.id.view2);

		view2 = (LinearLayout)findViewById(R.id.view2);
		view2.setOnClickListener(this);

		first = (EditText)findViewById(R.id.first);
		last = (EditText)findViewById(R.id.last);
		un = (EditText)findViewById(R.id.un);
		pw = (EditText)findViewById(R.id.pw);
		cpw = (EditText)findViewById(R.id.cpw);

		next = (Button)findViewById(R.id.next);
		next.setOnClickListener(this);
		needName = prefs.getBoolean("needName", true);
		if(!needName){

			first.setVisibility(View.INVISIBLE);
			last.setVisibility(View.INVISIBLE);
		}
	}
	private void secondSet(){


		setContentView(R.layout.editsettings);
		view = (LinearLayout)findViewById(R.id.view);
		view.setOnClickListener(this);



		tw = (EditText)findViewById(R.id.twitter);
		pNum = (EditText)findViewById(R.id.phone);
		email = (EditText)findViewById(R.id.email);
		instagram = (EditText)findViewById(R.id.instagram);
		youtube = (EditText)findViewById(R.id.youtube);
		snapchat = (EditText)findViewById(R.id.snapchat);
		save = (Button)findViewById(R.id.save);
		save.setOnClickListener(this);

		load();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//load();
		//only load when secondset is executed
	}

}

