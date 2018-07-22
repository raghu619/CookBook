package com.example.android.cookbook.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.widget.ImageView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.globaldata.Global_Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by raghvendra on 14/7/18.
 */

public class Video_Image_AsyncTask extends AsyncTask<File, File, File>{


        private  String video_path;
        private  final ThreadLocal<ImageView> imageView = new ThreadLocal<>();
    public  synchronized static void retrive_image_from_video(String videoPath, ImageView imageView){
        Video_Image_AsyncTask task=new Video_Image_AsyncTask();
        task.video_path=videoPath;
        task.imageView.set(imageView);
        task.execute();
    }


    @Override
    protected File doInBackground(File... files) {

        MediaMetadataRetriever mediaMetadataRetriever = null;
        File file = null;
        Bitmap bitmap;
        try{
            if(!video_path.equals(Global_Constants.NO_DATA)){

                byte[] byteArray = video_path.getBytes("UTF-8");
                String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Global_Constants.APP_NAME;
                File directory = new File(directoryPath);
                if (!directory.isDirectory())
                    directory.mkdirs();

                file = new File(directoryPath + File.separator + base64 + ".jpeg");
                if(!file.exists()){

                    file.createNewFile();
                    mediaMetadataRetriever = new MediaMetadataRetriever();

                    mediaMetadataRetriever.setDataSource(video_path,new HashMap<String, String>());
                    bitmap = mediaMetadataRetriever.getFrameAtTime();
                    scaleDown(bitmap,file);



                }

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }


        return file;
    }




    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if(file!=null){
            Picasso.get().load(file).placeholder(R.drawable.placeholder_video).into(imageView.get());

        }
    }

    public static void scaleDown(Bitmap realImage, File file) {
        int maxSize = 400;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        int width = realImage.getWidth();
        int height = realImage.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        realImage = Bitmap.createScaledBitmap(realImage, width, height, true);

        try {
            FileOutputStream fOut = new FileOutputStream(file);
            realImage.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!realImage.isRecycled())
            realImage.recycle();
    }
}
