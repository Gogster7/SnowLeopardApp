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
    public String loadUid(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        return sharedPreferences.getString("uid", null);


    }
    public boolean getAgreement(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TOS", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("tos", true);

    }
    public void setAgreement(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("TOS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("tos",false);
        editor.apply();
    }
    private String loaduid(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        return sharedPreferences.getString("uid",null);
    }
    public void saveprey(Prey prey,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<Prey> list=getprey(context);
        list.add(prey);
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new MyDateTypeAdapter()).create();;
        String json =gson.toJson(list);

        editor.putString("prey",json);
        editor.apply();

    }
    public ArrayList<Prey> getprey(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder().registerTypeAdapter(Timestamp.class,new MyDateTypeAdapter()).create();;
        String json =sharedPreferences.getString("prey",null);
        if (json==null){
            return new ArrayList<>();
        }
        else{


            Type type = new TypeToken<ArrayList<Prey>>() {}.getType();
            return gson.fromJson(json,type);



        }

    }
}
