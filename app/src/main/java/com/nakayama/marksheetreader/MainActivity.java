package com.nakayama.marksheetreader;

//import android.support.v7.app.ActionBarActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends Activity {
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView)findViewById(R.id.main_image);

        Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.mark_sheet);
        Bitmap src1 = src.copy(Bitmap.Config.ARGB_8888, true);
        Mat image = new Mat(src1.getWidth(),src1.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(src1,image);
        src = BitmapFactory.decodeResource(getResources(),R.drawable.white_sheet);
        Bitmap src2 = src.copy(Bitmap.Config.ARGB_8888,true);
        Mat template = new Mat(src2.getWidth(),src2.getHeight(), CvType.CV_8UC1);
        Utils.bitmapToMat(src2, template);
        Mat result = new Mat();
        Imgproc.matchTemplate(image,template,result,Imgproc.TM_CCOEFF_NORMED);
        Core.MinMaxLocResult maxr = Core.minMaxLoc(result);
        Point maxp = maxr.maxLoc;
        Point pt2 = new Point(maxp.x + template.width(),maxp.y+template.height());
        Mat dst = image.clone();
        Imgproc.rectangle(dst,maxp,pt2,new Scalar(0,255,0),2);
        Utils.matToBitmap(dst, src);
        mImageView.setImageBitmap(src);
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
}
