package com.marktony.zhihudaily.refactor.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marktony.zhihudaily.refactor.data.DoubanMomentNewsThumbs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2017/6/17.
 */

public class DoubanTypeConverters {

    @TypeConverter
    public static String thumbListToString(List<DoubanMomentNewsThumbs> thumbs) {
        return new Gson().toJson(thumbs);
    }

    @TypeConverter
    public static List<DoubanMomentNewsThumbs> stringToThumbList(String string) {
        Type listType = new TypeToken<ArrayList<DoubanMomentNewsThumbs>>(){}.getType();
        return new Gson().fromJson(string, listType);
    }

}
