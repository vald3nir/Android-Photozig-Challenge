package com.photozig.videos;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class AppConfig {
    public static final String SERVER_URL = "http://pbmedia.pepblast.com/pz_challenge/";


    public static String getFolderDownload(Context context) {
        File folder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS),
                context.getString(R.string.app_name)
        );

        if (!folder.exists()) folder.mkdirs();
        return folder.getAbsolutePath();
    }

}
