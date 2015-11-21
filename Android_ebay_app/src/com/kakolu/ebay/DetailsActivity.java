package com.kakolu.ebay;

import java.io.InputStream;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class DetailsActivity extends ActionBarActivity {
	String message, pg, desc,loc,link;
	String item,img_url,title,price,ship,desText;
	String category,condition,buying_format,user,score,positive,pos,rating,top,store,type,handling,hand,location,exp,returns,one_day;
	double s;
	TextView t,l;
	ImageButton fb_button,buy_now;
	TextView i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12,i13,i14,i15;

	TextView c1,c2,c3,c4,c5,c6,c7,c9,c10,c11,c12;
	ImageView c8,c13,c14,c15,top_img;

	ImageButton basic,seller,shipping;
	JSONObject x;





	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		message = intent.getStringExtra("com.kakolu.ebay.MESSAGE");
		if(bundle!=null){
			pg = bundle.getString("pg");
		}
		setContentView(R.layout.activity_details);
		fb_button = (ImageButton) findViewById(R.id.fb_button);
		buy_now = (ImageButton)findViewById(R.id.buy_now);
		i1 = (TextView)findViewById(R.id.i1);
		i2 = (TextView)findViewById(R.id.i2);
		i3 = (TextView)findViewById(R.id.i3);
		i4 = (TextView)findViewById(R.id.i4);
		i5 = (TextView)findViewById(R.id.i5);
		i6 = (TextView)findViewById(R.id.i6);
		i7 = (TextView)findViewById(R.id.i7);
		i8 = (TextView)findViewById(R.id.i8);
		i9 = (TextView)findViewById(R.id.i9);
		i10 = (TextView)findViewById(R.id.i10);
		i11 = (TextView)findViewById(R.id.i11);
		i12 = (TextView)findViewById(R.id.i12);
		i13 = (TextView)findViewById(R.id.i13);
		i14 = (TextView)findViewById(R.id.i14);
		i15 = (TextView)findViewById(R.id.i15);

		c1 = (TextView)findViewById(R.id.c1);
		c2 = (TextView)findViewById(R.id.c2);
		c3 = (TextView)findViewById(R.id.c3);
		c4 = (TextView)findViewById(R.id.c4);
		c5 = (TextView)findViewById(R.id.c5);
		c6 = (TextView)findViewById(R.id.c6);
		c7 = (TextView)findViewById(R.id.c7);
		c8 = (ImageView)findViewById(R.id.c8);
		c9 = (TextView)findViewById(R.id.c9);
		c10 = (TextView)findViewById(R.id.c10);
		c11 = (TextView)findViewById(R.id.c11);
		c12 = (TextView)findViewById(R.id.c12);
		c13 = (ImageView)findViewById(R.id.c13);
		c14 = (ImageView)findViewById(R.id.c14);
		c15 = (ImageView)findViewById(R.id.c15);
		top_img = (ImageView)findViewById(R.id.top);

		basic = (ImageButton)findViewById(R.id.basic);
		seller = (ImageButton)findViewById(R.id.seller);
		shipping = (ImageButton)findViewById(R.id.shipping);
		top_img.setVisibility(View.INVISIBLE);;

		try
		{
			JSONObject jsonObj = new JSONObject(message);
			item = "item"+pg;

			findViewById(R.id.tab1).setVisibility(View.VISIBLE);
			findViewById(R.id.tab2).setVisibility(View.GONE);
			findViewById(R.id.tab3).setVisibility(View.GONE);
			if(jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").length()!= 0)
				img_url = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("pictureURLSuperSize").getString("0");
			else
				img_url = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("galleryURL").getString("0");			
			new DownloadImageTask((ImageView) findViewById(R.id.dispImg)).execute(img_url);
			title = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("title").getString("0");
			price = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("convertedCurrentPrice").getString("0");
			if(jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("shippingServiceCost").length()!= 0)
				s=jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("shippingServiceCost").getDouble("0");
			else
				s=0.0f;
			desText = "Price: $" + price;
			if(s>0)
				desText += "($+" +Double.toString(s)+ "Shipping)";
			else
				desText += "(FREE Shipping)";
			if( !jsonObj.getJSONObject(item).getJSONObject("basicInfo").isNull("location") && jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("location").length()!= 0)
			{
				loc = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("location").getString("0");
				desc =desText+" Location: "+loc;
			}
			
			t = (TextView) findViewById(R.id.title);
			t.setText(title);
			l = (TextView) findViewById(R.id.price);
			l.setText(desText);
			link = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("viewItemURL").getString("0");

			//tab1

			if( !jsonObj.getJSONObject(item).getJSONObject("basicInfo").isNull("categoryName") && jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("categoryName").length()!= 0)
			{
				category = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("categoryName").getString("0");
				c1.setText(category);
			}
			else c1.setText("N/A");
			i1.setText("Category Name");
			x = jsonObj.getJSONObject(item).getJSONObject("basicInfo");
			if( !jsonObj.getJSONObject(item).getJSONObject("basicInfo").isNull("conditionDisplayName") && jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("conditionDisplayName").length()!= 0 )
			{
				condition = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("conditionDisplayName").getString("0");

				c2.setText(condition);
			}
			else c2.setText("N/A");
			i2.setText("Condition");
			if( !jsonObj.getJSONObject(item).getJSONObject("basicInfo").isNull("listingType") && jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("listingType").length()!= 0)
			{
				buying_format = jsonObj.getJSONObject(item).getJSONObject("basicInfo").getJSONObject("listingType").getString("0");

				c3.setText(buying_format);
			}
			else c3.setText("N/A");
			i3.setText("Buying Format");
			x = jsonObj.getJSONObject(item).getJSONObject("sellerInfo");
			//tab2
			if( !jsonObj.getJSONObject(item).getJSONObject("sellerInfo").isNull("sellerUserName") && jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("sellerUserName").length()!= 0)
			{
				user = jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("sellerUserName").getString("0");

				c4.setText(user);
			}
			else c4.setText("N/A");
			i4.setText("User Name");
			if( !jsonObj.getJSONObject(item).getJSONObject("sellerInfo").isNull("feedbackScore") && jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("feedbackScore").length()!= 0)
			{
				score = jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("feedbackScore").getString("0");

				c5.setText(score);
			}

			else c5.setText("N/A");
			i5.setText("Feedback Score");
			if( !jsonObj.getJSONObject(item).getJSONObject("sellerInfo").isNull("positiveFeedbackPercent") && jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("positiveFeedbackPercent").length()!= 0)
			{
				positive = jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("positiveFeedbackPercent").getString("0");
				i6.setText("Positive feedback");
				pos = positive+"%";
				c6.setText(pos);
			}
			else c6.setText("N/A");
			i6.setText("Positive feedback");
			if( !jsonObj.getJSONObject(item).getJSONObject("sellerInfo").isNull("feedbackRatingStar") && jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("feedbackRatingStar").length()!= 0)
			{
				rating = jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("feedbackRatingStar").getString("0");

				c7.setText(rating);
			}
			else c7.setText("N/A");
			i7.setText("Feedback Rating");
			i7.setText("Feedback Rating");
			top = jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("topRatedSeller").getString("0");
			i8.setText("Top Rating");
			if(top.equals("true"))
			{
				c8.setImageResource(R.drawable.tick);
				top_img.setVisibility(View.VISIBLE);;
			}
			else
			{
				c8.setImageResource(R.drawable.cross);
				top_img.setVisibility(View.INVISIBLE);;
			}
			if( !jsonObj.getJSONObject(item).getJSONObject("sellerInfo").isNull("sellerStoreName") && jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("sellerStoreName").length()!= 0)
			{
				store = jsonObj.getJSONObject(item).getJSONObject("sellerInfo").getJSONObject("sellerStoreName").getString("0");
				c9.setText(store);
			}
			else c9.setText("N/A");
			i9.setText("Buying Format");
			//tab3
			if( !jsonObj.getJSONObject(item).getJSONObject("shippingInfo").isNull("shippingType") && jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("shippingType").length()!= 0)
			{
				type = jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("shippingType").getString("0");

				c10.setText(type);
			}
			else c10.setText("N/A");
			i10.setText("Shipping Type");
			if( !jsonObj.getJSONObject(item).getJSONObject("shippingInfo").isNull("handlingTime") && jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("handlingTime").length()!= 0)
			{
				handling = jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("handlingTime").getString("0");

				hand = handling+"day(s)";
				c11.setText(hand);
			}
			else c11.setText("N/A");
			i11.setText("Handling Time");
			if( !jsonObj.getJSONObject(item).getJSONObject("shippingInfo").isNull("shipToLocations") && jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("shipToLocations").length()!= 0)
			{
				location = jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("shipToLocations").getString("0");

				c12.setText(location);
			}
			else c12.setText("N/A");
			i12.setText("Shipping Locations");
			exp = jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("expeditedShipping").getString("0");
			i13.setText("Expedited Shipping");
			if(exp.equals("true"))
			{
				c13.setImageResource(R.drawable.tick);
			}
			else
			{
				c13.setImageResource(R.drawable.cross);				
			}

			one_day = jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("oneDayShippingAvailable").getString("0");
			i14.setText("One Day Shipping");
			if(one_day.equals("true"))
			{
				c14.setImageResource(R.drawable.tick);
			}
			else
			{
				c14.setImageResource(R.drawable.cross);				
			}

			returns = jsonObj.getJSONObject(item).getJSONObject("shippingInfo").getJSONObject("returnsAccepted").getString("0");
			i15.setText("Returns Accepted");
			if(returns.equals("true"))
			{
				c15.setImageResource(R.drawable.tick);
			}
			else
			{
				c15.setImageResource(R.drawable.cross);				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}



		buy_now.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent viewIntent =
						new Intent("android.intent.action.VIEW",
								Uri.parse(link));
				startActivity(viewIntent);
			}
		});

		basic.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				findViewById(R.id.tab1).setVisibility(View.VISIBLE);
				findViewById(R.id.tab2).setVisibility(View.GONE);
				findViewById(R.id.tab3).setVisibility(View.GONE);
				basic.setBackgroundResource(R.drawable.basic_clicked);
				seller.setBackgroundResource(R.drawable.seller);
				shipping.setBackgroundResource(R.drawable.ship);
			}
		});

		seller.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				findViewById(R.id.tab1).setVisibility(View.GONE);
				findViewById(R.id.tab2).setVisibility(View.VISIBLE);
				findViewById(R.id.tab3).setVisibility(View.GONE);
				basic.setBackgroundResource(R.drawable.basic);
				seller.setBackgroundResource(R.drawable.seller_clicked);
				shipping.setBackgroundResource(R.drawable.ship);

			}
		});

		shipping.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				findViewById(R.id.tab1).setVisibility(View.GONE);
				findViewById(R.id.tab2).setVisibility(View.GONE);
				findViewById(R.id.tab3).setVisibility(View.VISIBLE);
				basic.setBackgroundResource(R.drawable.basic);
				seller.setBackgroundResource(R.drawable.seller);
				shipping.setBackgroundResource(R.drawable.ship_clicked);

			}
		});

		fb_button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				loginFacebook(title, "Search Information from eBay.com",link,"", new StringBuilder(desc), img_url); 


			}
		});
	}
	public void loginFacebook(final String name,final String caption,final String link, final String hereLink,final StringBuilder description, final String picture)

	{

		final Session.StatusCallback myCallback = new Session.StatusCallback()

		{
			// callback when session changes state


			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened())
				{

					publishFeedDialog(name, caption, link, hereLink,description, picture);

				}

			}
		};
		
		Session session = Session.getActiveSession();
		if ( session!=null && !session.isOpened() && !session.isClosed()){
		    session.openForRead(new Session.OpenRequest(this).setCallback(myCallback));
		}else{
		    Session.openActiveSession(this, true, myCallback);
		}

	}

	private void publishFeedDialog(String name, String caption, String link,
			String hereLink, StringBuilder description, String picture)
	{


		// loginFacebook();
		Bundle params = new Bundle();
		params.putString("name", name);
		params.putString("caption", caption);
		params.putString("description", description.toString());
		params.putString("link", link);
		params.putString("picture", picture);

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,
				Session.getActiveSession(), params)).setOnCompleteListener(
						new OnCompleteListener()
						{

							@Override
							public void onComplete(Bundle values,FacebookException error) {
								if (error == null)
								{
									// When the story is posted, echo the success
									// and the post Id.
									final String postId = values.getString("post_id");
									if (postId != null)
									{
										Toast.makeText(getApplicationContext(),
												"Posted story, id: " + postId,
												Toast.LENGTH_SHORT).show();
									} else
									{
										// User clicked the Cancel button
										Toast.makeText(getApplicationContext(),
												"Publish cancelled", Toast.LENGTH_SHORT)
												.show();
									}
								} else if (error instanceof FacebookOperationCanceledException)
								{
									// User clicked the "x" button
									Toast.makeText(getApplicationContext(),
											"Publish cancelled", Toast.LENGTH_SHORT)
											.show();
								} else
								{
									// Generic, ex: network error
									Toast.makeText(getApplicationContext(),
											"Error posting story", Toast.LENGTH_SHORT)
											.show();
								}

							}

						}).build();
		feedDialog.show();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession()
		.onActivityResult(this, requestCode, resultCode, data);
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

}
