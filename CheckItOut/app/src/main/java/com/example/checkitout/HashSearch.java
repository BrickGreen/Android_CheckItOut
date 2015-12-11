package com.example.checkitout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by aspendavis on 12/11/15.
 */
public class HashSearch extends AsyncTask<String, Void, Integer> {
        ArrayList<Tweet> tweets;
        final int SUCCESS = 0;
        final int FAILURE = 1;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(this, "", "searching");
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setDebugEnabled(true)
                        .setOAuthConsumerKey(TWIT_CONS_KEY)
                        .setOAuthConsumerSecret(TWIT_CONS_SEC_KEY)
                        .setOAuthAccessToken(TWIT_TOKEN)
                        .setOAuthAccessTokenSecret(TWIT_TOKEN_SEC);

                Twitter twitter = new TwitterFactory(builder.build()).getInstance();
                double latitude;
                double longitude;
                if(location !=null)
                {
                    latitude = newLat;
                    longitude = newLong;
                }
                else{
                    latitude = currentLatitude;
                    longitude = currentLongitude;
                }
                GeoLocation location = new GeoLocation(latitude, longitude);
                Log.d("Lat = ", Double.toString(latitude));
                Log.d("Long = ", Double.toString(longitude));
                Query query = new Query();
                query.setCount(100);
                query.setGeoCode(location, 25, Query.MILES);


                QueryResult result = twitter.search(query);

                List<twitter4j.Status> tweeters = result.getTweets();
                StringBuilder str = new StringBuilder();
                if (tweeters != null) {
                    this.tweets = new ArrayList<Tweet>();
                    for (twitter4j.Status tweet : tweeters) {
                        str.append("@").append(tweet.getUser().getScreenName()).append(" - ").append(tweet.getText()).append("\n");
                        System.out.println(str);
                        this.tweets.add(new Tweet("@" + tweet.getUser().getScreenName(), tweet.getText()));
                    }
                    return SUCCESS;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return FAILURE;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result == SUCCESS) {
                lstMedia.setAdapter(new TweetAdapter(MapsActivity.this, tweets));
            } else {
                Toast.makeText(MapsActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        }
    }
}
