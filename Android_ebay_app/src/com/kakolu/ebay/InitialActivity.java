package com.kakolu.ebay;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.R.bool;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InitialActivity extends ActionBarActivity {
	boolean error_flag=false;
	EditText key_value;
	EditText from_value;
	EditText to_value;
	TextView error_value;
	ImageButton clear,search;
	Spinner sort_menu;
	double from;
	double to;
	String sort_value,key,f,t,sort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial);
		key_value = (EditText) findViewById(R.id.key_value);
		key=key_value.getText().toString();
		from_value = (EditText) findViewById(R.id.from_value);
		f=from_value.getText().toString();
		to_value = (EditText) findViewById(R.id.to_value);
		t=to_value.getText().toString();
		error_value = (TextView) findViewById(R.id.error_value);
		clear = (ImageButton) findViewById(R.id.clear);
		search = (ImageButton) findViewById(R.id.search);
		addSortList();
		
		clear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				key_value.setText("");
				from_value.setText("");
				to_value.setText("");
				error_value.setText("");
				sort_menu.setSelection(0);
			}
		});       
		
		search.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				key=key_value.getText().toString();
				f=from_value.getText().toString();
				t=to_value.getText().toString();
				
				//converting these values into float
				if(from_value.getText().toString().length()!=0)
					from = Float.parseFloat(from_value.getText().toString());
				if(to_value.getText().toString().length()!=0)
					to = Float.parseFloat(to_value.getText().toString());
				
				sort_value = String.valueOf(sort_menu.getSelectedItem());
				
				if(key_value == null || key_value.getText().toString().length()==0)
				{
					error_value.setText("The keyword must not be empty.");
					error_flag=true;
				}
				else if(from_value.getText().toString().length()!=0 && from < 0)
				{
					if(from < 0)
					{
						error_value.setText(" Price from must be a positive integer or decimal number.");
						error_flag=true;
					}
				}
				else if(to_value.getText().toString().length()!=0 && to <= 0)
				{
					if(to <= 0)
					{
						error_value.setText("Price to must be a positive integer or decimal number.");
						error_flag=true;
					}
				}
				else if(from_value.getText().toString().length()!=0 && to_value.getText().toString().length()!=0 && to<from)
				{
					
					if(to < from)
					{
						error_value.setText("Price To must not be less than Price From.");
						error_flag=true;
					}
				}
				else
				{
					error_value.setText("");
					error_flag=false;
				}
				if(!error_flag)
				{

					callmyphp(key,f,t,sort_value);
				}

			}
		});

	}
	
	public void callmyphp(String key_val,String from_val,String to_val,String sort_val) {
		// Do something in response to button click
		String sort_string = new String();
		if(sort_val.equals("Best Match"))
			sort_string = "BestMatch";
		else if(sort_val.equals("Price: highest first"))
			sort_string = "CurrentPriceHighest";
		else if(sort_val.equals("Price + Shipping: highest first"))
			sort_string = "PricePlusShippingHighest";
		else if(sort_val.equals("Price + Shipping: lowest first"))
			sort_string = "PricePlusShippingLowest";
		String[] params = {key_val,from_val,to_val,sort_string};
		new JsonFetchTask(getApplicationContext()).execute(params);
	}


	class JsonFetchTask extends AsyncTask<String, String, String> {
		Context context;
		public JsonFetchTask(Context context)
		{
			this.context = context.getApplicationContext();
		}
		protected String doInBackground(String ... urls) {
			byte[] result=null;
			String str = "";
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://kakolu-hw9.elasticbeanstalk.com/");

			try {
				// Add your data
				//Toast.makeText(InitialActivity.this,"me1",Toast.LENGTH_SHORT).show();
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("key", urls[0]));
				nameValuePairs.add(new BasicNameValuePair("from", urls[1]));
				nameValuePairs.add(new BasicNameValuePair("to", urls[2]));
				nameValuePairs.add(new BasicNameValuePair("sort", urls[3]));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
					result = EntityUtils.toByteArray(response.getEntity());
					str = new String(result, "UTF-8");
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			catch (Exception e) {
				System.out.println(e.toString());
			}
			return str;
		}

		protected void onProgressUpdate(Integer... progress) {
			//setProgressPercent(progress[0]);
		}

		protected void onPostExecute(String result) {
			try {
				JSONObject jsonTestObj = new JSONObject(result);
				TextView error= (TextView) InitialActivity.this.findViewById(R.id.error_value);
				String validity = jsonTestObj.getString("jsonValid");
				if(validity.equals("false"))
				{					
					error.setText("No Results Found!");
					return;
				}
				else if(validity.equals("true"))
				{
					JSONObject jsonObj = new JSONObject(result);
					String res_no = jsonObj.getJSONObject("resultCount").getString("0");
					boolean x = res_no.equals("0");
					if(res_no.equals("0")){
					error.setText("No Results Found!");
					return;
					}
				}
				Intent intent = new Intent(context,ResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("com.kakolu.ebay.MESSAGE", result);
				context.startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void addSortList() {

		sort_menu = (Spinner) findViewById(R.id.sort_menu);
		List<String> list = new ArrayList<String>();
		list.add("Best Match");
		list.add("Price: highest first");
		list.add("Price + Shipping: highest first");
		list.add("Price + Shipping: lowest first");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sort_menu.setAdapter(dataAdapter);
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
