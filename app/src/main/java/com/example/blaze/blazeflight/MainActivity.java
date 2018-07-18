package com.example.blaze.blazeflight;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView stickyView;
    private View heroImageView;

    private View stickyViewSpacer;

    private int MAX_ROWS = 20;
String destination;
String date;
String url;
String origin;
ArrayList<String>mydata;
File path;
    private String TAG = MainActivity.class.getSimpleName();

        private ProgressDialog pDialog;
        private ListView lv;
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       Intent i2 = getIntent();
       Bundle b = i2.getExtras();

       if (b != null) {
           String from = (String) b.get("origin");
           origin = from;
           String to = (String) b.get("destination");
           destination = to;
           String on = (String) b.get("on");
           date = on;
       }


       flightList = new ArrayList<>();
       mydata=new ArrayList<>();

       lv = (ListView) findViewById(R.id.list);
       heroImageView = findViewById(R.id.heroImageView);
       stickyView = (TextView) findViewById(R.id.stickyView);
       LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View listHeader = inflater.inflate(R.layout.listheader, null);
       stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

       lv.addHeaderView(listHeader);


       lv.setOnScrollListener(new AbsListView.OnScrollListener() {

                                        @Override
                                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                                        }

                                        @Override
                                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                            if (lv.getFirstVisiblePosition() == 0) {
                                                View firstChild = lv.getChildAt(0);
                                                int topY = 0;
                                                if (firstChild != null) {
                                                    topY = firstChild.getTop();
                                                }

                                                int heroTopY = stickyViewSpacer.getTop();
                                                stickyView.setY(Math.max(0, heroTopY + topY));
                                                heroImageView.setY(topY * 0.9f);
                                            }
                                        }
       });
       new GetFlights().execute();
if(!date.isEmpty())
{
    url = "https://api.sandbox.amadeus.com/v1.2/flights/extensive-search?apikey="+BuildConfig.ApiKey13+"&origin="+ origin + "&destination=" + destination + "&departure_date=" + date + "&one-way=true";

   }
   else
{
    url = "https://api.sandbox.amadeus.com/v1.2/flights/extensive-search?apikey="+BuildConfig.ApiKey13+"&origin=" + origin + "&destination=" + destination + "&one-way=true&aggregation_mode=day";
}


   }
    ArrayList<HashMap<String, String>> flightList;

        private class GetFlights extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.makeServiceCall(url);

                Log.e(TAG, "Response from url: " + jsonStr);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        String origin="FROM :\t"+jsonObj.getString("origin");
                        String currency="Currency :\t"+jsonObj.getString("currency");
                        JSONArray results = jsonObj.getJSONArray("results");

                        for (int i = 0; i < results.length(); i++) {
                            JSONObject c = results.getJSONObject(i);
                            String destination = "TO :\t"+c.getString("destination");
                            String departure_date ="ON :\t"+ c.getString("departure_date");
                            String price = "PRICE :\t"+c.getString("price");
                            String airline = "AIRLINE :\t"+c.getString("airline");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.put("currency",currency);
                            contact.put("origin",origin);
                            contact.put("destination", destination);
                            contact.put("departure_date", departure_date);
                            contact.put("price",price);
                            contact.put("airline",airline);
                            flightList.add(contact);
                            mydata.add(currency+"#"+origin+"#"+destination+"#"+departure_date+"#"+price+"#"+airline);



                        }
                    }catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (pDialog.isShowing())
                    pDialog.dismiss();

                ListAdapter adapter = new SimpleAdapter(
                        MainActivity.this, flightList,
                        R.layout.list_item, new String[]{"origin", "destination", "departure_date","currency"
                        , "price", "airline"}, new int[]{R.id.origin,
                        R.id.destination, R.id.date,R.id.currency, R.id.price, R.id.airline});


                lv.setAdapter(adapter);

                String csv = "/sdcard/output.csv";
                CSVWriter writer = null;
                try {
                    writer = new CSVWriter(new FileWriter(csv));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                List<String[]> data = new ArrayList<String[]>();

                for(int i=0;i<mydata.size();i++)
                {
                    String [] data1=mydata.get(i).split("#");

                    data.add(data1);
                }


                String [] column = "Currency#Origin#Destination#DOJ#Price#Airline".split("#");
                writer.writeNext(column);

                writer.writeAll(data);
                Toast.makeText(getApplicationContext(),"CSV File Exported To SD CARD As output.csv", Toast.LENGTH_SHORT).show();

                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }


}
