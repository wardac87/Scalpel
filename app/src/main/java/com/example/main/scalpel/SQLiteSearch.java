package com.example.main.scalpel;

/**
 * Created by Aaron on 3/14/2015.
 */
//Allows sorting through SQLite table, using id as the key
//and nonSimiliarity as the parameter to be used by the sort
public class SQLiteSearch {
    public String _id;
    public int nonSimiliarity;

 public SQLiteSearch(String idIn, int nonSimiliarityIn)
 {
     _id = idIn;
     nonSimiliarity=nonSimiliarityIn;
 }

}
