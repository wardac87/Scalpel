package com.example.main.scalpel;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * @see com.example.main.scalpel.util.SystemUiHider
 */
public class ImageFoundTextActivity extends Activity {
    public static ImageFoundTextActivity instance;
    public static SQLiteSearch[] matches;
    public static int searchIndex;
    public static String latestSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_found_text);

        instance = this;

        ImageView iv = (ImageView) (findViewById(R.id.imageViewFound));
        iv.setVisibility(View.VISIBLE);
        TextView tv = (TextView) (findViewById(R.id.textView));
        tv.setText("Last Search: "+latestSearch);

        searchIndex =0;
        RelativeLayout mainScreen = (RelativeLayout)(findViewById(R.id.foundDisplay));
        int baseWidth = mainScreen.getWidth()/2;
        int baseHeight = mainScreen.getHeight()/2;

        BitmapAsyncSQLite initial = new BitmapAsyncSQLite(PCHelper.myDataBase,matches[searchIndex]._id
                                  ,baseWidth,baseHeight, BitmapAsyncSQLite.textSearch);
        initial.execute();
    }

    public void homePageHandler(View v) {
        FindImageActivity.goHome = true;
        finish();
    }

    public static void asyncHandler(Bitmap b)
    {ImageView iv = (ImageView) (instance.findViewById(R.id.imageViewFound));
        iv.setImageBitmap(b);}


    public void findBetterHandler(View v)
    {
        searchIndex++;
        if (searchIndex >=matches.length)
        {searchIndex=0;}

        RelativeLayout mainScreen = (RelativeLayout)(findViewById(R.id.foundDisplay));
        int baseWidth = mainScreen.getWidth()/2;
        int baseHeight = mainScreen.getHeight()/2;

        BitmapAsyncSQLite attempt = new BitmapAsyncSQLite(PCHelper.myDataBase,matches[searchIndex]._id
                                       , baseWidth, baseHeight, BitmapAsyncSQLite.textSearch);
        attempt.execute();
    }

    public void tryAgainHandler(View v)
    {
        FindImageActivity.goHome=false;
        finish();
    }

}