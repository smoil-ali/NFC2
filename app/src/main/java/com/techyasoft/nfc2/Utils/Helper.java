package com.techyasoft.nfc2.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techyasoft.nfc2.model.Profile;

import java.util.HashMap;
import java.util.List;

public class Helper {
    private static final String TAG=Helper.class.getSimpleName();
    private static final String LOGIN_INFO="login_info";
    private static final String MYPREF="MyPref";
    private static final String STATUS="status";
    public static void setProfileInfo(Profile profile, Context context){
        String info = ConvertProfileToString(profile);
        Log.i(TAG,info);
        SharedPreferences sharedPref = context.getSharedPreferences(MYPREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LOGIN_INFO, info);
        editor.apply();
    }

    public static Profile getProfileInfo(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(MYPREF,Context.MODE_PRIVATE);
        String profile = sharedPref.getString(LOGIN_INFO,"");
        Log.i(TAG,profile+" here");
        return ConvertStringToProfile(profile);
    }

    public static String ConvertProfileToString(Profile mProfile){
        return new Gson().toJson(mProfile);
    }

    public static Profile ConvertStringToProfile(String mProfile){
        return new Gson().fromJson(mProfile,new TypeToken<Profile>(){}.getType());
    }

    public static String ConvertListToString(HashMap<Integer,List<String>> list){
        return new Gson().toJson(list);
    }

    public static HashMap<Integer,List<String>> ConvertStringToList(String list){
        return new Gson().fromJson(list,new TypeToken<HashMap<Integer,List<String>>>(){}.getType());
    }

    public static String ConvertHashMapToString(HashMap<Integer,Integer> hashMap){
        return new Gson().toJson(hashMap);
    }

    public static HashMap<Integer,Integer> ConvertStringToHashMap(String hashMap){
        return new Gson().fromJson(hashMap,new TypeToken<HashMap<Integer,Integer>>(){}.getType());
    }

    public static void setLogInTrue(Context context,boolean status){
        SharedPreferences sharedPref = context.getSharedPreferences(MYPREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(STATUS,status);
        editor.apply();
    }

    public static boolean getLogInStatus(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(MYPREF,Context.MODE_PRIVATE);
        boolean stat = sharedPref.getBoolean(STATUS,false);
        Log.i(TAG,stat+" here");
        return stat;
    }


}
