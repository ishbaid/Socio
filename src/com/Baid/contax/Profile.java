package com.Baid.contax;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


public class Profile extends Activity {

	TextView phone;
	TextView email;
	TextView twitter;
	TextView youtube;
	TextView instagram;
	TextView snapchat;
	TextView name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pro);

		phone = (TextView)findViewById(R.id.textView1);
		email = (TextView)findViewById(R.id.textView2);
		twitter = (TextView)findViewById(R.id.textView3);
		youtube = (TextView)findViewById(R.id.textView4);
		instagram = (TextView)findViewById(R.id.textView5);
		snapchat = (TextView)findViewById(R.id.textView6);
		name = (TextView)findViewById(R.id.textView7);
		
		String empty = "";

		SharedPreferences prefs = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);
		
		String f = prefs.getString("first", empty);
		String l = prefs.getString("last", empty);
		String p = prefs.getString("phone", empty);
		String e = prefs.getString("email", empty);
		String t = prefs.getString("twitter", empty);
		String y = prefs.getString("youtube", empty);
		String i = prefs.getString("instagram", empty);
		String s = prefs.getString("snapchat", empty);
		
		name.setText(f+" "+l);
		phone.setText(Html.fromHtml(
				"<b>Phone number: </b>" + p));
		email.setText(Html.fromHtml(
				"<b>Email: </b>" + e));
		String tlink = "https://twitter.com/"+t;
		String ylink = "http://www.youtube.com/user/"+y;
		String ilink = "http://instagram.com/"+i;
		
		twitter.setText(Html.fromHtml(
				"<b>Twitter: @</b>" +
						"<a href="+tlink+">"+t+"</a> "));
		twitter.setMovementMethod(LinkMovementMethod.getInstance());
		
		youtube.setText(Html.fromHtml(
				"<b>Youtube: </b>" +
						"<a href="+ylink+">"+y+"</a> "));
		youtube.setMovementMethod(LinkMovementMethod.getInstance());
		instagram.setText(Html.fromHtml(
				"<b>Instagram: </b>" +
						"<a href="+ilink+">"+i+"</a> "));
		instagram.setMovementMethod(LinkMovementMethod.getInstance());
		snapchat.setText(Html.fromHtml(
				"<b>Snapchat: </b>" + s));
		



	}



}
