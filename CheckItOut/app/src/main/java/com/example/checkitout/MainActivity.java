package com.example.checkitout;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity {

    ListView lstMedia;
    Button btnSubmit;
    Button btnTestMap;
    private final String TWIT_CONS_KEY = "0PidwQdIP3Yf1oybRPYLal6A5";
    private final String TWIT_CONS_SEC_KEY = "q5hxuA2C7vz8FD8ebt5iG0MeoK9ua1puem43t0Ydh8NPyaKp3h";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstMedia = (ListView) findViewById(R.id.list);
        btnSubmit = (Button) findViewById(R.id.btnLocation);
        btnTestMap = (Button) findViewById(R.id.btnTestMap);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchOnTwitter().execute();
            }
        });

        btnTestMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this,
                        MapsActivity.class);
                startActivity(myIntent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class SearchOnTwitter extends AsyncTask<String, Void, Integer> {
        ArrayList<Tweet> tweets;
        final int SUCCESS = 0;
        final int FAILURE = 1;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", getString(R.string.searching));
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);

                OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

                builder = new ConfigurationBuilder();
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
                builder.setOAuth2TokenType(token.getTokenType());
                builder.setOAuth2AccessToken(token.getAccessToken());

                Twitter twitter = new TwitterFactory(builder.build()).getInstance();

                GeoLocation location = new GeoLocation(38.2177972,-85.7628502);

                Query query = new Query();
                query.setGeoCode(location, 5, Query.MILES);

                QueryResult result = twitter.search(query);

                List<twitter4j.Status> tweeters = result.getTweets();
                StringBuilder str = new StringBuilder();
                if (tweeters != null) {
                    this.tweets = new ArrayList<Tweet>();
                    for (twitter4j.Status tweet : tweeters) {
                        str.append("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + "\n");
                        Log.d("tweet", "tweet");
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
                lstMedia.setAdapter(new TweetAdapter(MainActivity.this, tweets));
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        }
    }
}