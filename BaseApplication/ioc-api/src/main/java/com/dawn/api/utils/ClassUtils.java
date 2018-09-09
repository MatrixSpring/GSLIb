package com.dawn.api.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {
    private static final String EXTRACTED_NAME_EXT = ".classes";
    private static final int VM_WITH_MULTIDEX_VERSION_MAJOR = 2;
    private static final int VM_WITH_MULTIDEX_VERSION_MINOR = 1;
    private static final String PREFS_FILE = "multidex.version";
    private static final String KEY_DEX_NUMBER = "dex.number";
    private static final String SECONDARY_FOLDER_NAME = "code_cache" + File.separator + "secondary-dexes";
    private static final String EXTRACTED_SUFFIX = ".zip";

    private static SharedPreferences getMultiDexPreferences(Context context){
        return context.getSharedPreferences(PREFS_FILE, Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB ? Context.MODE_PRIVATE : Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    public static List<String> getFileNameByPackageName(Context context, String packageName) throws  PackageManager.NameNotFoundException, IOException {
        List<String> classNames = new ArrayList<>();
//        for(String path : get){
//
//        }

        return classNames;
    }

    /**
     *
     * @return
     */
    private static boolean isYunOS(){
        try{
            String version = System.getProperty("ro.yunos.version");
            String vmName = System.getProperty("java.vm.name");
            return (vmName != null && vmName.toLowerCase().contains("lemur"))
                    || (version != null && version.trim().length() > 0);
        }catch (Exception e){
            return false;
        }
    }
}
