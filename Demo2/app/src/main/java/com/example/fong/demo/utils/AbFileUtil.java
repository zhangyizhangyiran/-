package com.example.fong.demo.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Fong on 17/1/7.
 */
public class AbFileUtil {
    public static File getCacheDir(){

        File externalStorageDirectory = Environment.getDownloadCacheDirectory();

        return externalStorageDirectory;
    }

}
