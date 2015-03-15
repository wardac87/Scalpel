package com.example.main.scalpel;

/**
 * Created by Aaron on 3/13/2015.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;


/**
 * Created by Aaron on 3/8/2015.
 */
class BitmapAsyncSQLite extends AsyncTask<Void, Void, Bitmap> {
    private static  WeakReference<SQLiteDatabase> dataBaseReference;
    private static String blobToFind;

  public static Bitmap resourceImage;

    //Dimensions are proportional to a text Button in MainActivity
    //These constants decide the scaling.
    public static final int heightScale = 1;
    public static final int widthScale = 1;
    public static int baseHeight;
    public static int baseWidth;

    public static final int imageSearch= 76543; //chosen arbitrarily
    public static final int textSearch = 891011; //chosen arbitrarily
    public static int signalCode;

    public BitmapAsyncSQLite(SQLiteDatabase dataBaseIn, String blobIndex, int widthIn, int heightIn,int signal) {
        // Use a WeakReference to ensure the Cache can be garbage collected
        BitmapAsyncSQLite.dataBaseReference = new WeakReference<SQLiteDatabase>(dataBaseIn);
        BitmapAsyncSQLite.blobToFind = blobIndex;

        baseWidth = widthIn;
        baseHeight=heightIn;
        signalCode = signal;
    }

    //Downsample an image to fit it into an ImageButton
    public static int calculateReduceInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        //Downsample by powers of 2 until it fits the ImageButton
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    ||  (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
            //We want it smaller than the required bounds, so multiply by 2 again
            inSampleSize*=2;
        }
        return inSampleSize;
    }

    static Cursor getBlobLocation()
    {String blobIndexString =  blobToFind;
        String[] column = {"IMAGE"};
        String condition = "_id = ?";
        String[] args = {blobIndexString};

        Cursor cursor= dataBaseReference.get().query(PCHelper.tableName,column, condition, args,null,null,null);
        return cursor;
    }
    public static Bitmap decodeSampledBitmapFromSQLiteDataBase(int reqWidth, int reqHeight)
    {
        Cursor myCursor = getBlobLocation();
        myCursor.moveToFirst(); //should only be one result
        byte[] temp = myCursor.getBlob(myCursor.getColumnIndex("IMAGE"));

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // Calculate amount to down-sample
        options.inSampleSize = calculateReduceInSampleSize(options, reqWidth, reqHeight);

        //further data-saving techniques
        options.inPreferredConfig= Bitmap.Config.ARGB_4444; //data-saving format
        options.inPreferQualityOverSpeed=false;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(temp, 0, temp.length,options);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Void... params) {
        Log.v("Async", "Task Started");

        int desiredHeight = heightScale*baseWidth;
        int desiredWidth = widthScale*baseHeight;
         resourceImage =decodeSampledBitmapFromSQLiteDataBase( desiredWidth, desiredHeight);

        return null; //No return used, but function must be of type Bitmap
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (signalCode == imageSearch)
        { ImageFoundActivity.asyncHandler(resourceImage);}
        else if (signalCode == textSearch)
        {ImageFoundTextActivity.asyncHandler(resourceImage);}
    }
}