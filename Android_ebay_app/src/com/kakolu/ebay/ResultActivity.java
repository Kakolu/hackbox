package com.kakolu.ebay;

import java.io.InputStream;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends ActionBarActivity {

	String query;
	ImageView img0,img1,img2,img3,img4;
	String img_url0,img_url1,img_url2,img_url3,img_url4;
	String title0,title1,title2,title3,title4;
	String price0,price1,price2,price3,price4;
	String ship0,ship1,ship2,ship3,ship4;
	String desText0,desText1,desText2,desText3,desText4;
	double s0,s1,s2,s3,s4;
	TextView t0,t1,t2,t3,t4;
	TextView l0,l1,l2,l3,l4;
	TextView head;
	Bitmap mIcon_val;
	String message;
	Context context;
	int x;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		message = intent.getStringExtra("com.kakolu.ebay.MESSAGE");
		setContentView(R.layout.activity_result);
		calltable(message);

	}


	public void calltable(String message) {
		try
		{
			JSONObject jsonObj = new JSONObject(message);

			query = jsonObj.getString("query");
			head = (TextView) findViewById(R.id.textView1);
			String page_title = "Results for '"+query+"'";
			head.setText(page_title);
			//result 0
			if(jsonObj.getJSONObject("item0").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").length()!= 0)
				img_url0 = jsonObj.getJSONObject("item0").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").getString("0");
			else
				img_url0 = jsonObj.getJSONObject("item").getJSONObject("basicInfo").getJSONObject("galleryURL").getString("0");			
			new DownloadImageTask((ImageView) findViewById(R.id.img0)).execute(img_url0);
			title0 = jsonObj.getJSONObject("item0").getJSONObject("basicInfo").getJSONObject("title").getString("0");
			t0 = (TextView) findViewById(R.id.t1);
			t0.setText(title0);
			price0 = jsonObj.getJSONObject("item0").getJSONObject("basicInfo").getJSONObject("convertedCurrentPrice").getString("0");

			if(jsonObj.getJSONObject("item0").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").length()!= 0)
				s0=jsonObj.getJSONObject("item0").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").getDouble("0");
				else
					s0=0.0f;
			desText0 = "Price: $" + price0;
			if(s0>0)
				desText0 += "(+$" +Double.toString(s0)+ "Shipping)";
			else
				desText0 += "(FREE Shipping)";

			l0 = (TextView) findViewById(R.id.t2);
			l0.setText(desText0);

			//result 1
			if(jsonObj.getJSONObject("item1").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").length()!= 0)
				img_url1 = jsonObj.getJSONObject("item1").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").getString("0");
			else
				img_url1 = jsonObj.getJSONObject("item1").getJSONObject("basicInfo").getJSONObject("galleryURL").getString("0");			
			new DownloadImageTask((ImageView) findViewById(R.id.img1)).execute(img_url1);
			title1 = jsonObj.getJSONObject("item1").getJSONObject("basicInfo").getJSONObject("title").getString("0");
			t1 = (TextView) findViewById(R.id.t3);
			t1.setText(title1);
			price1 = jsonObj.getJSONObject("item1").getJSONObject("basicInfo").getJSONObject("convertedCurrentPrice").getString("0");
			if(jsonObj.getJSONObject("item1").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").length()!= 0)
				s1=jsonObj.getJSONObject("item1").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").getDouble("0");
				else
					s1=0.0f;
			desText1 = "Price: $" + price1;
			if(s1>0)
				desText1 += "(+$" +Double.toString(s1)+ "Shipping)";
			else
				desText1 += "(FREE Shipping)";

			l1 = (TextView) findViewById(R.id.t4);
			l1.setText(desText1);

			//result 2
			if(jsonObj.getJSONObject("item2").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").length()!= 0)
				img_url2 = jsonObj.getJSONObject("item2").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").getString("0");
			else
				img_url2 = jsonObj.getJSONObject("item2").getJSONObject("basicInfo").getJSONObject("galleryURL").getString("0");			
			new DownloadImageTask((ImageView) findViewById(R.id.img2)).execute(img_url2);
			title2 = jsonObj.getJSONObject("item2").getJSONObject("basicInfo").getJSONObject("title").getString("0");
			t2 = (TextView) findViewById(R.id.t5);
			t2.setText(title2);
			price2 = jsonObj.getJSONObject("item2").getJSONObject("basicInfo").getJSONObject("convertedCurrentPrice").getString("0");
			if(jsonObj.getJSONObject("item2").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").length()!= 0)
			s2=jsonObj.getJSONObject("item2").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").getDouble("0");
			else
				s2=0.0f;
			desText2 = "Price: $" + price2;
			if(s2>0)
				desText2 += "(+$" +Double.toString(s2)+ "Shipping)";
			else
				desText2 += "(FREE Shipping)";

			l2 = (TextView) findViewById(R.id.t6);
			l2.setText(desText2);

			//result 3
			if(jsonObj.getJSONObject("item3").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").length()!= 0)
				img_url3 = jsonObj.getJSONObject("item3").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").getString("0");
			else
				img_url3 = jsonObj.getJSONObject("item3").getJSONObject("basicInfo").getJSONObject("galleryURL").getString("0");		
			new DownloadImageTask((ImageView) findViewById(R.id.img3)).execute(img_url3);
			title3 = jsonObj.getJSONObject("item3").getJSONObject("basicInfo").getJSONObject("title").getString("0");
			t3 = (TextView) findViewById(R.id.t7);
			t3.setText(title3);
			price3 = jsonObj.getJSONObject("item3").getJSONObject("basicInfo").getJSONObject("convertedCurrentPrice").getString("0");
			
			if(jsonObj.getJSONObject("item3").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").length()!= 0)
				s3=jsonObj.getJSONObject("item3").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").getDouble("0");
				else
					s3=0.0f;
			desText3 = "Price: $" + price3;
			if(s3>0)
				desText3 += "(+$" +Double.toString(s3)+ "Shipping)";
			else
				desText3 += "(FREE Shipping)";

			l3 = (TextView) findViewById(R.id.t8);
			l3.setText(desText3);

			//result 4
			if(jsonObj.getJSONObject("item4").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").length()!= 0)
				img_url4 = jsonObj.getJSONObject("item4").getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").getString("0");
			else
				img_url4 = jsonObj.getJSONObject("item4").getJSONObject("basicInfo").getJSONObject("galleryURL").getString("0");		
			new DownloadImageTask((ImageView) findViewById(R.id.img4)).execute(img_url4);
			title4 = jsonObj.getJSONObject("item4").getJSONObject("basicInfo").getJSONObject("title").getString("0");
			t4 = (TextView) findViewById(R.id.t9);
			t4.setText(title4);
			price4 = jsonObj.getJSONObject("item4").getJSONObject("basicInfo").getJSONObject("convertedCurrentPrice").getString("0");
			if(jsonObj.getJSONObject("item4").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").length()!= 0)
				s4=jsonObj.getJSONObject("item4").getJSONObject("basicInfo").getJSONObject("shippingServiceCost").getDouble("0");
				else
					s4=0.0f;
			desText4 = "Price: $" + price4;
			if(s4>0)
				desText4 += "(+$" +Double.toString(s4)+ "Shipping)";
			else
				desText4 += "(FREE Shipping)";

			l4 = (TextView) findViewById(R.id.t10);
			l4.setText(desText4);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public void details(View view) {
		String pg = (String) view.getTag();
		Intent intent = new Intent(ResultActivity.this,DetailsActivity.class);
		intent.putExtra("pg",pg);
		intent.putExtra("com.kakolu.ebay.MESSAGE", message);
		startActivity(intent);

	}






	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}



	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.initial, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
