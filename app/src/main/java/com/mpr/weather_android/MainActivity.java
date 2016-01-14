package com.mpr.weather_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import helper.RSSParser2;
import model.RSSWeather2;

public class MainActivity extends Activity {

    private ProgressDialog progressDialog;
    private static String url_weather;
    RSSParser2 parser2 = new RSSParser2();
    RSSWeather2 weather2;
    TextView latitu, longti;

    public static final String EXTRA_MESSAGE = "detail latitude and longtitude";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitu = (TextView) findViewById(R.id.latitude);
        longti = (TextView) findViewById(R.id.logtitude);

        Button sub = (Button) findViewById(R.id.submit);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("123", "ClickOk");

                EditText lat = (EditText) findViewById(R.id.editlatitude);
                String la = lat.getText().toString();
                EditText lon = (EditText) findViewById(R.id.editlongtitude);
                String lo = lon.getText().toString();

                if (la.matches("")|| lo.matches("")) {

                    lat.setError("Please fill Latitude");
                    lon.setError("Please fill Longitude");
                    return;
                }
                else {
                    Log.d("lat",la);
                    Log.d("lon",lo);


                    url_weather = ("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text=%22" + la + "," + lo + "%22%20and%20gflags=%22R%22");
                    Log.d("Long", url_weather);
                    /**
                     * Calling a backgroung thread will loads recent data of a website
                     * @param rss url of website
                     * */
                    //new loadRSSFeedItems().execute(weather_url);
                }
                new loadRSSFeedItems().execute(url_weather);
                //startActivity(intent);
            }

            class loadRSSFeedItems extends AsyncTask<String, String, String> {


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Loading weather...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }

                @Override
                protected String doInBackground(String... args) {
                    String query_url = args[0];
                    weather2 = parser2.getRSSFeedWeather(query_url);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            /**
                             * Updating parsed data into text view.
                             * */

                            Intent intent1 = new Intent(MainActivity.this, ResultActivity.class);
                            String description1 = weather2.getwoeid();
                            intent1.putExtra(EXTRA_MESSAGE, description1);
                            Log.d("Long", description1);

                            startActivity(intent1);
                        }
                    });
                    return null;
                }

                /**
                 * After completing background task Dismiss the progress dialog
                 **/
                protected void onPostExecute(String args) {
                    // dismiss the dialog after getting all products
                    progressDialog.dismiss();
                }


            }
        })
        ;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.mpr.weather_android/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.mpr.weather_android/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}
