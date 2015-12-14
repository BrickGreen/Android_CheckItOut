package com.example.checkitout;

import java.util.Comparator;


/**
 * Created by Chiharu on 12/14/2015.
 */
public class CustomComparator implements Comparator<twitter4j.Status> {
    @Override
    public int compare(twitter4j.Status t1, twitter4j.Status t2) {
        Integer x = new Integer(t1.getFavoriteCount() + t1.getRetweetCount());
        Integer y = new Integer(t2.getFavoriteCount() + t2.getRetweetCount());
        return y.compareTo(x);
    }
}
