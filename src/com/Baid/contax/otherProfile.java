package com.Baid.contax;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


public class otherProfile extends Activity {

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
		name =  (TextView)findViewById(R.id.textView7);

		String empty = "";

		SharedPreferences prefs = getSharedPreferences("com.Baid.contax", Context.MODE_PRIVATE);

		String f1 = prefs.getString("ofirst", "");
		String l1 =prefs.getString("olast", "");

		String p1=prefs.getString("ophone", "");
		String e1=prefs.getString("oemail", "");
		String t1=prefs.getString("otwitter","");
		String y1=prefs.getString("oyoutube", "");
		String i1=prefs.getString("oinstagram", "");
		String s1=prefs.getString("osnapchat", "");
		
		name.setText(f1 + " " + l1);
		phone.setText(Html.fromHtml(
				"<b>Phone number:</b>" + p1));
		email.setText(Html.fromHtml(
				"<b>Email:</b>" + e1));
		String tlink = "https://twitter.com/"+t1;
		String ylink = "http://www.youtube.com/user/"+y1;
		String ilink = "http://instagram.com/"+i1;
		
		twitter.setText(Html.fromHtml(
				"<b>Twitter:</b>" +
						"<a href="+tlink+">"+t1+"</a> "));
		twitter.setMovementMethod(LinkMovementMethod.getInstance());
		
		youtube.setText(Html.fromHtml(
				"<b>Youtube:</b>" +
						"<a href="+ylink+">"+y1+"</a> "));
		youtube.setMovementMethod(LinkMovementMethod.getInstance());
		instagram.setText(Html.fromHtml(
				"<b>Instagram:</b>" +
						"<a href="+ilink+">"+i1+"</a> "));
		instagram.setMovementMethod(LinkMovementMethod.getInstance());
		snapchat.setText(Html.fromHtml(
				"<b>Snapchat:</b>" + s1));
		



	}



}

