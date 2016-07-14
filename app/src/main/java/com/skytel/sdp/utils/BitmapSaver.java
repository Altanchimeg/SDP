package com.skytel.sdp.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by bayarkhuu on 7/14/2016.
 */
public class BitmapSaver {
    public static void saveBitmapToFile(Bitmap bitmap, String imageName) {
        File file = new File(Environment.getExternalStorageDirectory(),
                imageName);
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File readBitmapFromFile(String imageName) {
        return new File(Environment.getExternalStorageDirectory(),
                imageName);
    }
}