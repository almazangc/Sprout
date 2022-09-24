package com.prototype.sprout.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    //From room
    @TypeConverter
    public static ArrayList<Integer> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    // To room
    @TypeConverter
    public static String fromArrayList(ArrayList<Integer> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
