package com.example.checkitout;



/**
 * Created by aspendavis on 11/22/15.
 */
public class Tweet {
    String tweetBy;
    String tweet;
    String favorites;

    public Tweet(String tweetBy, String tweet, String favorites) {
        this.tweetBy = tweetBy;
        this.tweet = tweet;
        this.favorites = favorites;
    }

    public String getTweetBy() {
        return tweetBy;
    }

    public void setTweetBy(String tweetBy) {
        this.tweetBy = tweetBy;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getFavorites() { return favorites; }

    public void setFavorites (String favorites) { this.favorites = favorites;}
}

