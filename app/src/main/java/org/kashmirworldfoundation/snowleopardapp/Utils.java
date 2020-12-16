package org.kashmirworldfoundation.snowleopardapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public class Utils {
    public Member loaduser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson= new Gson();
        String json = sharedPreferences.getString("user", null);
        Type type =new TypeToken<Member>(){}.getType();
        return gson.fromJson(json,type);
    }
}
