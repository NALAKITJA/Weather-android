package com.mpr.weather_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import helper.RSSParser;
import model.RSSWeather;
import model.RSSWeather2;

/**
 * Created by MPR on 14/1/2559.
 */
public class ResultActivity extends MainActivity {
    private ProgressDialog pDialog;
    private static String weather_url;
    RSSParser parser = new RSSParser();
    RSSWeather weather;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        //create an instance of the view object.
        textView = (TextView) findViewById(R.id.text_view);
        Button back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bac = new Intent(ResultActivity.this,MainActivity.class);
                startActivity(bac);
            }
        });
        Intent intent = getIntent();
        String description1 = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Log.d("Lat", description1);
        //"http://weather.yahooapis.com/forecastrss?w=12756339&u=c"
        weather_url = ("http://weather.yahooapis.com/forecastrss?w="+ description1 +"&u=c");
        /**
         * Calling a backgroung thread will loads recent data of a website
         * @param rss url of website
         * */
        new loadRSSFeedItems().execute(weather_url);


    }

    /**
     * Background Async Task to get RSS data from URL
     * */
    class loadRSSFeedItems extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(
                    ResultActivity.this);
            pDialog.setMessage("Loading weather...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting all recent data and showing them in text view.
         */
        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            // weather object of rss.
            weather = parser.getRSSFeedWeather(rss_url);

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed data into text view.
                     * */
                    String description = String.format("title: %s \n pubdate: %s \n temp: %s \n link: %s", weather.getTitle(), weather.getPubdate(), weather.getTemp(), weather.getLink());
                    textView.setText(description);
                }
            });
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }

}
